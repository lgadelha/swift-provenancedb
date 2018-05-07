#!/bin/bash

export RUNID=$(basename $1 .log)

export WFID="execute:${RUNID}:"

# TODO is there already a URI form for identifying workflows?
export WF="${RUNID}"

echo Generating Datalog for $RUNID

# this gives a distinction between the root process for a workflow and the
# workflow itself. perhaps better to model the workflow as a process
echo "isProcess('${WFID}0')." > tmp.datalog
echo "hasType('${WFID}0', 'rootthread')." >> tmp.datalog
echo "hasName('${WFID}0', 'name')." >> tmp.datalog
echo "isInScriptRun('${WFID}0', '$WF')." >> tmp.datalog

while read time duration thread localthread endstate tr_name scratch; do
    echo "isProcess('$thread')." >> tmp.datalog
    echo "hasType('$thread', 'execute')." >> tmp.datalog
    echo "hasName('$thread', '$tr_name')." >> tmp.datalog
    echo "hasStartTime('$thread', $time)." >> tmp.datalog
    echo "hasDuration('$thread', $duration)." >> tmp.datalog
    echo "hasFinalState('$thread', '$endstate')." >> tmp.datalog
done < execute.global.event

while read start_time duration globalid id endstate thread site scratch; do
    # cut off the last component of the thread, so that we end up at the
    # parent thread id which should correspond with the execute-level ID
    inv_id="$WFID$(echo $thread | sed 's/-[^-]*$//')"  >> tmp.datalog
    echo "isExecutionAttempt('$globalid')."  >> tmp.datalog
    echo "attemptsToExecute('$globalid', '$inv_id')."  >> tmp.datalog
    echo "hasStartTime('$globalid', $start_time)."  >> tmp.datalog
    echo "hasDuration('$globalid', $duration)."  >> tmp.datalog
    echo "hasFinalState('$globalid', '$endstate')."  >> tmp.datalog 
    echo "executedAtSite('$globalid', '$site')." >> tmp.datalog
done < execute2.global.event

while read col1 col2 col3 col4 col5 thread name lhs rhs result; do
    thread=$(echo $thread | awk 'BEGIN { FS = "=" }; {print $2}')
    name=$(echo $name | awk 'BEGIN { FS = "=" }; {print $2}')
    lhs=$(echo $lhs | awk 'BEGIN { FS = "=" }; {print $2}')
    rhs=$(echo $rhs | awk 'BEGIN { FS = "=" }; {print $2}')
    result=$(echo $result | awk 'BEGIN { FS = "=" }; {print $2}')
    
    operatorid="${WFID}operator:$thread"
    
    if [ $version -le 3726 ]; then
	lhs=$(echo $lhs | sed -e 's/tag:benc@ci.uchicago.edu,2008:swift://g')
	rhs=$(echo $rhs | sed -e 's/tag:benc@ci.uchicago.edu,2008:swift://g')
	result=$(echo $result | sed -e 's/tag:benc@ci.uchicago.edu,2008:swift://g')
    fi
    
    echo "isDataset('$lhs')." >> tmp.datalog
    echo "isDataset('$rhs')." >> tmp.datalog
    echo "isDataset('$result')." >> tmp.datalog
    echo "isProcess('$operatorid')." >> tmp.datalog
    echo "hasType('$operatorid', 'operator')." >> tmp.datalog
    echo "hasName('$operatorid', '$name')." >> tmp.datalog
    echo "used('$operatorid', '$lhs', 'lhs')." >> tmp.datalog
    echo "used('$operatorid', '$rhs', 'rhs')." >> tmp.datalog
    echo "wasGeneratedBy('$result', '$operatorid', 'result')." >> tmp.datalog
done < operators.txt

while read id name output; do
    echo "isDataset('$output')." >> tmp.datalog
    echo "isProcess('$id')." >> tmp.datalog
    echo "hasType('$id', 'function')." >> tmp.datalog
    echo "hasName('$id', '$name')." >> tmp.datalog
    echo "isInScriptRun('$id', '$WF')." >> tmp.datalog
    echo "wasGeneratedBy('$output', '$id', 'result')." >> tmp.datalog
done < functions.txt

while read id value; do
    # TODO need ordering/naming
    echo "isDataset('$value')." >> tmp.datalog
    echo "wasGeneratedBy('$value', '$id', 'undefined')." >> tmp.datalog
done < function-inputs.txt


while read thread appname; do
    echo "hasName('$thread', '$appname')." >> tmp.datalog
done < invocation-procedure-names.txt

while read outer inner; do
    echo "isDataset('$outer')." >> tmp.datalog
    echo "isDataset('$inner')." >> tmp.datalog
    echo "isContainedIn('$inner', '$outer')." >> tmp.datalog
done < tie-containers.txt

while read dataset filename; do

    if [ $version -le 3726 ]; then
	dataset=$(echo $dataset | sed -e 's/tag:benc@ci.uchicago.edu,2008:swift://g')
    fi
    echo "isDataset('$dataset')."  >> tmp.datalog
    echo "hasFileName('$dataset', '$filename')." >> tmp.datalog
done < dataset-filenames.txt

while read dataset idtype equal value rest; do

    if [ $version -le 3726 ]; then
	dataset=$(echo $dataset | sed -e 's/tag:benc@ci.uchicago.edu,2008:swift://g')
    fi

    echo "isDataset('$dataset')."  >> tmp.datalog
    echo "hasValue('$dataset', '$value')." >> tmp.datalog
done < dataset-values.txt

while read start duration wfid rest; do
    echo "hasStartTime('$WF', $start)."  >> tmp.datalog
    echo "hasDuration('$WF', $duration)." >> tmp.datalog
done < workflow.event


# TODO this could merge with other naming tables
while read start duration thread final_state procname ; do
    if [ "$duration" != "last-event-line" ]; then
	compoundid=$WFID$thread
	echo "isProcess('$compoundid')." >> tmp.datalog
	echo "hasType('compound')." >> tmp.datalog
	echo "hasName('$procname')."  >> tmp.datalog
	echo "isInScriptRun('$compoundid', '$WF')." >> tmp.datalog
    fi
done < compound.event

while read start duration thread final_state procname ; do
    if [ "$duration" != "last-event-line" ]; then
	fqid=$WFID$thread
	echo "isProcess('$fqid')." >> tmp.datalog
	echo "hasType('internal')." >> tmp.datalog
	echo "hasName('$procname')."  >> tmp.datalog
	echo "isInScriptRun('$fqid', '$WF')." >> tmp.datalog
    fi	
done < internalproc.event

while read t ; do 
    thread="${WFID}$t"
    echo "isProcess('$thread')." >> tmp.datalog
    echo "hasType('scope')." >> tmp.datalog
    echo "hasName('scope')."  >> tmp.datalog
    echo "isInScriptRun('$thread', '$WF')." >> tmp.datalog    
done < scopes.txt

while read thread direction dataset variable rest; do 

    dataset=$(echo $dataset | sed -e 's/tag:benc@ci.uchicago.edu,2008:swift://g')
    echo "isDataset('$dataset')." >> tmp.datalog 

    if [ "$direction" == "input" ] ; then
	echo "used('$thread', '$dataset', '$variable')." >> tmp.datalog
    else
	echo "wasGeneratedBy('$dataset', '$thread', '$variable')." >> tmp.datalog
    fi
    
done < tie-data-invocs.txt

if [ -f extrainfo.txt ]; then
    while read execute2_id extrainfo; do
	echo $extrainfo | awk -F ";"  '{ for (i = 1; i <= NF; i++)
                                               print $i
                                         }' | awk -F "=" '{ print $1 " " $2 }' | awk -F ":" '{ print $1 " " $2 }' > fields.txt
	id=$($SQLCMD --tuples-only -c "select app_inv_id from app_exec where id='$execute2_id';" | awk '{print $1}')
	while read name type value; do
	    # TODO: check types
	    echo "hasAnnotation('$id', '$name', '$value')." >> tmp.datalog
	done < fields.txt 
    done < extrainfo.txt
fi

if [ -f runtime.txt ]; then
    while read execute2_id runtime; do
	timestamp=$(echo $runtime | awk -F "," '{print $1}' | awk -F ":" '{print $2}')
	cpu_usage=$(echo $runtime | awk -F "," '{print $2}' | awk -F ":" '{print $2}')
	max_phys_mem=$(echo $runtime | awk -F "," '{print $3}' | awk -F ":" '{print $2}')
	max_virtual_mem=$(echo $runtime | awk -F "," '{print $4}' | awk -F ":" '{print $2}')
	io_read_bytes=$(echo $runtime | awk -F "," '{print $5}' | awk -F ":" '{print $2}')
	io_write_bytes=$(echo $runtime | awk -F "," '{print $6}' | awk -F ":" '{print $2}')
	echo "hasRuntimeInfo('$execute2_id', $timestamp, $cpu_usage, $max_phys_mem, $max_virtual_mem, $io_read_bytes, $io_write_bytes)." >> tmp.datalog
    done < runtime.txt
fi

cat tmp.datalog | sort | uniq >> provenancedb.datalog

echo Finished writing Datalog.