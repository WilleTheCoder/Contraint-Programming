import org.jacop.constraints.*;
import org.jacop.core.*;
import org.jacop.search.*;

public class Logistics {

    Store store;
    int graph_size = 6;
    int start = 1;
    int n_dests = 1;
    int[] dest = {5};
    int n_edges = 9;
    int[] from = {1,1,1,2,2,3,3,3,4};
    int[] to = {2,3,4,3,5,4,5,6,6};
    int[] cost = {6,1,5,5,3,5,6,4,2};

public void model(){

store = new Store();

int moreDests;
if(n_dests == 1){
    moreDests = 0;
}else{
    moreDests = 1;
}

IntVar[][] verts = new IntVar[n_dests][graph_size];
IntVar[][] costs = new IntVar[n_dests+moreDests][graph_size];
IntVar sum = new IntVar(store, "sum", 1, 100);
IntVar[] shared = new IntVar[graph_size];
int[][] weights = new int[graph_size][graph_size];
IntVar[][] sharedroad = new IntVar[graph_size][n_dests];

//Set weights
    for(int i = 0 ; i < n_edges ; i++){
        weights[from[i]-1][to[i] - 1] = cost[i];
    };


//Initialize verts, sharedroads, shared and costs
for(int k = 0; k < n_dests; k++){
    for(int i = 0; i < graph_size; i++){
        sharedroad[i][k] = new IntVar(store, "v"+(i+1), -1, 0);
        verts[k][i] = new IntVar(store, "v"+(i+1), i + 1, i + 1);
        costs[k][i] = new IntVar(store, "c"+(k)+(i+1), 0, 6);
            if(moreDests == 1){
                costs[n_dests][i] = new IntVar(store, "c"+(k+1)+","+(i+1), -1000, 0);
            };
            if(k == 0){
                shared[i]= new IntVar(store, "shared", -1*n_dests + 1, 0);
            };        
    };
};

//Add domain to verts depending on graph
for(int n = 0; n < n_dests; n++){
    for(int i = 0 ; i < n_edges; i++){
        verts[n][from[i] - 1].addDom(to[i], to[i]);
    };
    verts[n][dest[n]-1].addDom(start,start);
};

//Add Elements tied to the vertices
for(int k=0; k < n_dests; k++){
    for(int i = 0; i < graph_size; i++){
        store.impose(new Element(verts[k][i], weights[i], costs[k][i]));
    };
};

//Subcirc for each dest
for(int i = 0; i < n_dests; i++){
store.impose(new Subcircuit(verts[i]));
};

//Adding to the cost vector to reduce the cost from the shared roads (Only work for 2 dests or less)
if(n_dests > 1){
    for(int j = 0; j < n_dests; j++){
        for(int k = j+1; k < n_dests; k++){
            for(int i = 0; i < graph_size; i++){
                store.impose(new IfThenElse(new XeqY(verts[j][i], verts[k][i]) , new XeqC(sharedroad[i][j], -1), new XeqC(sharedroad[i][j], 0)));
            };
        };
    };

    for(int i = 0 ; i < graph_size; i++){
        store.impose(new XeqC(sharedroad[i][n_dests-1], 0));
        store.impose(new SumInt(sharedroad[i], "==", shared[i]));
        store.impose(new XmulYeqZ(shared[i], costs[0][i], costs[n_dests][i]));
    };
};




//Constraint to tie last node to the first for each dest
for(int i = 0; i < n_dests; i++){
store.impose(new XeqC(verts[i][dest[i]-1], start));
};

//creating 1 dimensional vectors as input to search function
IntVar[] costsum = new IntVar[graph_size*(n_dests+moreDests)];
IntVar[] vertsearch = new IntVar[graph_size*n_dests];

for(int i=0; i < n_dests+moreDests; i++){
    for(int n=0; n < graph_size; n++){
            costsum[i*graph_size + n] = costs[i][n];  
    };
};

for(int i=0; i < n_dests; i++){
    for(int n=0; n < graph_size; n++){
        vertsearch[i*graph_size + n] = verts[i][n];   
    };
};




store.impose(new SumInt(costsum, "==", sum));


Search<IntVar> search = new DepthFirstSearch<IntVar>();
SelectChoicePoint<IntVar> select = new SimpleSelect<IntVar>(vertsearch, new SmallestDomain<IntVar>(), new IndomainMin<IntVar>());
search.setSolutionListener(new PrintOutListener<IntVar>());

boolean result = search.labeling(store, select, sum);
};

    public static void main(String args[]) {
        Logistics l = new Logistics();
        l.model();
    }

};