public class MaxTriplet {

  /*@ public normal_behavior
      @ requires true;
      @ ensures \result >= a && \result >= b && \result >= c;
      @*/


	public int getMax(int a, int b, int c) {
		int max;
		if(a >= b) {
			max = a;
		} else {
			max = b;
		}

		if(max <= c) {
			max = c;
		}

		return max;
	}

}