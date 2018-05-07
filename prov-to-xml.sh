#!/bin/bash


function output_execute2() {
  grep -E "^[^ ]* [^ ]* [^ ]* [^ ]* $1 .*\$" $LOGDIR/execute2.event > /tmp/prov-xml-output-execute2.tmp || echo "ERROR: Couldn't find thread $1"
  while read start duration invid thread result site rest; do
    echo " <execute2>"
    echo "  <invocationid>$invid</invocationid>"
    echo "  <start>$start</start>"
    echo "  <duration>$duration</duration>"
    echo "  <status>$status</status>"
    echo "  <site>$site</site>"
    output_kickstart $invid
    echo " </execute2>"
  done < /tmp/prov-xml-output-execute2.tmp
}

function output_kickstart() {
 KFN=$LOGBASE/$1-kickstart.xml
 if [ -f $KFN ]; then
   cat $KFN
 else
   echo "<!-- no kickstart record - expected $KFN -->"
 fi
}

function output_dataset() {

  echo "<dataset identifier=\"$1\">"
  FN="$(cat $LOGDIR/dataset-filenames.txt | grep -E "^$1 " | cut -f 2 -d ' ')"
 echo "<filename>$FN</filename>"

# way i metadata impl
# comment out to have only way ii
# needs to be before recursion becasuse in bash, recursion doesn't
# have local variables (by default?)
#  if echo $FN | grep -E '[0-4]\.in$'; then
#    ./prov-mfd-meta-to-xml $1
#  fi
# end wayi metadata



  for b in $(grep -E "^$1 " $LOGDIR/tie-containers.txt | cut -f 2 -d ' '); do
    output_dataset $b
  done



  echo "</dataset>"
}

# dataset containment

export LOGBASE=$(dirname $1)
export RUNID=$(basename $1 .log | cut -f 2- -d '-')

echo "<workflow runid=\"$RUNID\">"

cat $LOGDIR/tie-containers.txt | cut -f 1 -d ' ' | sort  | uniq  > /tmp/known-outer-containers.tmp

cat $LOGDIR/tie-containers.txt | cut -f 2 -d ' ' | sort  | uniq  > /tmp/known-inner-containers.tmp

for a in $(cat /tmp/known-outer-containers.tmp); do

if grep -E "^$a\$" /tmp/known-inner-containers.tmp > /dev/null; then
  echo "<!-- skipping $a contained by something else -->"
else

  output_dataset $a

fi

done


# invocations

while read start duration iduri thread endstat tr rest ; do 

  echo "<execute>"
  echo " <id>$iduri</id>"
  echo " <start>$start</start>"
  echo " <duration>$duration</duration>"
  echo " <endstate>$endstat</endstate>"
  echo " <trname>$tr</trname>"
  echo " <thread>$thread</thread>"
  output_execute2 $thread
  echo "</execute>"

done <  $LOGDIR/execute.global.event

# data/invocation tie

while read thread direction dataset paramname rest ; do
  echo "<tie>"
  echo "  <thread>$thread</thread>"
  echo "  <direction>$direction</direction>"
  echo "  <dataset>$dataset</dataset>"
  echo "  <param>$paramname</param>"
  echo "</tie>"
done < $LOGDIR/tie-data-invocs.txt


echo "</workflow>"

