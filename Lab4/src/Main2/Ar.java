package Main2;

import org.jacop.constraints.Cumulative;
import org.jacop.constraints.Diff2;
import org.jacop.constraints.Max;
import org.jacop.constraints.XeqY;
import org.jacop.constraints.XgteqY;
import org.jacop.constraints.XplusYeqZ;
import org.jacop.core.IntVar;
import org.jacop.core.Store;
import org.jacop.search.DepthFirstSearch;
import org.jacop.search.IndomainMin;
import org.jacop.search.MostConstrainedStatic;
import org.jacop.search.PrintOutListener;
import org.jacop.search.Search;
import org.jacop.search.SelectChoicePoint;
import org.jacop.search.SimpleSelect;

public class Ar {
	public static void main(String[] args) {
		long T1, T2, T;
		T1 = System.currentTimeMillis();
		// auto_reg(new ar_1_1());
		// auto_reg(new ar_1_2());
		auto_reg(new ar_2_4());
		// auto_reg(new ar_2_2());
		// auto_reg(new ar_2_3());
		// auto_reg(new ar_2_4());
		T2 = System.currentTimeMillis();
		T = T2 - T1;
		System.out.println("\n\t***	Execution time = " + T + " ms");
	}

	public static void auto_reg(Data data) {
		Store store = new Store();

		int n = data.n;
		int del_add = data.del_add;
		int del_mul = data.del_mul;
		int num_add = data.number_add;
		int num_mul = data.number_mul;

		int[] add = data.add;
		int[] mul = data.mul;
		int[][] dep = data.dependencies;

		IntVar[] o1 = new IntVar[n];
		IntVar[] o2 = new IntVar[n];
		IntVar[] l1 = new IntVar[n];
		IntVar[] l2 = new IntVar[n];

//		IntVar[] startsA = new IntVar[add.length];
//		IntVar[] durA = new IntVar[add.length];
//		IntVar[] resA = new IntVar[add.length];
//
//		IntVar[] startsM = new IntVar[mul.length];
//		IntVar[] durM = new IntVar[mul.length];
//		IntVar[] resM = new IntVar[mul.length];
		
		
		

		for (int i = 0; i < n; i++) {
			o1[i] = new IntVar(store, 0, 50);
			l2[i] = new IntVar(store, 1, 1);
		}
		for (int i = 0; i < add.length; i++) {
			o2[add[i] - 1] = new IntVar(store, 0, num_add - 1);
			l1[add[i] - 1] = new IntVar(store, del_add, del_add);

		//	startsA[i] = new IntVar(store, 0, 50);
		//	store.impose(new XeqY(o1[add[i] - 1], startsA[i]));
			//durA[i] = new IntVar(store, del_add, del_add);
			//resA[i] = new IntVar(store, 1, 1);
		}

		for (int i = 0; i < mul.length; i++) {
			o2[mul[i] - 1] = new IntVar(store, num_add, num_add + num_mul - 1);
			l1[mul[i] - 1] = new IntVar(store, del_mul, del_mul);

		//	startsM[i] = new IntVar(store, 0, 50);
			//store.impose(new XeqY(o1[mul[i] - 1], startsM[i]));
		//	durM[i] = new IntVar(store, del_mul, del_mul);
		//	resM[i] = new IntVar(store, 1, 1);
		}

		for (int i = 0; i < dep.length; i++)
			for (int j = 0; j < dep[i].length; j++) {
				IntVar end = new IntVar(store, 0, 50);
				store.impose(new XplusYeqZ(o1[dep[i][j]], l1[dep[i][j]], end));
				store.impose(new XgteqY(o1[i], end));
			}

		IntVar[] len = new IntVar[n];
		for (int i = 0; i < n; i++) {
			len[i] = new IntVar(store, 0, 50);
			store.impose(new XplusYeqZ(o1[i], l1[i], len[i]));
		}

		IntVar max = new IntVar(store, 0, 50);
		store.impose(new Max(len, max));

		store.impose(new Diff2(o1, o2, l1, l2));

		
		
		
		
		
		
	//	store.impose(new Cumulative(startsA, durA, resA, new IntVar(store, 0, num_add)));
	//	store.impose(new Cumulative(startsM, durM, resM, new IntVar(store, 0, num_mul)));

		
		
		
		
		
		
		
		
		
		
		
		
		
		// search
		Search<IntVar> search = new DepthFirstSearch<IntVar>();
		SelectChoicePoint<IntVar> select = new SimpleSelect<IntVar>(o1, new MostConstrainedStatic<IntVar>(),
				new IndomainMin<IntVar>());

		search.setSolutionListener(new PrintOutListener<IntVar>());
		boolean Result = search.labeling(store, select, max);
		if (Result)
			// System.out.println(store);
			System.out.println("\n***Yes");
		System.out.println("Time: " + max + " ms");
		// System.out.println("all:");

	}
}

