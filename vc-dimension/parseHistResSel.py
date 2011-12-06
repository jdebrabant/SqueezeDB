from sys import argv, exit, stderr, stdout
import getopt, math, os.path, random

def usage():
    stderr.write("Usage: {} -e eps -l largesize largeresults histresults\n".format(argv[0]))

class QueryRes():
    class OpRes():
        def __init__(self, line):
            tokens = line.split("|")
            self.optype = tokens[1]
            self.table = (tokens[2])[-2]
            self.estRows = int(tokens[3])
            self.actRows = int(tokens[4])
            self.err = 0.0
            self.epsOK = True
            self.errPerc = 0.0
            self.line = line

    def __init__(self, lines):
        opNumber = 0
        self.ops = []
        for line in lines:
            self.ops.append(QueryRes.OpRes(line))

def buildQueries(qfile):
    queries = []

    with open(qfile, 'rt') as qFILE:
        for line in qFILE:
            queries.append(QueryRes([line]))

    return queries

class QueryResHist():
    class OpRes():
        def __init__(self, line):
            tokens = line.split("(")
            tokensOp = tokens[0].split("on")
            self.optype = tokensOp[0]
            if len(tokensOp) > 1:
                self.table = tokensOp[1]
            else:
                self.table = ""
            #print("table:'{}'".format(self.table))
            tokensR = tokens[1].split("rows=")
            tokensR2 = tokensR[1].split(" ")
            self.estRows = int(tokensR2[0])
            #print(self.estRows)
            self.err = 0.0
            self.epsOK = True
            self.errPerc = 0.0
            self.line = line

    def __init__(self, lines):
        opNumber = 0
        self.ops = []
        for line in lines:
            self.ops.append(QueryResHist.OpRes(line))


def buildQueriesHist(hfile):
    queries = []
    with open(hfile, 'rt') as hFILE:
        lines = []
        hFILE.readline()
        lines.append(hFILE.readline())
        while True:
            queries.append(QueryResHist(lines))

            foo = hFILE.readline()
            if len(foo) == 0: # EOF!
                break
            lines[0] = hFILE.readline()
    return queries

def compareQueries(lqueries, hqueries, lsize, e):
    results = dict([])
    results['epsOKPred'] = 0
    results['epsOKLarge'] = 0
    results['OKrel'] = 0
    results['avgerrpercpred'] = 0.0
    results['avgerrperclarge'] = 0.0
    results['stddeverrpercpred'] = 0.0
    results['stddeverrperclarge'] = 0.0
    errpercpredsum = 0.0
    errperclargesum = 0.0
    for i in range(len(lqueries)):
        l = lqueries[i]
        s = hqueries[i]

        #compare the select on "a"
        l.ops[0].err = math.fabs(l.ops[0].estRows - l.ops[0].actRows) / lsize
        s.ops[0].err = math.fabs(s.ops[0].estRows - l.ops[0].actRows) / lsize

        if l.ops[0].actRows > 0:
            l.ops[0].errPerc = (l.ops[0].err * 100) /  (l.ops[0].actRows / lsize)
            s.ops[0].errPerc = (s.ops[0].err * 100) /  (l.ops[0].actRows / lsize)
        else:
            if l.ops[0].estRows > 0:
                l.ops[0].errPerc = (l.ops[0].err * 100) /  (1 / math.pow(lsize, 2))
            else:
                l.ops[0].errPerc = 0
            if s.ops[0].estRows > 0:
                s.ops[0].errPerc = (s.ops[0].err * 100) /  (1 / math.pow(lsize, 2))
            else:
                s.ops[0].errPerc = 0


        errperclargesum += l.ops[0].errPerc
        errpercpredsum += s.ops[0].errPerc

        if l.ops[0].err > e:
            l.ops[0].epsOK = 1
            results['epsOKLarge'] += 1
        if s.ops[0].err > e:
            s.ops[0].epsOK = 1
            results['epsOKPred'] += 1

        if s.ops[0].err > l.ops[0].err:
            results['OKrel'] += 1

    results['avgerrpercpred'] = errpercpredsum / len(lqueries)
    results['avgerrperclarge'] = errperclargesum / len(lqueries)
    errpercpredsum = 0.0
    errperclargesum = 0.0
    for i in range(len(lqueries)):
        #sum the contribution of the err of the join, of a, of b (square!)
        l = lqueries[i]
        s = hqueries[i]
        errpercpredsum += math.pow(s.ops[0].errPerc - results['avgerrpercpred'], 2)
        errperclargesum += math.pow(l.ops[0].errPerc - results['avgerrperclarge'], 2)
    results['stddeverrpercpred'] = math.sqrt(errpercpredsum / (len(lqueries) -1))
    results['stddeverrperclarge'] = math.sqrt(errperclargesum / (len(lqueries) -1))
    return results

def printResults(results):
    print("{}\t{}\t{}\t{}\t{}\t{}\t{}".format(results['epsOKPred'], results['epsOKLarge'], \
                results['OKrel'], results['avgerrpercpred'], \
                results['avgerrperclarge'], results['stddeverrpercpred'], \
                results['stddeverrperclarge']))

def parseRes(e, lsize, lfile, hfile, ):
    lqueries = buildQueries(lfile)
    hqueries = buildQueriesHist(hfile)
    results = compareQueries(lqueries, hqueries, lsize, e)
    printResults(results)

def main():
    if len(argv) < 7:
        usage()
        exit(1)
    try:
        opts, args = getopt.gnu_getopt(argv[1:], "e:hl:",)
    except getopt.GetoptError as err:
        print(err)
        usage()
        exit(1)
    for o, a in opts:
        if o == "-h":
            usage()
            exit(0)
        elif o == "-e":
            e = float(a)
            if e > 1 or e < 0:
                stderr.write("Epsilon should be between 0 and 1\n")
                usage()
                exit(1)
        elif o == "-l":
            lsize = int(a)
            if lsize < 1:
                stderr.write("Uhm, a large table smaller than one tuple?\n")
                usage()
                exit(1)
        else:
            stderr.write("Something is wrong. This should never happen!!!\n")
            exit(1)
    lfile = args[0]
    hfile = args[1]

    if not os.path.exists(lfile):
        stderr.write("Cannot find the large file\n")
        usage()
        exit(1)
    if not os.path.exists(hfile):
        stderr.write("Cannot find the histogram file\n")
        usage()
        exit(1)

    parseRes(e,lsize,lfile,hfile)

if __name__ == "__main__":
    main()

