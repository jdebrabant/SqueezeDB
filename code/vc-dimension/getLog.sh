#!/bin/sh

usage()
{
  echo Usage: $0 destdir >&2
}

if [ $# -lt 1 ]; then
  usage
  exit 1
fi

destdir="${1}"
log=`ls /home/lview/dbs/postgresql-8.4.1/data/pg_log/* | grep csv | tail -1`
echo `basename $log`
cp ${log} ${destdir}

