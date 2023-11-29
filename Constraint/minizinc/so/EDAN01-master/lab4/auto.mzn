include "globals.mzn";
include "jacop.mzn";

int : del_add;
int : del_mul;
int : number_add;
int : number_mul;
int : n;
set of int : last;
set of int : add;
set of int : mul;
array[1..n] of set of int : dependencies;
int : d_max = if del_mul > del_add then del_mul else del_add endif;
int : r_max = if number_mul > number_add then number_mul else number_add endif;

array[1..n] of var 0..n*d_max : t;
array[1..n] of var 0..d_max : d;
array[1..n] of var 1..r_max : r;
var int : score;

constraint forall(i in add, j in dependencies[i])(t[i] + del_add <= t[j] /\ d[i] = del_add);
constraint forall(i in mul, j in dependencies[i])(t[i] + del_mul <= t[j] /\ d[i] = del_mul);
constraint forall(i in last where i in add)(d[i] = del_add); 
constraint forall(i in last where i in mul)(d[i] = del_mul); 

constraint diffn([t[i] | i in add], [r[i] | i in add], [del_add | i in add], [1 | i in add]);
constraint diffn([t[i] | i in mul], [r[i] | i in mul], [del_mul | i in mul], [1 | i in mul]);

constraint cumulative([t[i] | i in add],[del_add | i in add], [1 | i in add], number_add);
constraint cumulative([t[i] | i in mul],[del_mul | i in mul], [1 | i in mul], number_mul);

constraint forall(i in last)(t[i] + d[i] <= score);

solve :: seq_search([
         int_search(t, smallest_max, indomain_min, complete)])
         minimize score;
