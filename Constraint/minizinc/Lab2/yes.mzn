include "globals.mzn";

int: graph_size=4;
int: n_dests =1;

array[1..n_dests, 1..graph_size] of var int: flatnext;

constraint forall (i in 1..n_dests)(
	subcircuit([flatnext[i,j] | j in 1..graph_size]));

output[show(flatnext),"\n"];
