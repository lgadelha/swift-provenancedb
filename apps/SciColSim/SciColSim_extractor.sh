#!/bin/bash

# Annotation extractor for the SciColSim application
# 

PROVDB_HOME=~/provenancedb
SCICOLSIM_HOME=~/scicolsimruns
IMPORT_HOME=/tmp

#cp $PROVDB_HOME/etc/provenance.config.ci $PROVDB_HOME/etc/provenance.config
source $PROVDB_HOME/etc/provenance.config

cd $SCICOLSIM_HOME
for k in $(find . -name "annealing-*-*-*.log");
do
    run_dir=$(echo "$k" | awk -F "annealing-" '{print $1}')
    cd $SCICOLSIM_HOME/$run_dir
    full_dir=$(pwd)
    log_suffix=$(echo "$k" | awk -F "/" '{print $3}')
    count=$($SQLCMD --tuples-only -c "select count(*) from script_run where log_filename like '%$log_suffix';" | awk '{print $1}')
    if [ "$count" -eq "0" ]; then
	echo "export LOGREPO=$full_dir" > $PROVDB_HOME/etc/provenance.config
	echo "export SQLCMD=\"$SQLCMD\"" >> $PROVDB_HOME/etc/provenance.config
	cd /tmp
	$PROVDB_HOME/swift-prov-import-all-logs
	SCICOLSIM_RUNID=$(echo "$run_dir" | awk -F "/" '{print $2}')
	SCRIPT_RUN_ID=$($SQLCMD --tuples-only -c "SELECT id FROM script_run WHERE log_filename like '%$log_suffix';" | awk '{print $1}')
	echo "Annotating script run $SCRIPT_RUN_ID with key-value(text) pair ('scicolsim_run_id', '$SCICOLSIM_RUNID')"
	$SQLCMD -c "INSERT INTO a_run_t VALUES ('$SCRIPT_RUN_ID', 'scicolsim_run_id', '$SCICOLSIM_RUNID')" 1> /dev/null
	cd $SCICOLSIM_HOME/$run_dir
	while read key value rest
	do
	    if [ -n "$key" ]; then
		echo "Annotating script run $SCRIPT_RUN_ID with key-value(numeric) pair ('$key', $value)."
		$SQLCMD -c "INSERT INTO a_run_n VALUES ('$SCRIPT_RUN_ID', '$key', $value)" 1> /dev/null
	    fi
	done < paramfile
	
	cd /tmp
	while read t d id rest; do
	    cd $SCICOLSIM_HOME
	    record=$(find "$run_dir" -name ${id}-info | grep -v swiftwork)
	    globalid=$EXECUTE2PREFIX$id
	    if [ -n "$record" -a -f "$record" ] ; then
		outf=$(grep '^OUTF=' $record | awk -F "=" '{print $2}' | awk -F "|" '{print $1}')
		while read keyc value rest
		do
		    key=$(echo $keyc | awk -F ":" '{print $1'})
		    if [ "$key" != "$keyc" -a "$key" != "multi_loss" ]; then
			PROC_ID=$($SQLCMD --tuples-only -c "SELECT app_inv_id FROM app_exec WHERE id='execute2:$SCRIPT_RUN_ID:$globalid';" | awk '{print $1}')
			if [ "$key" == "Operation" ]; then
			    echo "Annotating function call $PROC_ID with key-value(text) pair ('$key', '$value')."
			    $SQLCMD -c "INSERT INTO a_proc_t VALUES ('$PROC_ID', '$key', '$value')" 1> /dev/null
			else
			    echo "Annotating function call $PROC_ID with key-value(numeric) pair ('$key', $value)."
			    $SQLCMD -c "INSERT INTO a_proc_n VALUES ('$PROC_ID', '$key', $value)" 1> /dev/null
			fi
			if [ "$value" == "+-" ]; then
			    echo "Annotating function call $PROC_ID with key-value(numeric) pair ('multi_loss', $key)."
			    $SQLCMD -c "INSERT INTO a_proc_n VALUES ('$PROC_ID', 'multi_loss', $key)" 1> /dev/null
			    echo "Annotating function call $PROC_ID with key-value(numeric) pair ('multi_loss_std_dev', $rest)."
			    $SQLCMD -c "INSERT INTO a_proc_n VALUES ('$PROC_ID', 'multi_loss_std_dev', $rest)" 1> /dev/null			    
			fi
		    fi	
		done < $SCICOLSIM_HOME/$run_dir/$outf
	    else
		echo no wrapper log for $id >&2
	    fi
	done < execute2.event
	
    fi
    cd $SCICOLSIM_HOME
done
