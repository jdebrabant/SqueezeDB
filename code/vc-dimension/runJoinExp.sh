#! /bin/sh

usage()
{
  echo Usage: $0 db tableA tableB querydir logdir outfile >&2
}

if [ $# -lt 6 ]; then
  usage
  exit 1
fi

db=$1
table1=$2
table2=$3
querydir=$4
logdir=$5
outfile=$6

if [ ! -d $logdir -o ! -w $logdir ]; then
  echo "Logdir must exist and be writable!" >&2
  usage
  exit 1
fi

touch $outfile || exit 1

echo Restarting DBMS...
sh ./restartDB.sh || exit 1
echo done

log=`ls /home/lview/dbs/postgresql-8.4.1/data/pg_log/* | grep csv | tail -1`
echo Logfile: `basename $log`

echo Running queries...
bash ./runJoinQueries.sh $db $table1 $table2 $querydir || exit 1
echo done!

echo Restarting DBMS...
sh ./restartDB.sh || exit 1
echo done

echo -n Getting log `basename $log`... 
cp ${log} ${logdir} || exit 1
echo done 

echo Inserting log into csvlog...
newlog="`realpath ${logdir}`/`basename $log`"
out=`sh ./insertLog.sh "${newlog}"` || exit 1
start=`echo $out | cut -d " " -f 2`
stop=`echo $out | cut -d " " -f 4`
echo start: $start
echo stop: $stop
echo done

echo -n Getting results...
sh ./getResults.sh $start $stop > $outfile || exit 1
echo "done (Yay!)"

