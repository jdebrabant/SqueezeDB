#! /bin/bash

usage()
{
  echo "Usage: $0 database table file" 1>&2
}

if [ $# -lt 3 ]; then
  usage
  exit 1
fi

db=$1
table=$2
filename=`realpath -s $3`

if [ ! -r $filename ]; then
  echo "$3: file not found" 1>&2
  usage
  exit 1
fi

columns=`head -n 1 $filename | grep -o "|" | wc -l`

prefix="T_"
suffix=" INTEGER NOT NULL, "
schema=""
i=1
while [ $i -le $columns ]; do
  schema="$schema$prefix$i $suffix "
  i=`expr $i + 1`
done

len=`expr ${#schema} - 3`
schema=${schema:0:$len}

#echo $schema
psql -d $db -c "CREATE TABLE $table(sampleindex$suffix$schema);"
psql -d $db -c "COPY $table FROM '$filename' WITH DELIMITER AS '|';"

