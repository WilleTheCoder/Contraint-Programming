package Split2;
/**
 *  SimpleDFS.java 
 *  This file is part of JaCoP.
 *
 *  JaCoP is a Java Constraint Programming solver. 
 *	
 *	Copyright (C) 2000-2015 Krzysztof Kuchcinski and Radoslaw Szymanek
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *  
 *  Notwithstanding any other provision of this License, the copyright
 *  owners of this work supplement the terms of this License with terms
 *  prohibiting misrepresentation of the origin of this work and requiring
 *  that modified versions of this work be marked in reasonable ways as
 *  different from the original version. This supplement of the license
 *  terms is in accordance with Section 7 of GNU Affero General Public
 *  License version 3.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

import org.jacop.constraints.Not;
import org.jacop.core.FailException;
import org.jacop.core.IntDomain;
import org.jacop.core.IntVar;
import org.jacop.core.Store;


public class Split2  {

    boolean trace = false;

    /**
     * Store used in search
     */
    Store store;

    /**
     * Defines varibales to be printed when solution is found
     */
    IntVar[] variablesToReport;

    /**
     * It represents current depth of store used in search.
     */
    int depth = 0;

    /**
     * It represents the cost value of currently best solution for FloatVar cost.
     */
    public int costValue = IntDomain.MaxInt;

    /**
     * It represents the cost variable.
     */
    public IntVar costVariable = null;

    /*
     * Number of visited nodes
     */
    public long N=0;

    /*
     * Number of failed nodes excluding leave nodes
     */
    public long failedNodes = 0;

    public Split2(Store s) {
	store = s;
    }


    /**
     * This function is called recursively to assign variables one by one.
     */
    public boolean label(IntVar[] vars) {
	N++;
	
	if (trace) {
	    for (int i = 0; i < vars.length; i++) 
		System.out.print (vars[i] + " ");
	    System.out.println ();
	}

	ChoicePoint choice = null;
	boolean consistent;

	// Instead of imposing constraint just restrict bounds
	// -1 since costValue is the cost of last solution
	//hitta nya lösningar med lägre kostnad.
	if (costVariable != null) {
	    try {
		if (costVariable.min() <= costValue - 1)
			//costVariable < costValue;
		    costVariable.domain.in(store.level, costVariable, costVariable.min(), costValue - 1);
		else
		    return false;
	    } catch (FailException f) {
		return false;
	    }
	}

	//check store for consistency, prune domains
		//possible detect failure
	consistent = store.consistency();
		
	if (!consistent) {
	    // Failed leaf of the search tree
	    return false;
	} else { // consistent

	    if (vars.length == 0) {
		// solution found; no more variables to label

		// update cost if minimization
		if (costVariable != null)
		    costValue = costVariable.min();

		reportSolution();

		return costVariable == null; // true is satisfiability search and false if minimization
	    }

 	    choice = new ChoicePoint(store,vars);

	    levelUp();

	    store.impose(choice.getConstraint());

	    // choice point imposed.
			
	    consistent = label(choice.getSearchVariables());

            if (consistent) {
		levelDown();
		return true;
	    } else {
		failedNodes++;

		restoreLevel(); //restore store back to state at levelUp();

		store.impose(new Not(choice.getConstraint()));

		// negated choice point imposed.
		
		consistent = label(vars);

		levelDown();

		if (consistent) {
		    return true;
		} else {
		    return false;
		}
	    }
	}
    }

    void levelDown() {
	store.removeLevel(depth);
	store.setLevel(--depth);
    }

    void levelUp() {
	store.setLevel(++depth);
    }

    void restoreLevel() {
	store.removeLevel(depth);
	store.setLevel(store.level);
    }

    public void reportSolution() {
	System.out.println("Nodes visited: " + N);

	if (costVariable != null)
	    System.out.println ("Cost is " + costVariable);

	for (int i = 0; i < variablesToReport.length; i++) 
	    System.out.print (variablesToReport[i] + " ");
	System.out.println ("\n---------------");
    }

    public void setVariablesToReport(IntVar[] v) {
	variablesToReport = v;
    }

    public void setCostVariable(IntVar v) {
	costVariable = v;
    }

}
