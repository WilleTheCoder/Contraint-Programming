%nCakes
enum Products;  

%nIngridients
enum Resources;

%capacity of resources
array[Resources] of int: capacity;

%profit
array[Products] of int: profit;

%how much resources one product need
array[Products, Resources] of int: consumption;

%decision variable nbr of products
array[Products] of var 0..10000: nProducts;
array[Resources] of var 0..10000: magic;

%constraints cant use more resources than what we have
% Basic constraint: nProducts*resourceinbananaandinchocalate <= consumption(p,r);


constraint forall(r in Resources)(
magic[r]=sum(p in Products)(nProducts[p]*consumption[p,r])
);

constraint forall(r in Resources)(
magic[r]<=capacity[r]
);

solve maximize sum(p in Products) (nProducts[p]*profit[p]);

output [show(nProducts)];