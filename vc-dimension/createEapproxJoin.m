function [SA,SB] = createEapproxJoin(A, B, eps, delta, k, b)

global RIONDA_RAND
if isempty(RIONDA_RAND)
    RIONDA_RAND = clock;
    RandStream.setDefaultStream( ... 
        RandStream('mt19937ar','Seed',sum(100*RIONDA_RAND)));
end

d = vcDimJoin(k,b);
t = sampleSize(eps, delta, d);
SA = createSample(A, t);
SB = createSample(B, t);
indexS = transpose(1:t);
SA = [ indexS, SA];
SB = [ indexS, SB];
