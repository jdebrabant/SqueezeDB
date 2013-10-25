function [A] = readMat(filename)
% Read a table with n rows and k columns from filename. Columns must be 
% separated with '|'
% Output parameters:
% A : a n x k matrix representing the table.

% importdata may not work for large matrix.
%A = importdata(filename, '|');

fid = fopen(filename);
C = textscan(fid, '%s', 1);
fclose(fid);
kcell = strfind(C{1}, '|');
ksize = size(kcell{1});
k = ksize(2) + 1;

format = '%d';
for i = 1:k-1
	format = strcat(format, ' %d');
end

fid = fopen(filename);
C = textscan(fid, format, 'delimiter', '|');
fclose(fid);

A = cell2mat(C);

