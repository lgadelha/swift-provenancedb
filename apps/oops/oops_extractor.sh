#!/bin/bash

# Annotation extractor for the OOPS application
# 

PROVDB_HOME=~/provenancedb
PROTESTS_HOME=/gpfs/pads/oops/aashish/CASP9
IMPORT_HOME=~/protests

cp $PROVDB_HOME/etc/provenance.config.ci $PROVDB_HOME/etc/provenance.config
source $PROVDB_HOME/etc/provenance.config

# provdb_imported records runs already imported to the provenance database
cd $IMPORT_HOME
if [ ! -a provdb_imported ]; then
    touch provdb_imported
fi

cd $PROTESTS_HOME
for k in $(ls -1);
do
    cd $PROTESTS_HOME/$k
    for i in $(ls | grep run.raptorloops; ls | grep run.loops);
    do 
	cd $IMPORT_HOME
	if ! grep --silent $i provdb_imported; then
	    if grep --silent "Swift finished with no errors" $PROTESTS_HOME/$k/$i/*-*-*-*.log; then
	        # swift-prov-import-all-logs also controls what already has been
	        # imported, so it does not repeat work
		echo "export LOGREPO=$PROTESTS_HOME/$k/$i" > $PROVDB_HOME/etc/provenance.config
		echo "export SQLCMD=\"psql -U provdb -h db.ci.uchicago.edu provdb\"" >> $PROVDB_HOME/etc/provenance.config
		$PROVDB_HOME/swift-prov-import-all-logs
		cd $IMPORT_HOME
		echo $i >> provdb_imported
		cd swift-logs
	        
                # annotate workflows with their oops runid
		OOPS_RUN_ID=$(echo $i | awk -F . '{print $3}')
		cd $PROTESTS_HOME/$k/$i
		LOG_FILENAME=$(ls *-*-*-*.log)
 		WORKFLOW_ID=$(echo "select id from run where log_filename like '%$LOG_FILENAME%'" | $SQLCMD -t | awk '{print $1}')
		cd $IMPORT_HOME/swift-logs
		$SQLCMD -c "insert into a_run_t (run_id, name, value) values ('$WORKFLOW_ID','oops_run_id','$OOPS_RUN_ID');" 
		
		#extracts scientific parameters given as input to the workflow in file *.params.
		$SQLCMD -t -A -F " " -c "select file.id,file.name from proc, ds_out, ds_cont, file where  proc.id=ds_out.proc_id and ds_out.ds_id=out_id and file.id=ds_cont.in_id and file.name like '%.params' and proc.name='PrepLoop' and proc.run_id='$WORKFLOW_ID';" -o result.txt
		
		DATASET_ID=$(awk '{if (NR==1) print $1}' result.txt)
		FILENAME=$(awk '{if (NR==1) print $2}' result.txt | sed 's/file:\/\/localhost\///g')
		
		cd $PROTESTS_HOME/$k/$i
		
		while read line; do
		    NAME=$(echo $line | awk 'BEGIN { FS = "=" }; {print $1}')
		    RIGHT=$(echo $line | awk 'BEGIN { FS = "=" }; {print $2}')
		    if [ "$NAME" = "SAMPLE RANGE" ]; then
			$SQLCMD -c "insert into a_ds_t values ('$DATASET_ID', 'sample_range', '$RIGHT');"
		    fi 
		    if [ "$NAME" = "RESTRAIN DISTANCE" ]; then
			VALUE1=$(echo $RIGHT | awk 'BEGIN { FS = "," }; {print $1}')
			VALUE2=$(echo $line | awk 'BEGIN { FS = "=" }; {print $2}' | awk 'BEGIN { FS = "," }; {print $2}')
			$SQLCMD -c "insert into a_ds_n values ('$DATASET_ID', 'restrain_distance_1', $VALUE1);"
			$SQLCMD -c "insert into a_ds_n values ('$DATASET_ID', 'restrain_distance_2', $VALUE2);"
		    fi 
		    if [ "$NAME" = "MAXIMUM NUMBER OF STEPS" ]; then
			$SQLCMD -c "insert into a_ds_n values ('$DATASET_ID', 'maximum_number_of_steps', $RIGHT);"
		    fi 
		done < $FILENAME

		#extracts length of the fasta sequence given as input to the workflow in file *.fasta.
		cd $IMPORT_HOME/swift-logs

		$SQLCMD -t -A -F " " -c "select file.id,file.name from proc, ds_out, ds_cont, file where  proc.id=ds_out.proc_id and ds_out.ds_id=out_id and file.id=ds_cont.in_id and file.name like '%.fasta' and proc.name='PrepLoop' and proc.run_id='$WORKFLOW_ID';" -o result.txt
		
		DATASET_ID=$(awk '{if (NR==1) print $1}' result.txt)
		FILENAME=$(awk '{if (NR==1) print $2}' result.txt | sed 's/file:\/\/localhost\///g')
		
		cd $PROTESTS_HOME/$k/$i
		
		if [ -n "$FILENAME" ]; then
		    SEQLENGTH=$(awk '{if (NR==2) print $1}' $FILENAME | wc -c)
		    $SQLCMD -c "insert into a_ds_n values ('$DATASET_ID', 'fasta_sequence_length', $SEQLENGTH);"
		fi

		# extracts scientific parameters given as output by the workflow in *.log.
		# relevant lines:
		# zone2            (Initial Energy: -21352.116911)
		# Total Function Evaluations: 20000
		# Accepted transitions: 7410
		# Increasing transitions: 4525
		# Decreasing transitions: 2885
		# Rejected transitions: 12590
		# Final Energy: -27152.264775
		# Final Temp: 79.778142
		# Total Running Time: 18006

		cd $IMPORT_HOME/swift-logs
	    
		$SQLCMD -t -A -F " " -c "select file.id,file.name from proc, ds_out, ds_cont, file where  proc.id=ds_out.proc_id and ds_out.ds_id=out_id and file.id=ds_cont.in_id and file.name like '%.log' and proc.name='LoopModel' and proc.run_id='$WORKFLOW_ID';" -o result.txt

		cd $PROTESTS_HOME/$k/$i
		
		while read dataset filename; do
		    FILENAME=$(echo $filename | sed 's/file:\/\/localhost\///g')
		    while read token1 token2 token3 token4; do
			if [ "$token2" = "(Initial Energy:" ]; then
			    initialenergy=$(echo $token3 | awk 'BEGIN { FS = "\)" }; {print $1}')
			    $SQLCMD -c "insert into a_ds_n (ds_id, name, value) values ('$dataset', 'initial_energy', $initialenergy);"
			fi
			if [ "$token1" = "Total" ] && [ "$token2" = "Function" ] && [ "$token3" = "Evaluations:" ]; then
			    $SQLCMD -c "insert into a_ds_n (ds_id, name, value) values ('$dataset', 'total_function_evaluations', $token4);"
			fi 
			if [ "$token1" = "Increasing" ] && [ "$token2" = "transitions:" ]; then
			    $SQLCMD -c "insert into a_ds_n (ds_id, name, value) values ('$dataset', 'accepted_increasing_transitions', $token3);"
			fi 
			if [ "$token1" = "Decreasing" ] && [ "$token2" = "transitions:" ]; then
			    $SQLCMD -c "insert into a_ds_n (ds_id, name, value) values ('$dataset', 'accepted_decreasing_transitions', $token3);"
			fi 
			if [ "$token1" = "Rejected" ] && [ "$token2" = "transitions:" ]; then
			    $SQLCMD -c "insert into a_ds_n (ds_id, name, value) values ('$dataset', 'rejected_transitions', $token3);"
			fi 
			if [ "$token1" = "Final" ] && [ "$token2" = "Energy:" ]; then
			    $SQLCMD -c "insert into a_ds_n (ds_id, name, value) values ('$dataset', 'final_energy', $token3);"
			fi 
			if [ "$token1" = "Final" ] && [ "$token2" = "Temp:" ]; then
			    $SQLCMD -c "insert into a_ds_n (ds_id, name, value) values ('$dataset', 'final_temp', $token3);"
			fi 
			if [ "$token1" = "Total" ] && [ "$token2" = "Running" ] && [ "$token3" = "Time:" ]; then
			    $SQLCMD -c "insert into a_ds_n (ds_id, name, value) values ('$dataset', 'total_running_time', $token4);"
			fi 
		    done < $FILENAME
		done < $IMPORT_HOME/swift-logs/result.txt
	    fi
	fi 
    done
done
