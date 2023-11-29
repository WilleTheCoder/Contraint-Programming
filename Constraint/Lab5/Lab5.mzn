include "globals.mzn";
include "urban_1.dzn";
/*
nxn grid e.g 5x5 7x7 grid
Your job is to place a mix of residential and commercial lots to maximize the quality of the layout.
 
Any column or row that has 5 Residential lots = +5 points
Any column or row that has 4 Residential lots = +4 points
Any column or row that has 3 Residential lots = +3 points
Any column or row that has 5 Commercial lots = -5 points
Any column or row that has 4 Commercial lots = -4 points
Any column or row that has 3 Commercial lots = -3 points
*/

%Input variable
int: n;
int: n_commercial;
int: n_residental;
array[1..n] of int: point_distribution; 

%Decision variable
array[1..n, 1..n] of 1..n: grid;
var int: cost;

% specify nbr of residentals/commercial in the grid
constraint sum(i in 1..n, j in 1..n)(grid[i,j]) = n_residental;
