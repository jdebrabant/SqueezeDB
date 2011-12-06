from sys import argv, exit, stderr, stdout
import getopt, os, os.path, random

outFileNameBase = "query_"
outFileNameSuff = ".sql"

queryBase = "SELECT * FROM "
queryJoinLargeBase = "SELECT COUNT(*) FROM "
queryJoinSampleBase = "CREATE TEMP TABLE TEMPT(SA, SB) AS SELECT A.SAMPLEINDEX,B.SAMPLEINDEX FROM "
querySelWhere = "table1 WHERE ("
querySelWhereSample = "table1_sample WHERE ("
queryJoinWhere = "%%TABLE1%% A,%%TABLE2%% B WHERE A.T_1 = B.T_1 AND ("
querySuff = ";\n"
queryJoinSelTemp = "SELECT * FROM TEMPT WHERE SA = SB;\n"

origDir = "orig/"
sampleDir = "sample/"

columnBase = "c_"

ops = ["<", "<=", ">=", ">"]
boolops = [" AND ", " OR "] 

def usage():
    stderr.write("Usage: {} [-j] -n numQuery -b booleanClauses -k columns -d domainSize outDir\n")

def genJoinQuery(k, b, d):
    sel1 = genSelQuery(k, b, d, "A.")
    sel2 = genSelQuery(k, b, d, "B.") 
    pred = "(" + sel1 + ") AND ((" + sel2 + ")"
    return pred

def genSelQuery(k, b, d, tprefix=""):
    i = 0
    sel = ""
    boolop = ""

    bs = []
    sum = 0
    for i in range(k-1):
        r = random.randint(1,b-sum-(k-i)+1)
        sum = sum + r
        bs.append(r)
    bs.append(b-sum)
    random.shuffle(bs)

    for i in range(k):
        if i > 0:
            sel = sel + ") AND ("
        column = tprefix + columnBase + str(i+1)
        limits = random.sample(range(d), bs[i])
        limits.sort()

        if bs[i] % 2 == 0:
            incl = random.randint(0,1)
            if incl == 0:
                sel = sel + " ( " + column + " <= " + str(limits[0]) + " )"
                sel = sel + " OR ( " + column + " >= " + str(limits[-1]) + " )"
                limits = limits[1:-1]
            else:
                sel = sel + " ( " + column + " >= " + str(limits[0]) + " AND " \
                    + column + "<= " + str(limits[1]) + " )"
                limits = limits[2:]
        else:
            upper = random.randint(0,1)
            if upper:
                sel = sel + " ( " + column + " >= " + str(limits[-1]) + " )"
                limits = limits[:-1]
            else:
                sel = sel + " ( " + column + " <= " + str(limits[0]) + " )"
                limits = limits[1:]

        for i in range(len(limits)// 2):
            sel = sel + " OR ( " + column + " >= " + str(limits[i*2]) + " AND " \
                    + column + "<= " + str(limits[i*2 +1]) + " )"

    sel = sel + ")"

    return sel

def genQueries(n, k, b, d, j, outDir):
    outFILE = open('queries.sql', 'w'); 
    for i in range(n):
        index = str(i+1).zfill(len(str(n)))
        if j:
            pred = genJoinQuery(k,b,d)
            pred = queryJoinWhere + pred
            outFileName = outDir + "/" + origDir + outFileNameBase + index + outFileNameSuff
            with open(outFileName, 'wt') as outFILE:
                outFILE.write(queryJoinLargeBase + pred + querySuff)
            outFileName = outDir + "/" + sampleDir + outFileNameBase + index + outFileNameSuff
            with open(outFileName, 'wt') as outFILE:
                outFILE.write(queryJoinSampleBase + pred + querySuff)
                outFILE.write(queryJoinSelTemp);
        else:
            pred = genSelQuery(k, b, d)
            pred1 = querySelWhere + pred
            pred2 = querySelWhereSample + pred
            outFileName = "queries.sql"
            outFILE.write(queryBase + pred1 + querySuff)
            outFILE.write(queryBase + pred2 + querySuff) 
            
        print(queryBase + pred + querySuff)

def main():
    j = False
    if len(argv) < 10:
        usage()
        exit(1)
    try:
        opts, args = getopt.gnu_getopt(argv[1:], "b:d:hjk:n:",)
    except getopt.GetoptError as err:
        print(err)
        usage()
        exit(1)
    for o, a in opts:
        if o == "-h":
            usage()
            exit(0)
        elif o == "-b":
            b = int(a)
            if b < 1:
                stderr.write("You don't want an empty selection predicate, do you?\n")
                usage()
                exit(1)
        elif o == "-d":
            d = int(a)
            if d < 1:
                stderr.write("Empty domain, are you sure?\n")
                usage()
                exit(1)
        elif o == "-h":
            usage()
            exit(0)
        elif o == "-j":
            j = True;
        elif o == "-k":
            k = int(a)
            if k < 1:
                stderr.write("You want at least one column, don't you?\n")
                usage()
                exit(1)
        elif o == "-n":
            n = int(a)
            if n < 1:
                stderr.write("You want at least one query, don't you?\n")
                usage()
                exit(1)
        else:
            assert False, "unhandled option"

    if b < k :
        stderr.write("Warning: b < k: some columns will be ignored\n");

    outDir = args[0]
    if not os.path.exists(outDir):
        stderr.write("Cannot find the output directory. Please create it\n")
        usage()
        exit(1)
    if not os.path.exists(outDir + "/" + origDir):
	    os.mkdir(outDir + "/" + origDir)
    if not os.path.exists(outDir + "/" + sampleDir):
	    os.mkdir(outDir + "/" + sampleDir)

    genQueries(n, k, b, d, j, outDir) 

if __name__ == "__main__":
    main()

