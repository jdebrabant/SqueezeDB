#! /bin/sh
psql -d vcdim20101113large -c "ALTER TABLE A_k5_u200k_unif ALTER T_1 SET STATISTICS -1;"
psql -d vcdim20101113large -c "ALTER TABLE A_k5_u200k_unif ALTER T_2 SET STATISTICS -1;"
psql -d vcdim20101113large -c "ALTER TABLE A_k5_u200k_unif ALTER T_3 SET STATISTICS -1;"
psql -d vcdim20101113large -c "ALTER TABLE A_k5_u200k_unif ALTER T_4 SET STATISTICS -1;"
psql -d vcdim20101113large -c "ALTER TABLE A_k5_u200k_unif ALTER T_5 SET STATISTICS -1;"
psql -d vcdim20101113large -c "VACUUM ANALYZE A_k5_u200k_unif;"
psql -d vcdim20101113large -c "ALTER TABLE B_k5_u200k_unif ALTER T_1 SET STATISTICS -1;"
psql -d vcdim20101113large -c "ALTER TABLE B_k5_u200k_unif ALTER T_2 SET STATISTICS -1;"
psql -d vcdim20101113large -c "ALTER TABLE B_k5_u200k_unif ALTER T_3 SET STATISTICS -1;"
psql -d vcdim20101113large -c "ALTER TABLE B_k5_u200k_unif ALTER T_4 SET STATISTICS -1;"
psql -d vcdim20101113large -c "ALTER TABLE B_k5_u200k_unif ALTER T_5 SET STATISTICS -1;"
psql -d vcdim20101113large -c "VACUUM ANALYZE B_k5_u200k_unif;"

bash runExpJoinQueries.sh vcdim20101113large A_k5_u200k_unif B_k5_u200k_unif ../data/20101113/queries/join/k1b1/orig/ > ../data/20101113/results/join/hist0/k1b1.txt
bash runExpJoinQueries.sh vcdim20101113large A_k5_u200k_unif B_k5_u200k_unif ../data/20101113/queries/join/k1b2/orig/ > ../data/20101113/results/join/hist0/k1b2.txt
bash runExpJoinQueries.sh vcdim20101113large A_k5_u200k_unif B_k5_u200k_unif ../data/20101113/queries/join/k1b3/orig/ > ../data/20101113/results/join/hist0/k1b3.txt
bash runExpJoinQueries.sh vcdim20101113large A_k5_u200k_unif B_k5_u200k_unif ../data/20101113/queries/join/k1b5/orig/ > ../data/20101113/results/join/hist0/k1b5.txt
bash runExpJoinQueries.sh vcdim20101113large A_k5_u200k_unif B_k5_u200k_unif ../data/20101113/queries/join/k1b8/orig/ > ../data/20101113/results/join/hist0/k1b8.txt


