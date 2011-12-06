from sys import argv, exit, stderr, stdout
import pyodbc
import os
import os.path
import string

def main():
    baseDir = "queries\\correl"
    queryDirs = [ "k2b2", "k2b5", "k2b8" ]
    table = "T_k2_correl"
    baseDirRes = "results"
    outFileExtension = ".txt"

    cnxn = pyodbc.connect('DRIVER={SQL Server Native Client 10.0};SERVER=localhost;DATABASE=matteo;Trusted_Connection=yes')
    cursor = cnxn.cursor()
    cursor.execute("SET SHOWPLAN_ALL ON;")

    for queryDir in queryDirs:
        queryDirFullname = baseDir + "\\" + queryDir
        outFilename = baseDirRes + "\\" + table + "\\" + queryDir + outFileExtension

        with open(outFilename, 'wt') as out:
            pass

        for queryFilename in os.listdir(queryDirFullname):
            with open(queryDirFullname + "\\" + queryFilename, 'rt') as queryFILE:
                query = queryFILE.readline()
            query = string.replace(query, "%%TABLE%%", table)

            cursor.execute(query)

            row = cursor.fetchone()
            toPrint = os.path.basename(queryFilename) + "," +  str(row[0]) + "," + str(row[8]) + "\n"
            with open(outFilename, 'at') as outFILE:
                outFILE.write(toPrint)

if __name__ == "__main__":
    main()
