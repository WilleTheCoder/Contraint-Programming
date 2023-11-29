import java.util.ArrayList;

import org.jacop.constraints.Or;
import org.jacop.constraints.PrimitiveConstraint;
import org.jacop.constraints.Reified;
import org.jacop.constraints.Subcircuit;
import org.jacop.constraints.SumWeight;
import org.jacop.constraints.XeqC;
import org.jacop.core.BooleanVar;
import org.jacop.core.IntVar;
import org.jacop.core.Store;
import org.jacop.search.DepthFirstSearch;
import org.jacop.search.IndomainMin;
import org.jacop.search.Search;
import org.jacop.search.SelectChoicePoint;
import org.jacop.search.SimpleMatrixSelect;

public class Lab2 {

	public static void main(String[] args) {
		// Change the desired test case here
		TestCase testCase = new TestCase(1);
		logistics(testCase.graph_size, testCase.start, testCase.n_dests, testCase.n_edges, testCase.dest, testCase.from,
				testCase.to, testCase.cost);
	}

	public static void logistics(int graph_size, int start, int n_dests, int n_edges, int[] dest, int[] from, int[] to,
			int[] cost) {
		Store store = new Store();
		// Create vertices that will also store all the edges using domain.
		IntVar[][] graph_edges = new IntVar[n_dests][graph_size];

		for (int i = 0; i < n_dests; i++) {
			for (int j = 0; j < graph_size; j++) {
				graph_edges[i][j] = new IntVar(store, "vertice(" + (i + 1) + ", " + (j + 1) + ")");
				// nodes that are not the start node may loop to themselves
				if (j != (start - 1)) {
					if (j != dest[i] - 1) {
						graph_edges[i][j].addDom(j + 1, j + 1);
					} else {
						// destinations can return to start.
						graph_edges[i][j].addDom(start, start);
					}
				}
			}
		}

		// add all the edges, add edges in both directions
		for (int i = 0; i < n_dests; i++) {
			for (int j = 0; j < n_edges; j++) {
				graph_edges[i][to[j] - 1].addDom(from[j], from[j]);
				graph_edges[i][from[j] - 1].addDom(to[j], to[j]);
			}
			store.impose(new Subcircuit(graph_edges[i]));
		}

		// create boolean array for selected edges.
		BooleanVar[] chosenEdges = new BooleanVar[n_edges];
		for (int i = 0; i < n_edges; i++) {
			chosenEdges[i] = new BooleanVar(store, ("edge (" + from[i] + "," + to[i] + ")" + " with cost " + cost[i]));
		}

		// Create a constraint so edge cost is only counted once.
		for (int j = 0; j < n_edges; j++) {
			ArrayList<PrimitiveConstraint> constraintList = new ArrayList<PrimitiveConstraint>();
			for (int i = 0; i < n_dests; i++) {
				constraintList.add(new XeqC(graph_edges[i][from[j] - 1], to[j]));
				constraintList.add(new XeqC(graph_edges[i][to[j] - 1], from[j]));
			}
			store.impose(new Reified(new Or(constraintList), chosenEdges[j]));
		}

		// Set up and perform search.
		IntVar destCost = new IntVar(store, "Cost", 0, sum(cost));
		// The cost is summed up here by connecting the chosen edges to the cost and
		// storing the sum in destCost
		store.impose(new SumWeight(chosenEdges, cost, destCost));
		Search<IntVar> search = new DepthFirstSearch<IntVar>();
		SelectChoicePoint<IntVar> select = new SimpleMatrixSelect<IntVar>(graph_edges, null, new IndomainMin<IntVar>());
		
		boolean result = search.labeling(store, select, destCost);
		System.out.println("Printing selected edges..");
		for (BooleanVar chosenEdge : chosenEdges) {
			if (chosenEdge.value() == 1) {
				System.out.println(chosenEdge.id);
			}
		}
	}

	public static int sum(int[] v) {
		int sum = 0;
		for (int i = 0; i < v.length; i++) {
			sum += v[i];
		}
		return sum;
	}

	// Helper class to handle input
	static public class TestCase {
		int graph_size;
		int start;
		int n_dests;
		int n_edges;
		int[] dest;
		int[] from;
		int[] to;
		int[] cost;

		int[] dest1 = { 6 };
		int[] from1 = { 1, 1, 2, 2, 3, 4, 4 };
		int[] to1 = { 2, 3, 3, 4, 5, 5, 6 };
		int[] cost1 = { 4, 2, 5, 10, 3, 4, 11 };
		int[] dest2 = { 5, 6 };
		int[] from2 = { 1, 1, 2, 2, 3, 4, 4 };
		int[] to2 = { 2, 3, 3, 4, 5, 5, 6 };
		int[] cost2 = { 4, 2, 5, 10, 3, 4, 11 };
		int[] dest3 = { 5, 6 };
		int[] from3 = { 1, 1, 1, 2, 2, 3, 3, 3, 4 };
		int[] to3 = { 2, 3, 4, 3, 5, 4, 5, 6, 6 };
		int[] cost3 = { 6, 1, 5, 5, 3, 5, 6, 4, 2 };

		public TestCase(int testCase) {
			switch (testCase) {
			case 1:
				graph_size = 6;
				start = 1;
				n_dests = 1;
				n_edges = 7;
				dest = dest1;
				from = from1;
				to = to1;
				cost = cost1;
				break;
			case 2:
				graph_size = 6;
				start = 1;
				n_dests = 2;
				n_edges = 7;
				dest = dest2;
				from = from2;
				to = to2;
				cost = cost2;
				break;
			case 3:
				graph_size = 6;
				start = 1;
				n_dests = 2;
				n_edges = 9;
				dest = dest3;
				from = from3;
				to = to3;
				cost = cost3;
				break;
			}
		}
	}

}