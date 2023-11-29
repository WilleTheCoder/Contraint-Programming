package Main2;

import org.jacop.constraints.Diff2;
import org.jacop.constraints.XplusYeqZ;
import org.jacop.core.IntVar;
import org.jacop.core.Store;
import org.jacop.constraints.*;
import org.jacop.search.*;

public class AutoFilter3 {

	public static void main(String[] args) {
		TestCase t = new TestCase(1);
		Auto(t.del_add, t.del_mul, t.number_add, t.number_mul, t.n, t.add, t.mul, t.dependencies);

	}

	public static void Auto(int delay_add, int delay_mul, int n_add, int n_mul, int n_tasks, int[] add, int[] mul,
			int[][] dep) {

		Store store = new Store();
int add_l = add.length;
int mul_l = mul.length;

		int high_val = 1000;
System.out.println(delay_mul);
		IntVar[] add_startX = new IntVar[add_l];
		IntVar[] add_startY = new IntVar[add_l];
		IntVar[] add_lx = new IntVar[add_l];
		IntVar[] add_ly = new IntVar[add_l];
		
		IntVar[] mul_startX = new IntVar[mul_l];
		IntVar[] mul_startY = new IntVar[mul_l];
		IntVar[] mul_lx = new IntVar[mul_l];
		IntVar[] mul_ly = new IntVar[mul_l];
		
		IntVar[] allOps = new IntVar[n_tasks];
		
	

		for (int i = 0; i < add.length; i++) {
			add_startX[i] = new IntVar(store, "X", 0, high_val);
			add_ly[i] = new IntVar(store, "ly", 1, 1);
			
			add_startY[i] = new IntVar(store, "Y", 1, n_add);
			add_lx[i] = new IntVar(store, "lx", delay_add, delay_add);
			allOps[add[i]-1] = new IntVar(store,"add", 0, high_val);
		}

		for (int i = 0; i < mul.length; i++) {
			mul_startX[i] = new IntVar(store, "X", 0, high_val);
			mul_ly[i] = new IntVar(store, "ly", 1, 1);	
			
			mul_startY[i] = new IntVar(store, "Y", 1, n_mul);
			mul_lx[i] = new IntVar(store, "lx", delay_mul, delay_mul);
			allOps[mul[i]-1] = new IntVar(store, "mul",0, high_val);
		}
		
		for (IntVar i : add_startY) {
			System.out.println(i);
		}


		// Constraints

		
		IntVar[] end_tot = new IntVar[n_tasks];
	for (int i = 0; i < n_tasks; i++) {
		end_tot[i] = new IntVar(store, "end_tot", 0, high_val);
		
		if(allOps[i].id=="add") {
			store.impose(new XplusYeqZ(allOps[i], add_lx[0], end_tot[i]));
		}else {
			store.impose(new XplusYeqZ(allOps[i], mul_lx[0], end_tot[i]));
		}
		
	}

		for (int i = 0; i < dep.length; i++) 
			for (int dep1 : dep[i]) {
				
				store.impose(new XgteqY(end_tot[dep1-1],allOps[i]));
			}
		
		

		
		store.impose(new Diff2(add_startX, add_startY, add_lx, add_ly));
		store.impose(new Diff2(mul_startX, mul_startY, mul_lx, mul_ly));
		
		IntVar cost = new IntVar(store, "cost", 0, high_val);
		store.impose(new Max(end_tot, cost));

		

		Search<IntVar> search = new DepthFirstSearch<>();
		SelectChoicePoint<IntVar> select = new SimpleSelect<>(allOps, new SmallestDomain<>(), new IndomainMin<>());

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
