include "globals.mzn";

int: graph_size;
int: start;
int: n_dests;
array[1..n_dests] of int: dest;
int: n_edges;
array[1..n_edges] of int: from;
array[1..n_edges] of int: to;
array[1..n_edges] of int: cost;

array[1..n_edges] of var bool: is_used;
var int: totalcost;

% Start node has atleast one connected edge used.
constraint
  sum(edge in 1..n_edges where from[edge] = start \/ to[edge] = start)(is_used[edge]) = 1;

% Dest nodes has atleast one connected edge used.
constraint forall (node in 1..n_dests) (
  sum(edge in 1..n_edges where to[edge] = dest[node] \/ from[edge] = dest[node])(is_used[edge]) = 1
);

constraint forall(node in 1..graph_size) ( % in between nodes have either 0 or >1 connected edges
  if (node != start /\ forall(i in 1..n_dests)(dest[i] != node)) then
    sum(edge in 1..n_edges where to[edge] = node \/ from[edge] = node)(is_used[edge]) == 0
    \/
    sum(edge in 1..n_edges where to[edge] = node \/ from[edge] = node)(is_used[edge]) > 1
  endif
);

constraint
    totalcost = sum(i in 1..n_edges where is_used[i])(cost[i]);

solve minimize totalcost;

output[show(is_used), "\n", show(totalcost)];