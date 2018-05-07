#!/bin/bash

PROVDIR=$(dirname $0)
pushd $PROVDIR
PROVDIR=$(pwd)
popd

# we need to keep this out of the log-proceesing dir because import
# of individual runs will clean other files.

source $PROVDIR/etc/provenance.config
export PATH=$PROVDIR:$PATH

query="select * from script_run order by start_time;"

$SQLCMD -c "$query"