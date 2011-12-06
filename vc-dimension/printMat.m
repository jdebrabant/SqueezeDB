function printMat(mat, outfile)
% print matrix mat on outfile, with values separated by '|'
[outfID, message] = fopen(outfile, 'w');
if outfID < 0
  disp(message);
end
matSize = size(mat);
k = matSize(2);
form = '%d';
for i = 1:k-1
    form = strcat(form, '|%d');
end
form = strcat(form, '\n');
fprintf(outfID, form, transpose(mat));
fclose(outfID);
