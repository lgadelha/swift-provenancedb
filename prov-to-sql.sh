#!/bin/bash

source_file=$(grep "source file" $1 | awk '{print $5}' | awk -F ":" '{print $1}')	
export RUNID=$2
export source_file_prefix=$3
export CKSUM=$4

export WFID="${RUNID}:"

# TODO is there already a URI form for identifying workflows?
export WF="${RUNID}"

echo "Generating SQL for $RUNID:"

# this gives a distinction between the root process for a workflow and the
# workflow itself. perhaps better to model the workflow as a process
echo "    - Root thread."
echo "INSERT INTO fun_call (id, type, name, run_id) VALUES ('${WFID}R', 'rootthread', '$RUNID', '$WF');" >> /tmp/$RUNID.sql

echo "    - Function calls."
while read time duration thread localthread endstate tr_name scratch; do
    id="$(echo "$thread" | sed "s/execute\://")"
    fid=${source_file_prefix}-$(echo $id | sed "s/run.../&-$CKSUM/g")
    echo "INSERT INTO fun_call (id, type, run_id) VALUES ('$fid', 'execute', '$WF');"  >> /tmp/$RUNID.sql
    echo "INSERT INTO app_fun_call (id, name, start_time, duration, final_state, scratch) VALUES ('$fid', '$tr_name', $time, $duration, '$endstate', '$scratch');"   >> /tmp/$RUNID.sql
done < execute.global.event

echo "    - Application executions."
while read start_time duration globalid id endstate thread site scratch; do
    # cut off the last component of the thread, so that we end up at the
    # parent thread id which should correspond with the execute-level ID
    #inv_id="$WFID$(echo $thread | sed 's/-[^-]*$//')"
    inv_id="$WFID$(echo $thread)"
    aux_eid=$(echo "$globalid" | sed "s/execute2\://")   
    eid=${source_file_prefix}-$(echo $aux_eid | sed "s/run.../&-$CKSUM/g")
    echo  "INSERT INTO app_exec (id, app_fun_call_id, start_time, duration, final_state, site) VALUES ('$eid', '$inv_id', $start_time, $duration, '$endstate', '$site');"  >> /tmp/$RUNID.sql
done < execute2.global.event

echo "    - Mapped variables."
while read dataset filename; do
    echo "INSERT INTO ds (id) VALUES ('$dataset');"  >> /tmp/$RUNID.sql
    echo "INSERT INTO mapped (id, filename) VALUES ('$dataset', '$filename');"  >> /tmp/$RUNID.sql
done < dataset-filenames.txt

echo "    - Primitive variables."
while read dataset idtype equal value rest; do
    echo "INSERT INTO ds (id) VALUES ('$dataset');"  >> /tmp/$RUNID.sql
    echo "INSERT INTO primitive (id, value) VALUES ('$dataset', '$value');"  >> /tmp/$RUNID.sql
done < dataset-values.txt

echo "    - Arrays and structures."
while read outer inner; do
    echo  "INSERT INTO ds (id) VALUES ('$outer');"  >> /tmp/$RUNID.sql
    echo  "INSERT INTO ds (id) VALUES ('$inner');"  >> /tmp/$RUNID.sql
    echo  "INSERT INTO dataset_containment (out_id, in_id) VALUES ('$outer', '$inner');"  >> /tmp/$RUNID.sql
    cid=$(echo $outer | awk -F "-" '{print $3}')
    echo  "INSERT INTO fun_call (id, type, name, run_id) VALUES ('${WFID}$cid', 'constructor', 'constructor', '$WF');"  >> /tmp/$RUNID.sql
    echo  "INSERT INTO dataset_in (function_call_id, dataset_id, parameter) VALUES ('${WFID}$cid', '$inner', 'element');"  >> /tmp/$RUNID.sql
    echo  "INSERT INTO dataset_out (function_call_id, dataset_id, parameter) VALUES ('${WFID}$cid', '$outer', 'collection');"  >> /tmp/$RUNID.sql
done < tie-containers.txt

echo "    - Operator calls."
while read col1 col2 col3 col4 col5 thread name lhs rhs result; do
    thread=$(echo $thread | awk 'BEGIN { FS = "=" }; {print $2}')
    name=$(echo $name | awk 'BEGIN { FS = "=" }; {print $2}')
    lhs=$(echo $lhs | awk 'BEGIN { FS = "=" }; {print $2}')
    rhs=$(echo $rhs | awk 'BEGIN { FS = "=" }; {print $2}')
    result=$(echo $result | awk 'BEGIN { FS = "=" }; {print $2}')
    
    operatorid="${WFID}operator:$thread"
    
    echo  "INSERT INTO ds (id) VALUES ('$lhs');" >> /tmp/$RUNID.sql
    echo  "INSERT INTO ds (id) VALUES ('$rhs');" >> /tmp/$RUNID.sql
    echo  "INSERT INTO ds (id) VALUES ('$result');" >> /tmp/$RUNID.sql
    echo  "INSERT INTO fun_call (id, type, name, run_id) VALUES ('$operatorid', 'operator', '$name', '$WF');"  >> /tmp/$RUNID.sql
    echo  "INSERT INTO dataset_in (function_call_id, dataset_id, parameter) VALUES ('$operatorid', '$lhs', 'lhs');"  >> /tmp/$RUNID.sql
    echo  "INSERT INTO dataset_in (function_call_id, dataset_id, parameter) VALUES ('$operatorid', '$rhs', 'rhs');"  >> /tmp/$RUNID.sql
    echo  "INSERT INTO dataset_out (function_call_id, dataset_id, parameter) VALUES ('$operatorid', '$result', 'result');"  >> /tmp/$RUNID.sql
done < operators.txt

echo "    - Built-in function calls."
while read id name output; do
    echo  "INSERT INTO ds (id) VALUES ('$output');"  >> /tmp/$RUNID.sql
    echo  "INSERT INTO fun_call (id, type, name, run_id) VALUES ('$id', 'function', '$name', '$WF');"  >> /tmp/$RUNID.sql
    echo  "INSERT INTO dataset_out (function_call_id, dataset_id, parameter) VALUES ('$id', '$output', 'result');"  >> /tmp/$RUNID.sql
done < functions.txt

while read id value; do
    echo  "INSERT INTO ds (id) VALUES ('$value');" >> /tmp/$RUNID.sql
    echo  "INSERT INTO dataset_in (function_call_id, dataset_id, parameter) VALUES ('$id', '$value', 'undefined');"  >> /tmp/$RUNID.sql
done < function-inputs.txt


echo "    - Script run events."
while read start duration wfid rest; do
    echo "UPDATE run SET start_time=$start WHERE id='$WF';"  >> /tmp/$RUNID.sql
    echo "UPDATE run SET duration=$duration WHERE id='$WF';"  >> /tmp/$RUNID.sql
done < workflow.event


# TODO this could merge with other naming tables
echo "    - Compound functions."
while read start duration thread final_state procname ; do
    if [ "$duration" != "last-event-line" ]; then
	compoundid=$WFID$thread
	echo "INSERT INTO fun_call (id, type, name, run_id) VALUES ('$compoundid', 'compound', '$procname', '$WF');"  >> /tmp/$RUNID.sql
    fi
done < compound.event

while read start duration thread final_state procname ; do
    if [ "$duration" != "last-event-line" ]; then
	fqid=$WFID$thread
	echo "INSERT INTO fun_call (id, type, name, run_id) VALUES ('$fqid', 'internal', '$procname', '$WF');"  >> /tmp/$RUNID.sql
    fi	
done < internalproc.event

while read t ; do 
    thread="${WFID}$t"
    echo "INSERT INTO fun_call (id, type, name, run_id) VALUES ('$thread', 'scope', 'scope', '$WF');"  >> /tmp/$RUNID.sql
done < scopes.txt

echo "    - Dataset consumption and production."
while read thread direction dataset variable rest; do 
    if [ "$direction" == "input" ] ; then
	table=dataset_in
    else
	table=dataset_out
    fi
    
    echo "INSERT INTO ds (id) VALUES ('$dataset');"  >> /tmp/$RUNID.sql
    echo "INSERT INTO $table (function_call_id, dataset_id, parameter) VALUES ('$thread', '$dataset', '$variable');"  >> /tmp/$RUNID.sql
done < tie-data-invocs.txt

#echo "    - Dataset stage-ins and stage-outs."
#while read filename execute; do
#    thread=$(grep $execute execute2.event | cut -f 5 -d " ")
#    dataset_id=$(grep "$thread " tie-data-invocs.txt | grep output | cut -f 3 -d " ")
#    echo "INSERT INTO mapped values ('$dataset_id', '$filename');"  >> /tmp/$RUNID.sql
#done < stage-outs.txt


echo "    - Wrapper log resource consumption info."
if [ -f runtime.txt ]; then
    while read execute2_id runtime; do
	real_secs=$(echo $runtime | awk -F "," '{print $1}' | awk -F ":" '{print $2}')
	kernel_secs=$(echo $runtime | awk -F "," '{print $2}' | awk -F ":" '{print $2}')
	user_secs=$(echo $runtime | awk -F "," '{print $3}' | awk -F ":" '{print $2}')
	percent_cpu=$(echo $runtime | awk -F "," '{print $4}' | awk -F ":" '{print $2}' | awk -F "%" '{print $1}')
	max_rss=$(echo $runtime | awk -F "," '{print $5}' | awk -F ":" '{print $2}')
	avg_rss=$(echo $runtime | awk -F "," '{print $6}' | awk -F ":" '{print $2}')
	avg_tot_vm=$(echo $runtime | awk -F "," '{print $7}' | awk -F ":" '{print $2}')
	avg_priv_data=$(echo $runtime | awk -F "," '{print $8}' | awk -F ":" '{print $2}')
	avg_priv_stack=$(echo $runtime | awk -F "," '{print $9}' | awk -F ":" '{print $2}')
	avg_shared_text=$(echo $runtime | awk -F "," '{print $10}' | awk -F ":" '{print $2}')
	page_size=$(echo $runtime | awk -F "," '{print $11}' | awk -F ":" '{print $2}')
	major_pgfaults=$(echo $runtime | awk -F "," '{print $12}' | awk -F ":" '{print $2}')
	minor_pgfaults=$(echo $runtime | awk -F "," '{print $13}' | awk -F ":" '{print $2}')
	swaps=$(echo $runtime | awk -F "," '{print $14}' | awk -F ":" '{print $2}')
	invol_context_switches=$(echo $runtime | awk -F "," '{print $15}' | awk -F ":" '{print $2}')
	vol_waits=$(echo $runtime | awk -F "," '{print $16}' | awk -F ":" '{print $2}')
	fs_reads=$(echo $runtime | awk -F "," '{print $17}' | awk -F ":" '{print $2}')
	fs_writes=$(echo $runtime | awk -F "," '{print $18}' | awk -F ":" '{print $2}')
	sock_recv=$(echo $runtime | awk -F "," '{print $19}' | awk -F ":" '{print $2}')
	sock_send=$(echo $runtime | awk -F "," '{print $20}' | awk -F ":" '{print $2}')
	signals=$(echo $runtime | awk -F "," '{print $21}' | awk -F ":" '{print $2}')
	exit_status=$(echo $runtime | awk -F "," '{print $22}' | awk -F ":" '{print $2}')
	
	echo "UPDATE app_exec SET real_secs='$real_secs', kernel_secs='$kernel_secs', user_secs='$user_secs', percent_cpu='$percent_cpu', max_rss='$max_rss', avg_rss='$avg_rss', avg_tot_vm='$avg_tot_vm', avg_priv_data='$avg_priv_data', avg_priv_stack='$avg_priv_stack', avg_shared_text='$avg_shared_text', page_size='$page_size', major_pgfaults='$major_pgfaults', minor_pgfaults='$minor_pgfaults', swaps='$swaps', invol_context_switches='$invol_context_switches', vol_waits='$vol_waits', fs_reads='$fs_reads', fs_writes='$fs_writes', sock_recv='$sock_recv', sock_send='$sock_send', signals='$signals', exit_status='$exit_status' WHERE id='$execute2_id';" >> /tmp/$RUNID.sql
	#echo "INSERT INTO rt_info (app_exec_id, timestamp, cpu_usage, max_phys_mem, max_virt_mem, io_read, io_write) VALUES ('$execute2_id', $timestamp, $cpu_usage, $max_phys_mem, $max_virtual_mem, $io_read_bytes, $io_write_bytes);"  >> /tmp/$RUNID.sql

#	for key in $(echo maxrss walltime systime usertime cpu fsin fsout timesswapped socketrecv socketsent majorpagefaults minorpagefaults contextswitchesinv contextswitchesvol); do
#	    value=$(echo $runtime | awk -F "," '{print $1}' | awk -F ":" '{print $2}')
#	    echo "INSERT INTO annot_app_exec_num VALUES ('$execute2_id','$key',$value)"  >>  /tmp/$RUNID.sql
#	done
    done < runtime.txt
fi

echo "    - Function call names."
while read thread appname; do
    fid=$(echo $thread)
    echo  "UPDATE fun_call SET name='$appname' WHERE id='$fid';"  >> /tmp/$RUNID.sql
done < invocation-procedure-names.txt

echo "    - Wrapper log extra info."
if [ -f extrainfo.txt ]; then
    while read execute2_id extrainfo; do
	echo $extrainfo | awk -F ";"  '{ for (i = 1; i <= NF; i++)
                                               print $i
                                         }' | awk -F "=" '{ print $1 " " $2 }' | awk -F ":" '{ print $1 " " $2 }' > fields.txt
	id=$($SQLCMD --tuples-only -c "select app_fun_call_id from app_exec where id='$execute2_id';" | awk '{print $1}')
	while read name type value; do
	    if [ "$type" = "num" ]; then
		echo "INSERT INTO annot_app_exec_num (id, name, value) VALUES ('$id', '$name', $value);"  >> /tmp/$RUNID.sql
	    fi 
	    if [ "$type" = "txt" ]; then
		echo "INSERT INTO annot_app_exec_text (id, name, value) VALUES ('$id', '$name', '$value');"  >> /tmp/$RUNID.sql
	    fi
	done < fields.txt
    done < extrainfo.txt
fi

echo "    - Stage ins and stage outs."
while read execute2_id stage_in; do
    app_exec_id=$WFID$execute2_id
    for isid in $(echo $stage_in); do 
	filename=$(echo $isid | sed "s/^\[//" | sed "s/\,$//" | sed "s/\]$//" | sed "s/\/\.\//\//g"); 
	echo "INSERT INTO stage_in VALUES ('$app_exec_id', '$filename');"  >> /tmp/$RUNID.sql
    done; 
done < stage-ins.txt 

while read stage_out desthost execute2_id; do
    app_exec_id=$WFID$execute2_id
    filename="file://"$desthost"/"$(echo $stage_out | sed "s/\/\.\//\//g"); 
    echo "INSERT INTO stage_out VALUES ('$app_exec_id', '$filename');"  >> /tmp/$RUNID.sql
done < stage-outs.txt 

echo "    - Prospective provenance (script, tc, sites)."
script_hash=$(openssl dgst -sha1  script.txt | awk  '{ print $2 }')
EXISTING=$($SQLCMD --tuples-only -c "select count(*) from script where hash_value='$script_hash';")
if [ "$EXISTING" -eq "0" ];  then
    content=$(cat script.txt | sed "s/'/''/g")
    echo "INSERT INTO script VALUES ('$script_hash', '$content');" >> /tmp/$RUNID.sql
fi
echo "UPDATE run SET script_hash='$script_hash' WHERE id='$WF';" >> /tmp/$RUNID.sql


    
tc_hash=$(openssl dgst -sha1  tc.txt | awk  '{ print $2 }')
EXISTING=$($SQLCMD --tuples-only -c "select count(*) from tc_file where hash_value='$tc_hash';")
if [ "$EXISTING" -eq "0" ];  then
    content=$(cat tc.txt | sed "s/'/''/g")
    echo "INSERT INTO tc_file VALUES ('$tc_hash', '$content');" >> /tmp/$RUNID.sql
fi
echo "UPDATE run SET tc_file_hash='$tc_hash' WHERE id='$WF';" >> /tmp/$RUNID.sql


sites_hash=$(openssl dgst -sha1  sites.txt | awk  '{ print $2 }')
EXISTING=$($SQLCMD --tuples-only -c "select count(*) from sites_file where hash_value='$sites_hash';")
if [ "$EXISTING" -eq "0" ];  then
    content=$(cat sites.txt | sed "s/'/''/g")
    echo "INSERT INTO sites_file VALUES ('$sites_hash', '$content');" >> /tmp/$RUNID.sql
fi
echo "UPDATE run SET sites_file_hash='$sites_hash' WHERE id='$WF';" >> /tmp/$RUNID.sql


echo "Finished SQL generation."
echo "Exporting provenance to database..."
$SQLCMD -f /tmp/$RUNID.sql 1> /dev/null 2> /tmp/$RUNID-provenancedb-error.log
mv /tmp/$RUNID.sql /tmp/$RUNID.sql-$(date +%Y%m%d%H%M%S)
echo "Finished exporting provenance to database."