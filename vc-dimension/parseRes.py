from sys import argv, exit, stderr, stdout
import getopt, math, os.path, random

def usage():
    stderr.write("Usage: {} [-j] -e eps -l largesize -s samplesize largeresults sampleresults\n".format(argv[0]))

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

def buildQueries(qfile, sampleFlag):
    queries = []
    if sampleFlag:
        toRead = 4 
    else:
        toRead = 3

    with open(qfile, 'rt') as qFILE:
        lines = []
        for i in range(toRead):
            lines.append(qFILE.readline())
        while True:
            queries.append(QueryRes(lines))
            if sampleFlag:
                temptSelect = queries[-1].ops[-1]
                queries[-1].ops[-1] = queries[-1].ops[-2]
                queries[-1].ops[-2] = queries[-1].ops[-3]
                queries[-1].ops[-3] = queries[-1].ops[-4]
                queries[-1].ops[0] = temptSelect

            if queries[-1].ops[-1].table == "a":
                aSelect = queries[-1].ops[-1]
                queries[-1].ops[-1] = queries[-1].ops[-2]
                queries[-1].ops[-2] = aSelect

            lines[0] = qFILE.readline()
            if len(lines[0]) == 0: # EOF!
                break
            lines[1] = qFILE.readline()
            lines[2] = qFILE.readline()
            if sampleFlag:
                lines[3] = qFILE.readline()
    return queries

def compareQueries(lqueries, squeries, lsize, ssize, e, joinFlag):
    if joinFlag:
        jop = 1
        divisor = math.pow(ssize, 2)
    else:
        jop = 0
        divisor = ssize
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
        s = squeries[i]
        #compare the join
        l.ops[0].err = math.fabs(l.ops[0].estRows - l.ops[0].actRows) / math.pow(lsize, 2)
        #s.ops[0].err = math.fabs((s.ops[0].actRows / math.pow(ssize,2)) - (l.ops[0].actRows / math.pow(lsize, 2)))
        s.ops[jop].err = math.fabs((s.ops[jop].actRows / divisor) - (l.ops[0].actRows / math.pow(lsize, 2)))

        if l.ops[0].actRows > 0:
            l.ops[0].errPerc = (l.ops[0].err * 100) /  (l.ops[0].actRows / math.pow(lsize, 2))
            s.ops[jop].errPerc = (s.ops[jop].err * 100) /  (l.ops[0].actRows / math.pow(lsize, 2))
        else:
            if l.ops[0].estRows > 0:
                l.ops[0].errPerc = (l.ops[0].err * 100) /  (1 / math.pow(lsize, 2))
            else:
                l.ops[0].errPerc = 0
            s.ops[jop].errPerc = 0.0

        errperclargesum += l.ops[0].errPerc
        errpercpredsum += s.ops[jop].errPerc

        if l.ops[0].err > e:
            l.ops[0].epsOK = 1
            results['epsOKLarge'] += 1
        if s.ops[jop].err > e:
            s.ops[jop].epsOK = 1
            results['epsOKPred'] += 1

        if s.ops[jop].err > l.ops[0].err:
            results['OKrel'] += 1

        #compare the select on "a"
        l.ops[1].err = math.fabs(l.ops[1].estRows - l.ops[1].actRows) / lsize
        s.ops[2].err = math.fabs((s.ops[2].actRows / ssize) - (l.ops[1].actRows / lsize))

        if l.ops[1].actRows > 0:
            l.ops[1].errPerc = (l.ops[1].err * 100) /  (l.ops[1].actRows / lsize)
            s.ops[2].errPerc = (s.ops[2].err * 100) /  (l.ops[1].actRows / lsize)
        else:
            if l.ops[1].estRows > 0:
                l.ops[1].errPerc = (l.ops[0].err * 100) /  (1 / math.pow(lsize, 2))
            else:
                l.ops[1].errPerc = 0
            s.ops[1].errPerc = 0.0

        errperclargesum += l.ops[1].errPerc
        errpercpredsum += s.ops[1].errPerc

        if l.ops[1].err > e:
            l.ops[1].epsOK = 1
            results['epsOKLarge'] += 1
        if s.ops[2].err > e:
            s.ops[2].epsOK = 1
            results['epsOKPred'] += 1

        if s.ops[2].err > l.ops[1].err:
            results['OKrel'] += 1

         #compare the select on "b"
        l.ops[2].err = math.fabs(l.ops[2].estRows - l.ops[2].actRows) / lsize
        s.ops[3].err = math.fabs((s.ops[3].actRows / ssize) - (l.ops[2].actRows / lsize))

        if l.ops[2].actRows > 0:
            l.ops[2].errPerc = (l.ops[2].err * 100) / (l.ops[2].actRows / lsize)
            s.ops[3].errPerc = (s.ops[3].err * 100) / (l.ops[2].actRows  / lsize)
        else:
            if l.ops[2].estRows > 0:
                l.ops[2].errPerc = (l.ops[0].err * 100) /  (1 / math.pow(lsize, 2))
            else:
                l.ops[2].errPerc = 0
            s.ops[3].errPerc = 0.0


        errperclargesum += l.ops[2].errPerc
        errpercpredsum += s.ops[3].errPerc

        if l.ops[2].err > e:
            l.ops[2].epsOK = 1
            results['epsOKLarge'] += 1
        if s.ops[3].err > e:
            s.ops[3].epsOK = 1
            results['epsOKPred'] += 1

        if s.ops[3].err > l.ops[2].err:
            results['OKrel'] += 1

    results['avgerrpercpred'] = errpercpredsum / len(lqueries)
    results['avgerrperclarge'] = errperclargesum / len(lqueries)
    errpercpredsum = 0.0
    errperclargesum = 0.0
    for i in range(len(lqueries)):
        #sum the contribution of the err of the join, of a, of b (square!)
        l = lqueries[i]
        s = squeries[i]
        errpercpredsum += math.pow(s.ops[0].errPerc - results['avgerrpercpred'], 2)
        errpercpredsum += math.pow(s.ops[2].errPerc - results['avgerrpercpred'], 2)
        errpercpredsum += math.pow(s.ops[3].errPerc - results['avgerrpercpred'], 2)
        errperclargesum += math.pow(l.ops[0].errPerc - results['avgerrperclarge'], 2)
        errperclargesum += math.pow(l.ops[1].errPerc - results['avgerrperclarge'], 2)
        errperclargesum += math.pow(l.ops[2].errPerc - results['avgerrperclarge'], 2)
    results['stddeverrpercpred'] = math.sqrt(errpercpredsum / (len(lqueries) -1))
    results['stddeverrperclarge'] = math.sqrt(errperclargesum / (len(lqueries) -1))
    return results

def printResults(results):
    print("{}\t{}\t{}\t{}\t{}\t{}\t{}".format(results['epsOKPred'], results['epsOKLarge'], \
                results['OKrel'], results['avgerrpercpred'], \
                results['avgerrperclarge'], results['stddeverrpercpred'], \
                results['stddeverrperclarge']))

def parseRes(e, lsize, ssize, lfile, sfile, joinFlag):
    lqueries = buildQueries(lfile, False)
    squeries = buildQueries(sfile, True)
    results = compareQueries(lqueries, squeries, lsize, ssize, e, joinFlag)
    printResults(results)

def main():
    # When specified, the joinFlag (-j) gives the "practitioner" results.
    joinFlag = False
    if len(argv) < 9:
        usage()
        exit(1)
    try:
        opts, args = getopt.gnu_getopt(argv[1:], "e:hjl:s:",)
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
        elif o == "-j":
            joinFlag = True
        elif o == "-l":
            lsize = int(a)
            if lsize < 1:
                stderr.write("Uhm, a large table smaller than one tuple?\n")
                usage()
                exit(1)
        elif o == "-s":
            ssize = int(a)
            if ssize < 1:
                stderr.write("Uhm, a sample table smaller than one tuple?\n")
                usage()
                exit(1)
        else:
            stderr.write("Something is wrong. This should never happen!!!\n")
            exit(1)
    lfile = args[0]
    sfile = args[1]

    if not os.path.exists(lfile):
        stderr.write("Cannot find the large file\n")
        usage()
        exit(1)
    if not os.path.exists(sfile):
        stderr.write("Cannot find the sample file\n")
        usage()
        exit(1)

    parseRes(e,lsize,ssize,lfile,sfile, joinFlag)

if __name__ == "__main__":
    main()

