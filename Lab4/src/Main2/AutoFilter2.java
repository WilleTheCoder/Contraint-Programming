package Main2;

import org.jacop.constraints.Diff2;
import org.jacop.constraints.XplusYeqZ;
import org.jacop.core.IntVar;
import org.jacop.core.Store;
import org.jacop.constraints.*;
import org.jacop.search.*;

public class AutoFilter2 {

	public static void main(String[] args) {
		TestCase t = new TestCase(2);
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
			startY[add[i] - 1] = new IntVar(store, "Y", 0, n_add);
			lx[add[i] - 1] = new IntVar(store, "lx", delay_add, delay_add);

		}

		for (int i = 0; i < mul.length; i++) {
			startY[mul[i] - 1] = new IntVar(store, "Y", 0, n_mul);
			lx[mul[i] - 1] = new IntVar(store, "lx", delay_mul, delay_mul);
		}

		for (IntVar f : startY) {
			System.out.println(f);
		}
		// Constraints

		IntVar[] end_tot = new IntVar[n_tasks];

		for (int i = 0; i < n_tasks; i++) {
			for (int j = 0; j < dep[i].length; j++) {
				System.out.println("iterating indexes: " + i + " : " + j);
				IntVar end = new IntVar(store, "end", 0, high_val);
				int index = dep[i][j];
				System.out.println("dependancy index given: " + index);
				store.impose(new XplusYeqZ(startX[i], lx[i], end));
				store.impose(new XgteqY(startX[index-1], end));
			}
		}
		
		for (int i = 0; i < n_tasks; i++) {
			end_tot[i] = new IntVar(store, "end_tot", 0, high_val);
			store.impose(new XplusYeqZ(startX[i], lx[i], end_tot[i]));
		}

		IntVar cost = new IntVar(store, "cost", 0, high_val);

		for (IntVar i : end_tot) {
			System.out.println(i);
		}

		store.impose(new Max(end_tot, cost));

		store.impose(new Diff2(startX, startY, lx, ly));

		Search<IntVar> search = new DepthFirstSearch<>();
		SelectChoicePoint<IntVar> select = new SimpleSelect<>(end_tot, new SmallestDomain<>(), new IndomainMin<>());

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

		int number_add;
		int number_mul;
		int n = 4;

		int[] add = { 1, 3 };

		int[] mul = { 2, 4 };

		int[][] dependencies = { { 2 }, { 4 }, {}, {} };

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
