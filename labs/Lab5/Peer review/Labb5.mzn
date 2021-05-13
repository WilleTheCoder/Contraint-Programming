include "globals.mzn";
include "u_11.dzn";
int: n;
int: n_commercial;
int: n_residential;

array[1..n+1] of -n..n: point_distribution;
array[0..n] of -n..n: points= array1d(0..n,point_distribution); 

array[1..n,1..n] of var 0..1: matrix;
array[1..n] of var int: rows;
array[1..n] of var int: cols;

%calculates the total value
var int: cost = sum (i in 1..n) (rows[i]) + sum (j in 1..n) (cols[j]);

%optimize run time by removing symmetry
constraint 
  lex2(matrix);

%assigns values to each row and column
constraint 
  forall (i in 1..n) (
  rows[i] = points[sum (matrix[i,1..n])]
  /\
  cols[i] = points[sum (matrix[1..n,i])]
  );

%makes sure the right amount of slots of each type are used
constraint  
  n_residential = sum(i in 1..n, j in 1..n) (matrix[i,j]);

solve maximize cost;

output ["cost: \(cost) \n"]++
[show_int(1,matrix[i,j]) ++ " " ++ if j mod n==0 then "\n" else "" endif | i,j in 1..n];