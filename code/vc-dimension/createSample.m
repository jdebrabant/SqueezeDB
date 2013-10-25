function [A] = createSample(B, t)
% Create a sample of n x k matrix B by drawing t rows of B at random with
% replacement.
% Output parameters:
% A: a t x k matrix representing the sample

global RIONDA_RAND
if isempty(RIONDA_RAND)
    RIONDA_RAND = clock;
    RandStream.setDefaultStream( ... 
        RandStream('mt19937ar','Seed',sum(100*RIONDA_RAND)));
end

sizeB = size(B);
n = sizeB(1);
%rows = randi(n,1,t);
rows = randsample(n,t,true);
A = B(rows, :);
