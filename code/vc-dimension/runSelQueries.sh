#! /bin/bash

usage()
{
  echo "Usage: $0 db table querydir " 1>&2
}

if [ $# -lt 3 ]; then
  usage
  exit 1
fi

TEMPL="%%TABLE%%"
db=$1
table=`echo $2 | tr [:lower:] [:upper:]`
idir=$3

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
  query=${orig//$TEMPL/$table}
  echo "${query}" > $tmpfile
  sync
  sudo sysctl vm.drop_caches=3 > /dev/null
  sudo sysctl vm.drop_caches=0 > /dev/null
  psql -d $db -c "SELECT * FROM pg_buffercache_free('{$table}');" > /dev/null
  psql -d $db -f $tmpfile > /dev/null
done
rm $tmpfile

