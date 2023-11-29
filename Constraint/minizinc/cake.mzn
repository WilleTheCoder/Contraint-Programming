/* cake problem, we have a bunch of ingridients and we know how to bake to types of cakes
question,how many cakes of each type should we bake to get the most profit?
Banana cake:  250g flour, 2 bananas, 75g sugar, 100g butter:   price: 4.00$
Chocolate cake: 200g flour, 75g cocoa, 150g sugar, 150g butter   price: 4.50$  
Ingridients: 4kg flour, 6 bananas, 2kg sugar, 500g butter, 500g cocoa */

int: flour;
int: banana;
int: sugar;
int: butter;
int: cocoa;


constraint assert(flour>=0, "Invalid, amount of flour must be greater than 0");
constraint assert(sugar>=0, "Invalid, amount of flour must be greater than 0");
constraint assert(banana>=0, "Invalid, amount of flour must be greater than 0");
constraint assert(butter>=0, "Invalid, amount of flour must be greater than 0");
constraint assert(cocoa>=0, "Invalid, amount of flour must be greater than 0");

var 0..100: bananaCake;
var 0..100: chocolateCake;

constraint bananaCake*250 + chocolateCake*200 <= flour;     %flour-constraint
constraint bananaCake*2 <= banana;                              %banana-constraint
constraint bananaCake*75+chocolateCake*150 <= sugar;      %sugar-constraint
constraint bananaCake*100+chocolateCake*150 <= butter;      %butter-constraint
constraint chocolateCake*75 <= cocoa;                      %cocoa-constraint

solve maximize bananaCake*400 + chocolateCake*450;

output["No. of bananacakes: \(bananaCake)\n",
"No. of chocolatecakes: \(chocolateCake)"];