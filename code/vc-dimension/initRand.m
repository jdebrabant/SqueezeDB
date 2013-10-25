function initRand()
% Initialize the random generator
global RIONDA_RAND
RIONDA_RAND = clock;
RandStream.setDefaultStream( ... 
    RandStream('mt19937ar','Seed',sum(100*RIONDA_RAND)));
