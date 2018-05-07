#!/bin/bash

# populate the trans table with transitive closure of some relations
# that we expect to query over transitively for causal queries.

# 1. populate the base (un-closed) relations

# do this by copying as appropriate from other tables

# 1a. dataset<->invocation direction
# dataset dependency - copy dataset dependency graph in
#  (the tie-containers one)

# 1b. execution dependency - tie-data-invocs

PROVDIR=$(dirname $0)
pushd $PROVDIR
PROVDIR=$(pwd)
popd

source $PROVDIR/etc/provenance.config

$SQLCMD < $PROVDIR/prov-sql-generate-transitive-closures.sql 

# 2. iteratively extend paths until there are no more to add.

NEW=0

while [ "$LAST" != "$NEW" ]; do
  LAST=$NEW
  NEW=$($SQLCMD < $PROVDIR/prov-sql-iterate-transitive-closures.sql)
  echo Previous: $LAST   Now: $NEW
done
