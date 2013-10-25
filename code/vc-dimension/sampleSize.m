function [t] = sampleSize(eps,delta,d)
% Computes the size of the sample needed to obtain an eps-approximation
% with probability at least delta for a range space with vc-dimension d.
% Output parameters:
% t: a number representing the number of sample needed
%original VapnikC71 bound: t = ceil((16 / eps^2) * ( 2 * d * log(sqrt(32)/eps) + log(4/delta)));
% refined LiLS01 bound, with constant from LofflerP09
t = ceil((0.5 / eps^2) * ( d  + log(1/delta)));

