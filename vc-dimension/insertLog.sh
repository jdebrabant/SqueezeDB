#!/bin/sh

usage()
{
  echo Usage: $0 logfile >&2
}

if [ $# -lt 1 ]; then
  usage
  exit 1
fi

log=`realpath $1`

if [ ! -r $log ]; then
  echo Log file must exists and be readable! >&2
  usage
  exit 1
fi

max=`psql -d csvlog -c "SELECT max(qid) FROM QUERY_T;" | tail -3 | head -1`
start=`expr $max + 1`

/home/lview/pgsql-log-parser/pgparser -b -l $log > /dev/null || exit 1 

end=`psql -d csvlog -c "SELECT max(qid) FROM QUERY_T;" | tail -3 | head -1`

echo start: $start end: $end

