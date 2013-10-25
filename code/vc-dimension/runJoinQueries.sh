#! /bin/bash

usage()
{
  echo "Usage: $0 db tableA tableB querydir " 1>&2
}

if [ $# -lt 4 ]; then
  usage
  exit 1
fi

TEMPL1="%%TABLE1%%"
TEMPL2="%%TABLE2%%"
db=$1
table1=`echo $2 | tr [:lower:] [:upper:]`
table2=`echo $3 | tr [:lower:] [:upper:]`
idir=$4

if [ ! -d $idir -o ! -r $idir ]; then
  echo "Input directory must exist and be readable!" 1>&2
  usage
  exit 1
fi

cd $idir
tmpfile=`mktemp -t runQueries.XXXXXXXX` || exit 1
for file in *; do
  echo $file
  orig=`cat $file`
  query=${orig//$TEMPL1/$table1}
  query=${query//$TEMPL2/$table2}
  echo "${query}" > $tmpfile
  sync
  sudo sysctl vm.drop_caches=3 > /dev/null
  sudo sysctl vm.drop_caches=0 > /dev/null
  psql -d $db -c "SELECT * FROM pg_buffercache_free('{$table1}');" > /dev/null
  psql -d $db -c "SELECT * FROM pg_buffercache_free('{$table2}');" > /dev/null
  psql -d $db -f $tmpfile > /dev/null
done
rm $tmpfile

