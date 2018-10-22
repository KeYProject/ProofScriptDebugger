package edu.kit.iti.formal.psdbg.examples.java.sumPositive;

public class SumPositive{

/*@ public normal_behaviour
  @ requires a != null;
  @ ensures \result >= 0;
  @*/
	public int sumPositiveArray(int[] a) {
		int sum = 0;

        /*@
          @ loop_invariant 0 <= i && i <= a.length;
          @ loop_invariant \old(sum) <= sum;
          @ decreases a.length - i;
          @ assignable sum;
          @*/
		for (int i = 0; i < a.length; i++) {
			if(a[i] >= 0) {
				sum += a[i];
			} 
		}

		return sum;

	}



}