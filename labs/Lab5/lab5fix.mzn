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

% score distribution row/col depending on n_res
array[0..1, 1..n] of var int: score;

%total score
var int: tot_score;

%Constraints

% constraint specifying nbr of res lots in grid
constraint sum(grid) = n_residential;

% sum nbr of res lots per row/col
constraint forall(i in 1..n)(
point_distribution[sum(j in 1..n)(grid[i,j])] = score[0,i] /\
point_distribution[sum(j in 1..n)(grid[j,i])] = score[1,i]
);

% calculate total score
constraint tot_score = sum(score);

% constraint blocking symmetrical solutions
constraint lex2(grid);

%Search
solve maximize tot_score;

%Output
 output["Total score: " ++ show(tot_score) ++ 
"\nScore per row/col:\n" ++ show(score) ++ "\nGrid:\n" ++ show2d(grid)];
