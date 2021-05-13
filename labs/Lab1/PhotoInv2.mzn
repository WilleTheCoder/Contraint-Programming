include "globals.mzn";
%Input variables

%nbr of people
int: n;
%nbr of preferences (standing nxt to)
int: n_prefs;
%array of preferences
array[1..n_prefs, 1..2] of 1..n: prefs;

%Decision variables

%placement array
array[1..n] of var 1..n: placement;
%index array
array[1..n] of var int: index=inverse(placement);
%cost array
var 0..n: cost;
%distances 
array[1..n_prefs] of var int: distance;

%Constraints

%alldifferent
constraint all_different(placement);

%break summetry
constraint placement[1]<placement[2];

%assign distances
constraint forall(i in 1..n_prefs)(
distance[i] = abs(index[prefs[i,1]]-index[prefs[i,2]])
);

constraint cost = max(distance);

%Solver
solve minimize cost;

%Output
output
["Placements: \(placement)\n"]++
["Cost: \(cost) \n"]++
["Distances: \n"]++
[show([prefs[p,i] | i in 1..2]) ++ ": " ++ show(distance[p]) ++ "\n"
   | p in 1..n_prefs];

