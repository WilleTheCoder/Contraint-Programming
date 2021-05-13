  include "cumulative.mzn";
  include "diff2.mzn";

int: del_add;
int: del_mul;
int: number_add;
int: number_mul;
int: n;
array[int] of int: last;
array[int] of int: add;
array[int] of int: mul;
array[int] of set of int: dependencies;

%Här sparas sluttiderna för alla operationer för mult och add
array[1..length(add)] of var int: tFinishAdd;
array[1..length(mul)] of var int: tFinishMul;


array[1..length(add)] of var 1..number_add: resAdd;
array[1..length(add)] of int: tUseA= [del_add | i in 1..length(add)];
array[1..length(add)] of int: resUseA= [1 | i in 1..length(add)];

%resMul håller koll på vilken resurs som gör vad.
array[1..length(mul)] of var 1..number_mul: resMul;
array[1..length(mul)] of int: tUseM= [del_mul | i in 1..length(mul)];
array[1..length(mul)] of int: resUseM= [1 | i in 1..length(mul)];


array[1..n] of var 0..1000: xOrig;
var int: cost;


array[1..length(mul),1..number_mul] of var 0..n:xMulTake ;
array[1..length(mul),1..number_add] of var 0..n:xAddTake ;



%Tittar så att depenencies är efter det som måste vara gjort innan. För additioner
constraint forall((i in 1..length(add)))(
forall(j in dependencies[add[i]])(xOrig[add[i]]+ del_add <=xOrig[j] 
)
);

%Tittar så att depenencies är efter det som måste vara gjort innan. För multiplikationer
constraint forall((i in 1..length(mul)))(
forall(j in dependencies[mul[i]])(xOrig[mul[i]]+ del_mul <=xOrig[j] 
)
);


%Här så sammanställer vi bara vilka adders och multi som gör vad. Vi vet redan i resMul vilket nummer som gör vad.
%Här sammanställs det för att kunna framställa
constraint forall((i in 1..length(mul)))(
xMulTake[i,resMul[i]] =mul[i]
);
constraint forall((i in 1..length(add)))(
xAddTake[i,resAdd[i]] =add[i]
);

%Lägger sluttiden för addition operation
constraint forall((i in 1..length(add)))(
 tFinishAdd[i]= xOrig[add[i]]+ del_add
);

%Lägger sluttiden för multiplikation operation
constraint forall((i in 1..length(mul)))(
 tFinishMul[i]= xOrig[mul[i]] + del_mul
);


%diff2 tittar så att inte saker överlappar( aka att inte en multiplier gör många saker samtidigt.
%cumulative tittar så att det inte görs mer än gränsen som ges, tex att bara två multiplikationer kan göras under samma tidsperiod om number_mul=2
constraint diff2(tFinishAdd,resAdd,tUseA,resUseA);
constraint cumulative(tFinishAdd,tUseA,resUseA,number_add);

constraint diff2(tFinishMul,resMul,tUseM,resUseM);
constraint cumulative(tFinishMul,tUseM,resUseM,number_mul);


constraint cost = max(max(tFinishMul),max(tFinishAdd));

solve :: int_search(resAdd++resMul, first_fail, indomain_min) minimize cost; 
output [
     "Total cost:  ", show(cost), "\n"
      ++ concat(["Adder \(b): \(xAddTake[1..length(add),b]) \n"|b in 1..number_add])
      ++ concat(["Multiplier \(b): \(xMulTake[1..length(mul),b]) \n"|b in 1..number_mul])
];

