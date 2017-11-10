class Bubblesort {
	
	/*@
	@	public normal_behaviour
	@	requires arr != null;
	@	requires arr.length > 0;
	@	ensures (\forall int i, j; 0 <= i && i < j && j < arr.length; arr[i] <= arr[j]);
	@	assignable arr[*];
	@*/
	
    public int[] bubbleSort(int[] arr) {
        int temp = 0;
		/*@
			@	loop_invariant 0 <= i && i <= arr.length;
			@	loop_invariant (\forall int a,b; a >= arr.length - i && b <= arr.length && a < b && i > 0; arr[a] <= arr[b]);
			@	assignable arr[*], temp;
			@	decreases arr.length - i;
			@*/
         for(int i=0; i < arr.length; i++){  
			/*@
			@	loop_invariant 1 <= j && j <= arr.length - i + 1;
			@	assignable arr[*], temp;
			@	decreases arr.length - i - j;
			@*/
                 for(int j=1; j < (arr.length-i); j++){  
                          if(arr[j-1] > arr[j]){  
                                 //swap elements  
                                 temp = arr[j-1];  
                                 arr[j-1] = arr[j];  
                                 arr[j] = temp;  
                         }  
                          
                 }  
         }  
		return arr;
	}
	

	/*@ public normal_behaviour
	@	requires arr != null;
	@	requires arr.length > 0;
	@	ensures \result == true;
	@*/
	public boolean test(int[] arr) {
		arr = bubbleSort(arr);
				for(int i = 0; i < arr.length - 1; i++) {
			if(arr[i] > arr[i+1]) {
				return false;
			} 
		}
		return true;
	}
	
}