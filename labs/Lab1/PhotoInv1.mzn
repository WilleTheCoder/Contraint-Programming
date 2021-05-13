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
var 0..n_prefs: cost;
%satistfied preferences
array[1..n] of var int: satprefs;

%Constraints

%alldifferent
constraint all_different(placement);

%break summetry
constraint placement[1]<placement[2];

%assign satprefs[i]
constraint forall(i in 1..n_prefs)(
satprefs[i] =if abs(index[prefs[i,1]]-index[prefs[i,2]]) = 1 then 1 else 0 endif
);

%cost variable based on placement
constraint cost = sum(max(satprefs));

%Maximize nbr of satisfied preferences
solve :: int_search(placement, first_fail, indomain_min, complete) maximize cost;

output
["Placements: \(placement)\n"]++
["Cost: \(cost)\n"]++
["Satisfied preferences: \n"]++
[show([prefs[p,i] | i in 1..2]) ++ ": " ++ show(satprefs[p]) ++ "\n"
   | p in 1..n_prefs];

