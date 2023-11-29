package Main;



import java.util.ArrayList;

import org.jacop.constraints.Alldiff;
import org.jacop.constraints.XeqC;
import org.jacop.constraints.XltY;
import org.jacop.constraints.XlteqC;
import org.jacop.constraints.XplusClteqZ;
import org.jacop.constraints.XplusYeqZ;
import org.jacop.core.IntVar;
import org.jacop.core.Store;

import Split1.Split1;
import Split2.Split2;

/**
 * 
 * It models a Golomb ruler problem.
 * 
 * @author Radoslaw Szymanek and Krzysztof Kuchcinski
 *
 * 
 * Golomb ruler is a special sequence of natural numbers
 * an example is 0 1 4 6
 *
 * a sequence is a Golomb ruler if all differences are different
 * (1-0), (4-0), (6-0), (4-1), (6-1), (6-4)
 * 1 4 6 3 5 2
 * All differences above have different values
 * A Golomb ruler is optimal if the length of it (the last mark)
 * has the smallest possible value
 * The presented ruler with 4 marks of length 6 is optimal
 */

public class Golomb {

    Store store;

    /**
     * It specifies the number of marks (number of natural numbers in 
     * the sequence).
     */
    public int noMarks = 10;

    /**
     * It specifies the upper bound of the optimal solution.
     */
    public int bound = -1;

	
    /**
     * It contains all differences between all possible pairs of marks.
     */
    public ArrayList<IntVar> subs = new ArrayList<IntVar>();
	
    /**
     * It executes the program which computes the optimal Golomb ruler. 
     * 
     * @param args the first argument specifies the number of marks, the second argument specifies the upper bound of the optimal solution.
     */
    public static void main(String args[]) {
		
	Golomb example = new Golomb();
		
	example.model();

    }			

    public void model() {

	System.out.println("Program to solve Golomb mark problem - length "
			   + noMarks);

	store = new Store();
	ArrayList<IntVar> vars = new ArrayList<IntVar>();
		
	IntVar numbers[] = new IntVar[noMarks];

	for (int i = 0; i < numbers.length; i++) {
	    // Create FDV for each natural number
	    numbers[i] = new IntVar(store, "n" + new Integer(i), (i + 1) * i / 2,
				    noMarks * noMarks);

	    // Impose constraints that each consequtive number
	    // is larger than the previous one
	    // Golomb ruler is an ordered sequence of numbers
	    if (i > 0)
		store.impose(new XltY(numbers[i - 1], numbers[i]));
	    else
		store.impose(new XeqC(numbers[0], 0));
	}

	for (IntVar v : numbers)
	    vars.add(v);
		
	if (bound > -1)
	    store.impose(new XlteqC(numbers[noMarks - 1], bound));

	// ArrayList contains all differences
	subs = new ArrayList<IntVar>();

	for (int i = 1; i < numbers.length; i++) {

	    // for (int j = i - 1; j >= 0; j--) {
	    for (int j = 0; j < i; j++) {
		// Create FDV for a difference between ith and jth number
		IntVar sub = new IntVar(store, "c" + i + "_" + j, (i - j)
					* (i - j + 1) / 2, noMarks * noMarks);

		subs.add(sub);

		// sub + jth = ith since sub = ith - jth
		// Add constraint so the above relationship holds
		//			store.imposePropagators(new XplusYeqZ(sub, numbers[j], numbers[i]));
		store.impose(new XplusYeqZ(sub, numbers[j], numbers[i]));

	    }
	}

	int index = 0;
	for (int i = 1; i < noMarks; i++)
	    for (int j = 0; j < i; j++)
		//				store.imposePropagators(new XplusClteqZ(subs.get(index++), (noMarks - 1 - i + j)
		//						* (noMarks - i + j) / 2, numbers[noMarks - 1]));
		store.impose(new XplusClteqZ(subs.get(index++), (noMarks - 1 - i + j)
					     * (noMarks - i + j) / 2, numbers[noMarks - 1]));

	// symmetry breaking constraint
	// important constraint to reduce search space since
	// interested in proving the optimality
	store.impose(new XltY(subs.get(0), subs.get(subs.size() - 1)));

	// All differences have to have unique values
	store.impose(new Alldiff(subs), 1);

	IntVar cost = numbers[numbers.length - 1];

	//SimpleDFS search = new SimpleDFS(store);
	Split1 search = new Split1(store);
	//Split2 search = new Split2(store);

	
	search.setVariablesToReport(numbers);
	search.setCostVariable(cost);

	boolean result = search.label(numbers);

	System.out.println("Total number of visited nodes = " + search.N);
	System.out.println("Total number of failed nodes = " + search.failedNodes);
	
	System.out.println (result);

    }
	//Simple DFS 10
    //Total number of visited nodes = 32059
    //Total number of failed nodes = 16029
}
