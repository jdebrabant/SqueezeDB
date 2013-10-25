#! /bin/sh
python3.1 parseResExplJoin.py  -e 0.05 -l 20000000 ../data/20101113/results/join/k1b1/large.txt ../data/20101113/results/join/hist0/k1b1.txt
python3.1 parseResExplJoin.py  -e 0.05 -l 20000000 ../data/20101113/results/join/k1b5/large.txt ../data/20101113/results/join/hist0/k1b5.txt
python3.1 parseResExplJoin.py  -e 0.05 -l 20000000 ../data/20101113/results/join/k1b8/large.txt ../data/20101113/results/join/hist0/k1b8.txt
