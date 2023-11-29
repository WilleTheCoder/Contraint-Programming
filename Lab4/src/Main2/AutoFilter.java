package Main2;

import org.jacop.constraints.Diff2;
import org.jacop.constraints.XplusYeqZ;
import org.jacop.core.IntVar;
import org.jacop.core.Store;
import org.jacop.constraints.*;
import org.jacop.search.*;

public class AutoFilter {

	public static void main(String[] args) {
		TestCase t = new TestCase(1);
		Auto(t.del_add, t.del_mul, t.number_add, t.number_mul, t.n, t.add, t.mul, t.dependencies);

	}

	public static void Auto(int delay_add, int delay_mul, int n_add, int n_mul, int n_tasks, int[] add, int[] mul,
			int[][] dep) {

		Store store = new Store();

		int high_val = 1000;
System.out.println(delay_mul);
		IntVar[] startX = new IntVar[n_tasks];
		IntVar[] startY = new IntVar[n_tasks];
		IntVar[] lx = new IntVar[n_tasks];
		IntVar[] ly = new IntVar[n_tasks];

		for (int i = 0; i < n_tasks; i++) {
			startX[i] = new IntVar(store, "X", 0, high_val);
			ly[i] = new IntVar(store, "ly", 1, 1);
		}

		for (int i = 0; i < add.length; i++) {
			startY[add[i] - 1] = new IntVar(store, "Y", 1, n_add);
			lx[add[i] - 1] = new IntVar(store, "lx", delay_add, delay_add);
		
		}

		for (int i = 0; i < mul.length; i++) {
			startY[mul[i] - 1] = new IntVar(store, "Y", 1, n_mul);
			lx[mul[i] - 1] = new IntVar(store, "lx", delay_mul, delay_mul);
		}

		for (IntVar f : ly) {
			System.out.println(f);
		}
		// Constraints

		
		IntVar[] end_tot = new IntVar[n_tasks];
	for (int i = 0; i < n_tasks; i++) {
		end_tot[i] = new IntVar(store, "end_tot", 0, high_val);
		store.impose(new XplusYeqZ(startX[i], lx[i], end_tot[i]));
	}

		for (int i = 0; i < dep.length; i++) 
			for (int dep1 : dep[i]) {
				
				store.impose(new XgteqY(end_tot[dep1-1],startX[i]));
			}
		
		
		
		

		
		
		
		for (IntVar i : end_tot) {
			System.out.println(i);
		}
		
		store.impose(new Diff2(startX, startY, lx, ly));
		IntVar cost = new IntVar(store, "cost", 0, high_val);
		store.impose(new Max(end_tot, cost));

		

		Search<IntVar> search = new DepthFirstSearch<>();
		SelectChoicePoint<IntVar> select = new SimpleSelect<>(startX, new SmallestDomain<>(), new IndomainMin<>());

		if (search.labeling(store, select, cost)) {
			System.out.println("\n*** Found solution.");
			System.out.println("Solution cost is: " + cost.value());
		} else {
			System.out.println("\n*** No solution found.");
		}

	}


	public static class TestCase {

		int del_add = 1;
		int del_mul = 2;

		int number_add = 1;
		int number_mul = 1;
		int n = 28;

		int[] last = {27,28};

		int[] add = {9,10,11,12,13,14,19,20,25,26,27,28};

		int[] mul = {1,2,3,4,5,6,7,8,15,16,17,18,21,22,23,24};

		int[][] dependencies = {
		{9},
		{9},
		{10},
		{10},
		{11},
		{11},
		{12},
		{12},
		{27},
		{28},
		{13},
		{14},
		{16,17},
		{15,18},
		{19},
		{19},
		{20},
		{20},
		{22,23},
		{21,24},
		{25},
		{25},
		{26},
		{26},
		{27},
		{28},
		{},
		{},
		};

		public TestCase(int n) {
			switch (n) {
			case 1:
				number_add = 1;
				number_mul = 1;
				break;
			case 2:
				number_add = 1;
				number_mul = 2;
				break;
			case 3:
				number_add = 1;
				number_mul = 3;
				break;
			case 4:
				number_add = 2;
				number_mul = 2;
				break;
			case 5:
				number_add = 2;
				number_mul = 3;
				break;
			case 6:
				number_add = 2;
				number_mul = 4;
				break;

			}
		}

	}
}
