function [M] = genCorrMat(n, m, D)
% function [M] = genCorrMat(n, m, D)
% Generate a n x k matrix with correlated values, normally distributed
% around m (or actually, around m translated so that the minimum value in both
% dimensions is 0). D is the k x k covariance matrix. 
% Output parameters: 
% M: a n x k matrix representing the table

global RIONDA_RAND
if isempty(RIONDA_RAND)
    RIONDA_RAND = clock;
    RandStream.setDefaultStream( ... 
        RandStream('mt19937ar','Seed',sum(100*RIONDA_RAND)));
end

sizeD = size(D);
k = sizeD(1);
mu = m * ones(n, k);
M = mvnrnd(mu,D);
mins = min(M);
toSub = ones(n,1) * mins;
M = M - toSub; 
M = round(M);
% plot(M(:,1),M(:,2),'+');

