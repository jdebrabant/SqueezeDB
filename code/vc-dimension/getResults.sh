#!/bin/sh

usage()
{
  echo Usage: $0 start stop >&2
}

if [ $# -lt 2 ]; then
  usage
  exit 1
fi

start=$1
stop=$2

if [ $start -gt $stop ]; then
  echo start must be greater than stop
  usage
  exit 1
fi

psql -d csvlog -c "SELECT QID,OPNAME,OPINFO,P_ROWS,ACT_ROWS FROM QUERY_PLAN_T WHERE QID >= $start AND QID <= $stop AND (OPNAME LIKE '%Join' OR OPNAME LIKE '%Scan') ORDER BY QID;" | tail -n+3 | head -n-2 || exit 1

