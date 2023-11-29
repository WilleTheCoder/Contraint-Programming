
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

public class Main2 {

	public static void main(String[] args) {
		Test t = new Test(3);
		assignment(t.graph_size, t.start, t.n_dests, t.n_edges, t.dest, t.from, t.to, t.cost);
	}

	public static void assignment(int graph_size, int start, int n_dests, int n_edges, int[] dest, int[] from, int[] to,
			int[] cost) {

		Store store = new Store();

		IntVar[][] edge = new IntVar[n_dests][graph_size];
//iom att start, dest ej kan ref till sig själv måste de ingå i subcircuit
		for (int j = 0; j < n_dests; j++) {
			for (int i = 0; i < graph_size; i++) {
				edge[j][i] = new IntVar(store, "v" + (i + 1));

				// "in-between nodes" can refer to its self
				if (i != (start - 1) && i != (dest[j] - 1)) {
					edge[j][i].addDom(i + 1, i + 1);
				}
				// destination nodes can connect to start-node
				if (i == dest[j] - 1)
					edge[j][i].addDom(start, start);
			}
		}

		BooleanVar[] used_edges = new BooleanVar[n_edges];
		for (int i = 0; i < n_edges; i++) {
			used_edges[i] = new BooleanVar(store, "edge" + "["+(i+1)+"]"+" cost: "+cost[i]);
		}

		// add domains for edge for all edges
		// undirected graph
		for (int j = 0; j < n_dests; j++) {
			for (int i = 0; i < n_edges; i++) {
				edge[j][to[i] - 1].addDom(from[i], from[i]);
				edge[j][from[i] - 1].addDom(to[i], to[i]);
			}
			// create sub-circuit for each destination
			store.impose(new Subcircuit(edge[j]));
		}

		
		//Anger vilka edges som används oberoende av direction
		//lägger ihop flera subcircuits, delad kostnad
		for (int j = 0; j < n_edges; j++) {
			ArrayList<PrimitiveConstraint> list = new ArrayList<PrimitiveConstraint>();
			for (int i = 0; i < n_dests; i++) {
				list.add(new XeqC(edge[i][from[j] - 1], to[j]));
				list.add(new XeqC(edge[i][to[j] - 1], from[j]));
			}
System.out.println("list["+ j+"]: " +list);
			store.impose(new Reified(new Or(list), used_edges[j]));
		}

	
		IntVar distanceCost = new IntVar(store, "cost", 0, sumCost(cost));
		//calc cost w/ used path, store in distanceCost
		store.impose(new SumWeight(used_edges, cost, distanceCost));

		Search<IntVar> search = new DepthFirstSearch<IntVar>();
		SelectChoicePoint<IntVar> select = new SimpleMatrixSelect<IntVar>(edge, new IndomainMin<IntVar>());

		boolean result = search.labeling(store, select, distanceCost); // Minimize distanceCost
		for (int j = 0; j < n_edges; j++) {
		System.out.println(used_edges[j]);
		}
		// Print result
		if (result) {
			for (IntVar[] e : edge) {

				for (int i = 0; i < e.length; i++) {
					System.out.print(e[i] + "\t");
				}
				System.out.println("\n");
			}

			System.out.println(distanceCost);

		} else
			System.out.println("**** No Solution");

	}

	public static int sumCost(int[]cost) {
		int sum = 0;
		for (int i = 0; i < cost.length; i++) {
			sum+=cost[i];
		}
		System.out.println("sum: "+sum);
		return sum;
	}
	
	public static class Test {

		int graph_size;
		int start;
		int n_dests;
		int n_edges;
		int[] dest;
		int[] from;
		int[] to;
		int[] cost;

		public Test(int n) {
			switch (n) {
			case 1:
				int[] dest1 = { 6 };
				int[] from1 = { 1, 1, 2, 2, 3, 4, 4 };
				int[] to1 = { 2, 3, 3, 4, 5, 5, 6 };
				int[] cost1 = { 4, 2, 5, 10, 3, 4, 11 };
				setParameters(6, 1, 1, 7, dest1, from1, to1, cost1);
				break;
			case 2:
				int[] dest2 = { 5, 6 };
				int[] from2 = { 1, 1, 2, 2, 3, 4, 4 };
				int[] to2 = { 2, 3, 3, 4, 5, 5, 6 };
				int[] cost2 = { 4, 2, 5, 10, 3, 4, 11 };
				setParameters(6, 1, 2, 7, dest2, from2, to2, cost2);
				break;
			case 3:
				int[] dest3 = { 5, 6 };
				int[] from3 = { 1, 1, 1, 2, 2, 3, 3, 3, 4 };
				int[] to3 = { 2, 3, 4, 3, 5, 4, 5, 6, 6 };
				int[] cost3 = { 6, 1, 5, 5, 3, 5, 6, 4, 2 };
				setParameters(6, 1, 2, 9, dest3, from3, to3, cost3);
				break;
			}
		}

		void setParameters(int graph_size, int start, int n_dests, int n_edges, int[] dest, int[] from, int[] to,
				int[] cost) {
			this.from = from;
			this.to = to;
			this.cost = cost;
			this.dest = dest;
			this.graph_size = graph_size;
			this.start = start;
			this.n_dests = n_dests;
			this.n_edges = n_edges;
		}

	}

}
