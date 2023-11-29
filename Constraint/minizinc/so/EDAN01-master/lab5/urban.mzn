include "globals.mzn";
int : n;
int : n_commercial;
int : n_residential;
array[0..n] of int : point_distribution;
array[1..n, 1..n] of var 0..1: grid;
array[1..2, 1..n] of var int: cost;

constraint sum(i in 1..n, j in 1..n)(grid[i,j]) = n_residential;
constraint forall(i in 1..n)(element(sum(col(grid, i)), point_distribution, cost[1,i]) /\ element(sum(row(grid, i)), point_distribution, cost[2,i]));
constraint lex2(grid);
solve maximize sum(i in 1..2, j in 1..n)(cost[i,j]);
output[show(sum(i in 1..2, j in 1..n)(cost[i,j]))];
