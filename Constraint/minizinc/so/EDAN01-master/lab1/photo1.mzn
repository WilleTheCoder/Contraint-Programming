include "globals.mzn";

int: n;
int: n_prefs;
array[1..n_prefs, 1..2] of int: prefs;
array[1..n] of var 1..n: pos;
array[1..n_prefs] of var int: dists;

constraint alldifferent (pos);
constraint pos[1] < pos[2];
constraint forall (i in 1..n_prefs) (dists[i] = abs(pos[prefs[i,1]] - pos[prefs[i,2]]));
solve maximize among(dists, {1});

output["Result:", show(pos)];
