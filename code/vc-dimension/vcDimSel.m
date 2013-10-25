function [vc] = vcDimSel(k,b)
% function [vc] = vcDimSel(k,b)
% Compute the VC-dimension of the range space of selection queries with up to k
% column and b boolean clauses in the selection predicate.
% Output parameters:
% vc: an integer representing the VC-dimension.

if k == 1
	vc = 2*b;
else
	if b == 1
		vc = k+1;
	else
		vc = floor(2*(k+1)*b*log2((k+1)*b));
	end
end

