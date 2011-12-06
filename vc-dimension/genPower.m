function [mat] = genPower(n, k, u, alpha)
% Generate a table with n tuples, and k columns, with values distributed
% according to the power law distribution  with parameter alpha in [0, u], 
% on all columns. Each column is generated independently.
% Output parameters: 
% mat: a n x k matrix representing the table
global RIONDA_RAND
if isempty(RIONDA_RAND)
    RIONDA_RAND = clock;
    RandStream.setDefaultStream( ... 
        RandStream('mt19937ar','Seed',sum(100*RIONDA_RAND)));
end

mat = zeros(n, k);
for i = 1:k
    c = randht(n, alpha);
    mat(:, i) = round((((u-1) / max(c)) * c) + 1);
end

