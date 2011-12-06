function [S] = createEapproxSel(M, eps, delta, k, b)
% function [S] = createEapprox(M, eps, delta, k, b)
% Create a sample of the matrix M which is, with probability at least 1-delta
% an eps-approximation of the range space of the selection queries involving up
% to k columns and b boolean clauses in the selection predicate.
% Output parameters:
% S: a matrix representing the eps-approximation.

d = vcDimSel(k,b);
t = sampleSize(eps, delta, d);
S = createSample(M, t);

