function [uni] = genUnif(n, k, u)
% Generate a table with n tuples, and k columns, with values distributed
% according to the uniform distribution in [0, u], on all columns.
% Output parameters: 
% uni: a n x k matrix representing the table 

global RIONDA_RAND
if isempty(RIONDA_RAND)
    RIONDA_RAND = clock;
    RandStream.setDefaultStream( ... 
        RandStream('mt19937ar','Seed',sum(100*RIONDA_RAND)));
end

uni = randi([0, u], n, k);
