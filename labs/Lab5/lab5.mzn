include "globals.mzn";

%Input variables

% grid size
int: n; 

% nbr of commercial lots
int: n_commercial; 

% nbr of residential lots
int: n_residential; 

% point distribution depending on nbr of lots per row/col
array[0..n] of int: point_distribution; 

%Decision variables

% planning area
array[1..n, 1..n] of var 0..1: grid;

% nbr of residential lots in every row and col
% 0 represents row, 1 represents col
array[0..1, 1..n] of var int: n_res;

% score distribution row/col depending on n_res
array[0..1, 1..n] of var int: score;

%total score
var int: tot_score;

%Constraints

% constraint specifying nbr of res lots in grid
constraint sum(grid) = n_residential;

% sum nbr of res lots per row/col
constraint forall(i in 1..n)(
sum(j in 1..n)(grid[i,j]) = n_res[0,i] /\
sum(j in 1..n)(grid[j,i]) = n_res[1,i]
);

% calculate points per row/col
constraint forall(j in 1..n)(
score[0,j] = point_distribution[n_res[0,j]] /\
score[1,j] = point_distribution[n_res[1,j]]
); 

% calculate total score
constraint tot_score = sum(score);

% constraint blocking symmetrical solutions
constraint lex2(grid);

%Search
solve maximize tot_score;

 output["Total score: " ++ show(tot_score) ++ 
"\nNumber of resident slots per row/col:\n" ++ show(n_res) ++
"\nScore per row/col:\n" ++ show(score) ++ "\nGrid:\n" ++ show2d(grid)];

