#!/bin/bash

PROVDIR=$(dirname $0)
pushd $PROVDIR 
PROVDIR=$(pwd)
popd

# we need to keep this out of the log-proceesing dir because import
# of individual runs will clean other files.

source $PROVDIR/etc/provenance.config
export PATH=$PROVDIR:$PATH

query="select pgraph_edge.* from proc,pgraph_edge where (proc.id=pgraph_edge.parent or proc.id=pgraph_edge.child) and proc.run_id='$1';"

echo "digraph \"$1\" {" > $1.dot
#$SQLCMD --tuples-only -c "$query" | sed -e '/^ *$/d' | sort | uniq | awk '{print "\""$1"\"" " -> " "\""$3"\""}' >> $1.dot
$SQLCMD --tuples-only -c "$query" | sed -e '/^ *$/d' > /tmp/$1.tmp

while read parent separator child; do
	isfc=$(echo "$parent" | grep ^execute)
	if [ "X" == "X$isfc" ]; then
		variable="$parent"
		functioncall="$child"
	else
		variable="$child"
		functioncall="$parent"
	fi
	
	variabletype=$($SQLCMD --tuples-only -c "select type from variable where id='$variable'" | awk '{print $1}')
	functioncalllabel="$functioncall"	
	variablelabel="$variable"
	if [ "$variabletype" == "mapped" ]; then
		variablelabel="mapped:"$($SQLCMD --tuples-only -c "select filename from variable where id='$variable'" | awk '{print $1}')
	fi
	if [ "$variabletype" == "primitive" ]; then
		variablelabel="primitive:"$($SQLCMD --tuples-only -c "select value from variable where id='$variable'" | awk '{print $1}')
	fi
	if [ "$variabletype" == "composite" ]; then
		variablelabel="composite"
	fi
		
	
	functioncalllabel=$($SQLCMD --tuples-only -c "select name from function_call where id='$functioncall'" | awk '{print $1}')
	parameter=$($SQLCMD --tuples-only -c "select parameter from ds_use where function_call_id='$functioncall' and variable_id='$variable';" | awk '{print $1}')

	echo "\"$variable\" [ label=\"$variablelabel\", style=filled, fillcolor=lightcyan ];" >> /tmp/$1.header.dot
	echo "\"$functioncall\" [ label=\"$functioncalllabel\", shape=box, style=filled, fillcolor=lightcyan ];" >> /tmp/$1.header.dot
	echo "\"$parent\" -> \"$child\" [ label=\"$parameter\" ];" >> /tmp/$1.body.dot

done < /tmp/$1.tmp

cat /tmp/$1.header.dot | sort | uniq >> $1.dot
cat /tmp/$1.body.dot >> $1.dot
rm  /tmp/$1.header.dot /tmp/$1.body.dot 
echo "}" >> $1.dot
dot -Tsvg -o $1.svg $1.dot
