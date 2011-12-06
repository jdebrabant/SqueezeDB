#!/bin/sh
python3 parseRes.py -j -e 0.05 -l 20000000 -s 1400 ../data/20101113/results/join/k1b8/large.txt ../data/20101113/results/join/k1b8/k1_b1.txt
python3 parseRes.py -j -e 0.05 -l 20000000 -s 3800 ../data/20101113/results/join/k1b8/large.txt ../data/20101113/results/join/k1b8/k1_b2.txt
python3 parseRes.py -j -e 0.05 -l 20000000 -s 7800 ../data/20101113/results/join/k1b8/large.txt ../data/20101113/results/join/k1b8/k1_b3.txt
python3 parseRes.py -j -e 0.05 -l 20000000 -s 20600 ../data/20101113/results/join/k1b8/large.txt ../data/20101113/results/join/k1b8/k1_b5.txt
python3 parseRes.py -j -e 0.05 -l 20000000 -s 51800 ../data/20101113/results/join/k1b8/large.txt ../data/20101113/results/join/k1b8/k1_b8.txt
python3 parseRes.py -j -e 0.05 -l 20000000 -s 192800 ../data/20101113/results/join/k1b8/large.txt ../data/20101113/results/join/k1b8/k2_b2.txt
python3 parseRes.py -j -e 0.05 -l 20000000 -s 2738400 ../data/20101113/results/join/k1b8/large.txt ../data/20101113/results/join/k1b8/k2_b5.txt
