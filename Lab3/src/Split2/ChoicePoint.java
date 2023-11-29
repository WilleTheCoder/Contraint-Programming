package Split2;

import org.jacop.constraints.PrimitiveConstraint;
import org.jacop.constraints.XgteqC;
import org.jacop.core.IntVar;
import org.jacop.core.Store;

public class ChoicePoint {

	IntVar var;
	IntVar[] searchVariables;
	int value;
	Store store;

	public ChoicePoint(Store store, IntVar[] v) {
		var = selectVariable(v);
		value = selectValue(var);
		this.store = store;
	}

	public IntVar[] getSearchVariables() {
		return searchVariables;
	}

	/**
	 * example variable selection; input order
	 */
	IntVar selectVariable(IntVar[] v) {
		if (v.length != 0) {
			//return inputOrder(v);
			//return firstFail(v);
			//return mostConstrained(v);
			//return smallestOption(v);
			//return largestOption(v);
			return smallMaxOption(v);
		} else {
			System.err.println("Zero length list of variables for labeling");
			return new IntVar(store);
		}
	}

	//VARIABLE SELECTION
	
	//input order selection, as it is defined
	
	//Total number of visited nodes = 701141
	//Total number of failed nodes = 350570
	public IntVar inputOrder(IntVar[] v) {
		if (v[0].min() == v[0].max()) { // only one domain, found a solution
			searchVariables = new IntVar[v.length - 1];
			for (int i = 0; i < v.length - 1; i++) {
				searchVariables[i] = v[i + 1];
			}

		} else {
			searchVariables = v;
		}

		return v[0];
	}

	
	//select first fail, fewest nbr of elements in its domain
	
	//Total number of visited nodes = 1630027
	//Total number of failed nodes = 815013
	public IntVar firstFail(IntVar[] v) {
		IntVar fewestDom = null;
		int index = 0;

		for (int i = 0; i < v.length; i++) {
	
			if (fewestDom == null || (calcDomain(v[i]) < calcDomain(fewestDom))) {
				fewestDom = v[i];
				index = i;
			}
		}

		if (fewestDom.min() == fewestDom.max()) { // only one domain, found a solution
			searchVariables = new IntVar[v.length - 1];
			int a=0;
			for (int i = 0; i < v.length - 1; i++) {
				if (i == index) {
					a++;
				} 
				
				searchVariables[i] = v[a];
				a++;
			}

		} else {
			searchVariables = v;
		}

		return fewestDom;

	}

	
	//most constrained selection, connected to most constraints.
	
	//Total number of visited nodes = 1625439
	//Total number of failed nodes = 812719
	public IntVar mostConstrained(IntVar[] v) {
		
		IntVar mostCon = null;
		int index = 0;
		for (int i = 0; i < v.length; i++) {
			
			if(mostCon == null || v[i].sizeConstraints() > mostCon.sizeConstraints()) {
				mostCon=v[i];
				index = i;
			}
		}
		
		
		if (mostCon.min() == mostCon.max()) { // only one domain, found a solution
			searchVariables = new IntVar[v.length - 1];
			int a=0;
			for (int i = 0; i < v.length - 1; i++) {
				if (i == index) {
					a++;
				} 
				
				searchVariables[i] = v[a];
				a++;
			}

		} else {
			searchVariables = v;
		}

		return mostCon;
	}

	
	//smallest minimum value in its domain
	
	//Total number of visited nodes = 701141
	//Total number of failed nodes = 350570
	public IntVar smallestOption(IntVar[] v) {
		
		IntVar smallestDom = null;
		int index = 0;
		for (int i = 0; i < v.length; i++) {
			
			if(smallestDom == null || v[i].min() < smallestDom.min()) {
				smallestDom=v[i];
				index = i;
			}
		}
		
		
		if (smallestDom.min() == smallestDom.max()) { // only one domain, found a solution
			searchVariables = new IntVar[v.length - 1];
			int a=0;
			for (int i = 0; i < v.length - 1; i++) {
				if (i == index) {
					a++;
				} 
				
				searchVariables[i] = v[a];
				a++;
			}

		} else {
			searchVariables = v;
		}

		return smallestDom;
	}
	
	
	//largest minimum value in its domain
	
	//Total number of visited nodes = 178937
	//Total number of failed nodes = 89468
	public IntVar largestOption(IntVar[] v) {
		
		IntVar greatestDom = null;
		int index = 0;
		for (int i = 0; i < v.length; i++) {
			
			if(greatestDom == null || v[i].min() > greatestDom.min()) {
				greatestDom=v[i];
				index = i;
			}
		}
		
		
		if (greatestDom.min() == greatestDom.max()) { // only one domain, found a solution
			searchVariables = new IntVar[v.length - 1];
			int a=0;
			for (int i = 0; i < v.length - 1; i++) {
				if (i == index) {
					a++;
				} 
				
				searchVariables[i] = v[a];
				a++;
			}

		} else {
			searchVariables = v;
		}

		return greatestDom;
	}
	
	
	//smallest maximal value in its domain

	//Total number of visited nodes = 701141
	//Total number of failed nodes = 350570
	public IntVar smallMaxOption(IntVar[] v) {
		
		IntVar smallMaxDom = null;
		int index = 0;
		for (int i = 0; i < v.length; i++) {
			
			if(smallMaxDom == null || v[i].max() < smallMaxDom.max()) {
				smallMaxDom=v[i];
				index = i;
			}
		}
		
		
		if (smallMaxDom.min() == smallMaxDom.max()) { // only one domain, found a solution
			searchVariables = new IntVar[v.length - 1];
			int a=0;
			for (int i = 0; i < v.length - 1; i++) {
				if (i == index) {
					a++;
				} 
				
				searchVariables[i] = v[a];
				a++;
			}

		} else {
			searchVariables = v;
		}

		return smallMaxDom;
	}

	
	
	int calcDomain(IntVar v) {
	return v.max() - v.min();
	}

	/**
	 * example value selection; split_mid
	 */
	int selectValue(IntVar v) {
//		System.out.println("v-min: "+v.min());
//		System.out.println("v-max: "+v.max());
//		System.out.println("calc: "+ (v.min() + v.max()) / 2);
//		System.out.println();
		return ((v.min() + v.max() + 1) / 2); // avrundning, ex 35 & 36
	}

	/**
	 * example constraint assigning a selected value
	 */
	public PrimitiveConstraint getConstraint() {
		return new XgteqC(var, value); // var >_ value
	}
}