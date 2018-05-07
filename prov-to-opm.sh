#!/bin/bash

echo Generating OPM for a single run

rm -f opm.xml

# TODO make swift-opm-ns into a proper URI
echo "<opmGraph xmlns=\"http://openprovenance.org/model/v1.01.a\" xmlns:swift=\"tag:benc@ci.uchicago.edu,2008:swift:opm:20090311\">" > opm.xml

echo "<accounts><account id=\"base\" /></accounts>" >> opm.xml

echo "<processes>" >> opm.xml

while read time duration globalthread localthread endstate app scratch; do

threadid=$(echo $globalthread | sed 's/[:,@.]/-/g')

echo "  <process id=\"$threadid\">"
echo "    <account id=\"base\" />"
echo "    <swift:info starttime=\"$time\" duration=\"$duration\" endstate=\"$endstate\" app=\"$app\" scratch=\"$scratch\"/>"
echo "    <swift:uri>$globalthread</swift:uri>"
# TODO no value here - this is some URI into an ontology, which is don't
# really know how should be mapped from Swift
echo "  </process>"

done < execute.global.event >> opm.xml

echo "</processes>" >> opm.xml

# TODO artifacts

echo "<artifacts>" >> opm.xml

# we need a list of all artifacts here. for now, take everything we can
# find in the tie-data-invocs and containment tables, uniquefied.
# This is probably the wrong thing to do.

while read outer inner; do
  echo $input
  echo $output
done < tie-containers.txt > tmp-dshandles.txt

while read t d dataset rest ; do
  echo $dataset
done < tie-data-invocs.txt >> tmp-dshandles.txt

cat tmp-dshandles.txt | sort | uniq > tmp-dshandles2.txt

while read artifact ; do
artifactid=$(echo $artifact | sed 's/[:,@.]/-/g')
echo "  <artifact id=\"$artifactid\">"
echo "    <account id=\"base\" />"
echo "    <swift:uri>$artifact</swift:uri>"
echo "  </artifact>"
done < tmp-dshandles2.txt >> opm.xml

echo "</artifacts>" >> opm.xml


echo "<causalDependencies>" >> opm.xml

# other stuff can do this in any order, but here we must probably do it
# in two passes, one for each relation, in order to satisfy schema.
# but for now do it in a single pass...

while read thread direction dataset variable rest; do 
  datasetid=$(echo $dataset | sed 's/[:,@.]/-/g')
  threadid=$(echo $thread | sed 's/[:,@.]/-/g')
  if [ "$direction" == "input" ] ; then
    echo "  <used>"
    echo "    <effect id=\"$threadid\" />"
    echo "    <role value=\"$variable\" />"
    echo "    <cause id=\"$datasetid\" />"
    echo "    <account id=\"base\" />"
    echo "  </used>"
  else
    echo "  <wasGeneratedBy>"
    echo "    <effect id=\"$datasetid\" />"
    echo "    <role value=\"$variable\" />"
    echo "    <cause id=\"$threadid\" />"
    echo "    <account id=\"base\" />"
    echo "  </wasGeneratedBy>"
  fi
done < tie-data-invocs.txt >> opm.xml



echo "</causalDependencies>" >> opm.xml

echo "</opmGraph>" >> opm.xml
echo Finished generating OPM, in opm.xml

