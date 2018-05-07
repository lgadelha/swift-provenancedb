#!/bin/bash

rm -f tmp22.dot

echo "digraph G {" >> tmp22.dot

$SQLCMD -separator ' ' provdb 'select * from trans;' | while read a b; do
  echo " \"$a\" -> \"$b\" "
done >> tmp22.dot

echo "}" >> tmp22.dot

dot -Tpng -o tmp22.png tmp22.dot
