#!/bin/bash

echo Generating OPM for entire sqlite3 database

rm -f ids.txt
touch ids.txt

mkid() {
  if ! grep --silent "^$1\$" ids.txt  ; then
    echo $1 >> ids.txt
  fi
  echo -n x
  grep -n "^$1\$" ids.txt | cut -f 1 -d ':'
}

mkxmldatetime() {
TZ=UTC date -j -f %s $1 +"%Y-%m-%dT%H:%M:%SZ"

# this includes the TZ, but not in the correct format for xsd:dateTime
# - xsd dateTime requires a colon separating the TZ hours and minutes
# date -j -f %s $1 +"%Y-%m-%dT%H:%M:%S%z"

}

rm -f opm.xml

echo "<opmGraph xmlns=\"http://openprovenance.org/model/v1.01.a\" xmlns:swift=\"tag:benc@ci.uchicago.edu,2008:swift:opm:20090419\">" > opm.xml

# TODO - there are actually many accounts here, if compound procedure
# nesting is regarded as presenting multiple accounts.
# For now, emit everything into a single account, which probably
# violates some (explicit or implicit) integrity rules.
echo "<accounts><account id=\"base\"><value /></account></accounts>" >> opm.xml

echo "<processes>" >> opm.xml

sqlite3 -separator ' ' -batch provdb "select * from processes;"  |
  while read id type ; do
    flatid=$(mkid $id)
    echo "  <process id=\"$flatid\">"
    echo "    <account id=\"base\" />"
    echo "    <value>"
    echo "    <swift:type>$type</swift:type>"
    echo "    <swift:uri>$id</swift:uri>"


    if [ "$type" == "execute" ]; then
      sqlite3 -separator ' ' -batch provdb "select * from executes where id='$id';"  | ( read  id starttime duration finalstate app scratch; echo "    <swift:executeinfo starttime=\"$starttime\" duration=\"$duration\" endstate=\"$finalstate\" app=\"$app\" scratch=\"$scratch\"/>" )
    fi

    sqlite3 -separator ' ' -batch provdb "select procedure_name from invocation_procedure_names where execute_id='$id';" | ( read pn ; echo "    <swift:name>$pn</swift:name>")

   # TODO type handling for other types

    echo "    </value>"
    echo "  </process>"
  done >> opm.xml

echo "</processes>" >> opm.xml

echo "<artifacts>" >> opm.xml

# we need a list of all artifacts here. for now, take everything we can
# find in the tie-data-invocs and containment tables, uniquefied.
# This is probably the wrong thing to do?

sqlite3 -separator ' ' -batch provdb "select outer_dataset_id from dataset_containment;" > tmp-dshandles.txt
sqlite3 -separator ' ' -batch provdb "select inner_dataset_id from dataset_containment;" >> tmp-dshandles.txt
sqlite3 -separator ' ' -batch provdb "select dataset_id from dataset_usage;" >> tmp-dshandles.txt

cat tmp-dshandles.txt | sort | uniq > tmp-dshandles2.txt

while read artifact ; do
artifactid=$(mkid $artifact)
echo "  <artifact id=\"$artifactid\">"
echo "    <value>"
echo "    <swift:uri>$artifact</swift:uri>"

sqlite3 -separator ' ' -batch provdb "select inner_dataset_id from dataset_containment where outer_dataset_id='$artifact';" | while read innerartifact ; do
  innerflat=$(mkid $innerartifact)
  echo "<swift:contains ref=\"$innerflat\" />"
 done

sqlite3 -separator ' ' -batch provdb "select filename from dataset_filenames where dataset_id='$artifact';" | while read fn ; do
  echo "<swift:filename>$fn</swift:filename>"
 done

sqlite3 -separator ' ' -batch provdb "select value from dataset_values where dataset_id='$artifact';" | while read value ; do
  encvalue=$(echo $value | sed 's/</\&lt;/g')
  echo "<swift:value>$encvalue</swift:value>"
 done

echo "    </value>"
echo "    <account id=\"base\" />"
echo "  </artifact>"
done < tmp-dshandles2.txt >> opm.xml

echo "</artifacts>" >> opm.xml

# this agent is the Swift command-line client
# TODO other agents - the wrapper script invocations at least
echo "<agents>" >> opm.xml
echo "  <agent id=\"swiftclient\"><value/></agent>" >> opm.xml
echo "</agents>" >> opm.xml

echo "<causalDependencies>" >> opm.xml

# other stuff can do this in any order, but here we must probably do it
# in two passes, one for each relation, in order to satisfy schema.
# but for now do it in a single pass...

sqlite3 -separator ' ' -batch provdb "select * from dataset_usage;" |
 while read thread direction dataset variable rest; do 
  datasetid=$(mkid $dataset)
  threadid=$(mkid $thread)
  if [ "$direction" == "I" ] ; then
    echo "  <used>"
    echo "    <effect id=\"$threadid\" />"
    echo "    <role value=\"$variable\" />"
    echo "    <cause id=\"$datasetid\" />"
    echo "    <account id=\"base\" />"
    echo "  </used>"
  elif [ "$direction" == "O" ] ; then
    echo "  <wasGeneratedBy>"
    echo "    <effect id=\"$datasetid\" />"
    echo "    <role value=\"$variable\" />"
    echo "    <cause id=\"$threadid\" />"
    echo "    <account id=\"base\" />"
    echo "  </wasGeneratedBy>"
  else
    echo ERROR: unknown dataset usage direction: $direction
  fi
done  >> opm.xml

# attach timings of executes

#sqlite3 -separator ' ' -batch provdb "select * from executes where id='$id';"  | ( read  id starttime duration finalstate app scratch; echo "    <swift:executeinfo starttime=\"$starttime\" duration=\"$duration\" endstate=\"$finalstate\" app=\"$app\" scratch=\"$scratch\"/>" )

# TODO for now, don't put any different between the no-later-than and
# no-earlier-than times. in reality, we know the swift log timestamp
# resolution and can take that into account

sqlite3 -separator ' ' -batch provdb "select * from executes;" | while read id starttime duration finalstate app scratch ; do
  (  echo "<wasControlledBy><effect id=\"$(mkid $id)\"/><role />" ;
     echo "<cause id=\"swiftclient\"/>" ;
     export XMLSTART=$(mkxmldatetime $starttime);
     echo "<start><noEarlierThan>$XMLSTART</noEarlierThan><clockId>swiftclient</clockId></start>" ; 
     export E=$(mkxmldatetime $(echo $starttime + $duration | bc -l)) ; 
     echo "<end><noLaterThan>$E</noLaterThan><clockId>swiftclient</clockId></end>" ; 
     echo "</wasControlledBy>" ) >> opm.xml 
done

echo "</causalDependencies>" >> opm.xml


echo "</opmGraph>" >> opm.xml
echo Finished generating OPM, in opm.xml

