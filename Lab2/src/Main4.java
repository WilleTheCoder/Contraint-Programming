import org.jacop.core.*;
import org.jacop.constraints.*;
import org.jacop.search.*;

public class Main4 {

	public static void main(String[] args) {
		// Create a store
		Store store = new Store();
		int size = 4;

		// Define variables
		IntVar a = new IntVar(store, "a", 1, 4);
		IntVar b = new IntVar(store, "b", 1, 4);
		IntVar c = new IntVar(store, "c", 1, 4);
		IntVar d = new IntVar(store, "d", 1, 4);

		IntVar[] v = { a, b, c, d };

		// Constraints
		store.impose(new Subcircuit(v));

		Search<IntVar> search = new DepthFirstSearch<IntVar>();
		SelectChoicePoint<IntVar> select = new InputOrderSelect<IntVar>(store, v, new IndomainMin<IntVar>());
		boolean result = search.labeling(store, select);

		if (result)
			System.out.println("Solution: " + v[0] + ", " + v[1] + ", " + v[2]);
		else
			System.out.println("**** No Solution");
		
	}
}