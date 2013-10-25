#!/bin/sh
python3 parseRes.py -j -e 0.05 -l 20000000 -s 10000 ../data/20101113/resultsPODS/join/k1b5/large.txt ../data/20101113/resultsPODS/join/k1b5/s10k.txt
python3 parseRes.py -j -e 0.05 -l 20000000 -s 25000 ../data/20101113/resultsPODS/join/k1b5/large.txt ../data/20101113/resultsPODS/join/k1b5/s25k.txt
python3 parseRes.py -j -e 0.05 -l 20000000 -s 50000 ../data/20101113/resultsPODS/join/k1b5/large.txt ../data/20101113/resultsPODS/join/k1b5/s50k.txt
python3 parseRes.py -j -e 0.05 -l 20000000 -s 75000 ../data/20101113/resultsPODS/join/k1b5/large.txt ../data/20101113/resultsPODS/join/k1b5/s75k.txt
python3 parseRes.py -j -e 0.05 -l 20000000 -s 100000 ../data/20101113/resultsPODS/join/k1b5/large.txt ../data/20101113/resultsPODS/join/k1b5/s100k.txt
python3 parseRes.py -j -e 0.05 -l 20000000 -s 270150 ../data/20101113/resultsPODS/join/k1b5/large.txt ../data/20101113/resultsPODS/join/k1b5/k1_b1.txt
python3 parseRes.py -j -e 0.05 -l 20000000 -s 996463 ../data/20101113/resultsPODS/join/k1b5/large.txt ../data/20101113/resultsPODS/join/k1b5/k1_b2.txt
python3 parseRes.py -j -e 0.05 -l 20000000 -s 2206984 ../data/20101113/resultsPODS/join/k1b5/large.txt ../data/20101113/resultsPODS/join/k1b5/k1_b3.txt
python3 parseRes.py -j -e 0.05 -l 20000000 -s 6080654 ../data/20101113/resultsPODS/join/k1b5/large.txt ../data/20101113/resultsPODS/join/k1b5/k1_b5.txt
#python3 parseRes.py -j-e 0.05 -l 20000000 -s 51800 ../data/20101113/resultsPODS/join/k1b5/large.txt ../data/20101113/resultsPODS/join/k1b5/k1_b8.txt
