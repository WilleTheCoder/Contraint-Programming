include "globals.mzn";

%Input variables

int : del_add;       %clock_cycles add instruction
int : del_mul;       %clock_cycles mul instruction
int : number_add;    %nbr of add processes
int : number_mul;    %nbr of add processes
int : n;             %nbr of instructions
set of int : last;   %last nodes in graph
set of int : add;    %add instructions
set of int : mul;    %mul instructions
array[1..n] of set of int : dependencies;   %dependencies 

%Decision variables

array[1..n] of var 0..100 : start_time;    
array[1..n] of var 0..100 : duration;
array[1..n] of var 1..number_add : add_resources;
array[1..n] of var 1..number_mul : mul_resources;
var int : endTime;
var int : numberOfResources;
var int : nadd;
var int : nmul;

%Constraints

%defining durations for add and mul ops
constraint forall(i in add)(duration[i] = del_add); 
constraint forall(i in mul)(duration[i] = del_mul); 
%dependency add
%if its a add op
%endtime <= starttime(dependency)
constraint forall(i in add, j in dependencies[i]) (duration[i] = del_add /\ start_time[i] + del_add <= start_time[j]);

%dependancy mul
constraint forall(i in mul, j in dependencies[i])(duration[i] = del_mul /\ start_time[i] + del_mul <= start_time[j]);

%boxes may not overlap
%resource constraints for adders
constraint diffn([start_time[i] | i in add], [add_resources[i] | i in add], [del_add | i in add], [1 | i in add]);
%resource constraints for multipliers
constraint diffn([start_time[i] | i in mul], [mul_resources[i] | i in mul], [del_mul | i in mul], [1 | i in mul]);

%cumulative with limit nbr of add resp mul op
%prune domains 
constraint cumulative([start_time[i] | i in add],[del_add | i in add], [1 | i in add], number_add);
constraint cumulative([start_time[i] | i in mul],[del_mul | i in mul], [1 | i in mul], number_mul);

%find max endtime for last nodes
constraint endTime = max(i in last)(start_time[i] + duration[i] );

%Search
%Minimize endTime

constraint  nadd = max(i in 1..12)(add_resources[i]);
constraint  nmul = max(i in 1..16)(mul_resources[i]);
constraint numberOfResources = nadd + nmul;

solve :: seq_search([
         int_search(start_time, first_fail, indomain_min, complete)])
         minimize endTime;

    
output["Clock_cycles: \(endTime)\n"];
output["Adder\n"];
output[ "\(add[i]):a\(add_resources[add[i]]) | " |i in 1..12];
output["\nMultiplier\n"];
output[ "\(mul[i]):m\(mul_resources[mul[i]]) | " |i in 1..16];
   