#! /bin/sh

usage()
{
  echo Usage: $0 db table querydir logdir outfile >&2
}

if [ $# -lt 5 ]; then
  usage
  exit 1
fi

db=$1
table=$2
querydir=$3
logdir=$4
outfile=$5

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
bash ./runSelQueries.sh $db $table $querydir || exit 1
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

