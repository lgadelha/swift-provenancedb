#!/bin/bash

echo "<cosmic>"

psql82 -At -U postgres -d i2u2vdc1 -c "select distinct name from anno_lfn" > tmp-lfns

while read lfn; do
 echo "<lfn name=\"$lfn\">"

psql82 -At -F '¿' -U postgres -d i2u2vdc1 -c "select mkey, value from anno_lfn, anno_text where name='$lfn' and anno_lfn.id = anno_text.id;" > tmp-lfns3


cat tmp-lfns3 | sed 's/&/\&amp;/g' | sed 's/</\&lt;/g' | sed 's%^\([^ ]*\)¿\(.*\)$%<\1>\2</\1>%'

#while read key value ; do
# echo "<$key>$value</$key>"
#done < tmp-lfns3


#  while read key id ; do
#    echo -n "<$key vds1id=\"$id\">"
#echo -n $(psql82 -At -F ' ' -U postgres -d i2u2vdc1 -c "select value from anno_text where id=$id")
# this echo strims off the newline that was appearing
#    echo "</$key>"
#  done <  tmp-lfns2

 echo "</lfn>"
done < tmp-lfns


echo "</cosmic>"
