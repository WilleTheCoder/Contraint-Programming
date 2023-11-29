% States problem: adjacent states must have different colors %
%As our first example, imagine that we wish to colour a map of Australia as shown in Fig. 2.1.1. It is made up of seven different states and territories each of which must be given a colour so that adjacent regions have different colours.%


/* basic parameter types: int, float, bool, string
we have 7 regions: wa,nt,sa,q,nsw,v and t

decision variable is a variable that can have severall values and will be given a certain value by the solver if that value
satisfies the constraints. does not need to give them a value when created.
var 1..n: name;

constraint a bunch of requirements that needs to be fullfilled for the problem to be solved.

*/


int: ncolors = 3;    %number of colors equals 3%

var 1..ncolors: wa; var 1..ncolors: nt; var 1..ncolors: sa; /* dec var "wa" can have a value from 1 to 3(number of colors) */
var 1..ncolors: q; var 1..ncolors: nsw; var 1..ncolors: v;
var 1..ncolors: t;

constraint wa != nt;
constraint wa != sa;
constraint nt != sa;
constraint nt != q;
constraint sa != q;
constraint sa != nsw;
constraint sa != v;
constraint q != nsw;
constraint nsw != v;

solve satisfy; /* indicates the kind of problem it is. In this case it is a satisfaction problem: we wish to find a value for the decision variables that satisfies the constraints but we do not care which one.*/

output["wa=\(wa)\t nt=\(nt) \t sa=\(sa) \n",
"q=\(q) \t nsw=\(nsw) \t v=\(v) \t t=\(t) ",
"wa=", show(wa), "\n"];     /*  "\(wa)"     */