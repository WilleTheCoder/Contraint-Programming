% finding the cheapest subgraph from start to dest in a graph
include "globals.mzn";

% parameters

int: graph_size;
int: start;
int: n_dests;
int: n_edges;
array[int] of int: dest;
array[int] of int: from;
array[int] of int: to;
array[int] of int: cost;

% decision variables

array[1..graph_size] of var int: subcost;
array[1..n_dests,   1..graph_size] of var set of int: next;
array[1..n_dests,   1..graph_size] of var int: flatnext;
array[1..graph_size,1..graph_size] of var 0..1000: visited;
array[1..graph_size,1..graph_size] of var 0..1000: distance;
var int: totalcost;

% constraints

% assign the domains
constraint forall(j in 1..n_dests, i in 1..graph_size)(
	next[j,i] =
	{to[x]   | x in 1..n_edges    where (from[x] == i /\ from[x] != dest[j])}
	union
	{from[x] | x in 1..n_edges    where (to[x] == i /\ to[x] != dest[j])}
	union
	{x       | x in 1..graph_size where (x == i /\ x != start /\ x != dest[j])}
	union
	{start   | x in 1..n_dests    where (dest[j] == i)}
);

% flatten the domain for insertion in subcircuit
constraint forall(i in 1..n_dests, j in 1..graph_size)(
	flatnext[i,j] in next[i,j]);
  

  
  output ["NEXT:", show(next),"\n"]++
  ["FLATNEXT", show(flatnext)];