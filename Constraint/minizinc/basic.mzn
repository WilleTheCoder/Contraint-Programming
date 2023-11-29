include "globals.mzn";
/*
int: phonenumber=064;
int: money = 400;
array[1..5] of int: profit;
profit =[2,4,6,4,5];

int: n=5;
%int: n_prefs;

%array[1..n_prefs, 1..2] of var 1..n: prefs;

array[1..2] of int: a;
array[1..n] of var 1..n: s2;  

a=[5,8];
array[1..3,1..2]of int: x;

x=[|5,5|3,7|6,7|];
set of int:s={1,2,3,4,5};


*/

array[1..5] of 1..5: ray= [3,5,2,4,1];
array[1..5] of 1..5: inv = inverse(ray);

output[show(ray),"\n", show(inv)++"\n"]++
[show(inv[2])];

