#!/bin/sh
usage()
{
  echo $0 db 1>&2
}

if [ $# -lt 1 ]; then
  usage
  exit 1
fi

db=$1

psql -d $db -f /home/lview/dbs/postgresql-8.4.1/contrib/pg_buffercache/pg_buffercache.sql

