#! /bin/sh
psql -d vcdim20101113large -c "ALTER TABLE T_k2_correl ALTER T_1 SET STATISTICS 10000;"
psql -d vcdim20101113large -c "ALTER TABLE T_k2_correl ALTER T_2 SET STATISTICS 10000;"
psql -d vcdim20101113large -c "VACUUM ANALYZE T_k2_correl;"
psql -d vcdim20101113large -c "ALTER TABLE T_k5_u200k_unif ALTER T_1 SET STATISTICS 10000;"
psql -d vcdim20101113large -c "ALTER TABLE T_k5_u200k_unif ALTER T_2 SET STATISTICS 10000;"
psql -d vcdim20101113large -c "ALTER TABLE T_k5_u200k_unif ALTER T_3 SET STATISTICS 10000;"
psql -d vcdim20101113large -c "ALTER TABLE T_k5_u200k_unif ALTER T_4 SET STATISTICS 10000;"
psql -d vcdim20101113large -c "ALTER TABLE T_k5_u200k_unif ALTER T_5 SET STATISTICS 10000;"
psql -d vcdim20101113large -c "VACUUM ANALYZE T_k5_u200k_unif;"

