
public final class Simple {

    //@ ghost \seq c_seq;
    //@ ghost \seq b_seq;

    private /*@spec_public*/ static int[] barr;
    private /*@spec_public*/ static int[] carr;

    /*@ public normal_behavior
      @ requires carr.length == barr.length;
      @ requires aarr != barr && aarr != carr;
      @ //requires c_seq == \dl_array2seq(carr);
      @ //ensures \dl_array2seq(carr) == \dl_array2seq(barr);
      @ ensures \dl_seqPerm(\dl_array2seq(barr), \dl_array2seq(carr));
      @ assignable \everything;
     */
    public int[] transitive(int[] aarr){
        aarr = Simple.copyArray(carr); // a=c && perm a,c


        sort(aarr); //perm(a,c)
        // --> result0@aftercopyArray perm result0@aftersort

        barr = Simple.copyArray(aarr); //b = perm(a)
        //carr@aftercopy2 = carr@aftercopy1
        //carr@aftersort = carr@aftercopy2
        //result0@aftersort = result1@aftercopyarray2
        //barr@aftercopyarray2 = result0@aftersort


        /*@ //set b_seq =  \dl_array2seq(barr);@*/

        return barr;
    }

    /*@ public normal_behavior
      @ ensures \dl_seqPerm(\dl_array2seq(a), \old(\dl_array2seq(a)));
      @ assignable a[*];
      */
    public abstract void sort(int[] a);

    /*@ public normal_behavior
      @ //ensures \dl_array2seq(input) == \dl_array2seq(\result);
      @ // ensures (\forall int i; 0 <= i < input.length; input[i] == \result[i]) && \result.length == input.length;
      @ ensures \fresh(\result);
      @ ensures \dl_seqPerm(\dl_array2seq(\result), \dl_array2seq(input));
      @ assignable \nothing;
      */
    public /*@helper@*/ static int[] copyArray(int[] input){
        int[] temp = new int[input.length];
        /*@ loop_invariant 0 <= i && i <= temp.length && (\forall int j; 0 <= j < i; temp[j] == input[j]) ;
          @ assignable temp[*];
          @ decreases temp.length-i;
          */
        for (int i = 0; i < input.length; i++) {
            temp[i] = input[i];
        }
        return temp;

    }


//    /*@ public normal_behavior
//      @ requires c.length == b.length;
//      @ ensures  \dl_seqPerm(\dl_array2seq(\result), \dl_array2seq(c));
//      @ assignable b[*];
//     */
//    public int[] foo(int[] a){
//        a = Simple.copyArray(c);
////hier weiß ich: \dl_array2seq(c) == \dl_array2seq(a);
//
//        /*@ loop_invariant 0 <= i && i <= a.length && (\forall int j; 0 <= j < i; a[j] == b[j]) ;
//          @ assignable b[*];
//          @ decreases a.length -i;
//          */
//        for (int i = 0; i < a.length; i++) {
//            b[i]= a[i];
//        }
//        //hier sollte ich wissen a = b
//        sort(a);
//        //hier sollte ich wissen permutation a,b
//        return b;
//    }




    /*@ public normal_behavior
      @ requires a[0] >= 0;
      @ ensures \result > 0;
      @ assignable \nothing;
     */
    public int getter(int[] a){
        int test = a[0];
        a[0] = a[1];
        a[1] = test;
        int ret = test+1;
        return test;
    }
    /*@ public normal_behavior
      @ requires a[1] >= 0;
      @ ensures \result > 0;
      @ assignable \nothing;
     */
    public int getter2(int[] a){
         int test = a[1];
        a[1] = a[0];
        a[0] = test;
        int ret = test+1;
        return test;
    }
//    /*@ public invariant log != null && log.length == 100; @*/
//    public int[] log = new int[100];
//
//    /*@ public invariant n >= 0; @*/
//    public int n;
//
//  /*@ public normal_behavior
//  @	requires log != null;
//  @	ensures n == 0 && log != null;
//  @*/
//
//    public void init() {
//        for (int i = 0; i < log.length; i++) {
//            log[i] = 0;
//        }
//        n = 0;
//    }
//
//    /*@ public normal_behavior
//      @	requires n < 100;
//      @	ensures log[n] == x+y;
//      @ assignable log.*;
//      @*/
//    public void logging(int x, int y) {
//        if (n < 100) {
//            log[n] = x + y;
//        }
//
//    }
//
//    /*@ public normal_behavior
//      @	requires x >= 0 && y >= 0 && n <100;
//      @	ensures \result >=0;
//      @ assignable log.*;
//      @*/
//    public int example(int x, int y) {
//
//
//        if (log[n] == 0) {
//            logging(x, y);
//            return x + y;
//        } else {
//            return x + y;
//        }
//
//    }
//    /*@ public normal_behavior
//      @	requires a != null && b != null && (\forall int i; 0 <= i < a.length; a[i] != b[i]);
//      @	ensures (\forall int i; 0 <= i < a.length; a[i] != b[i]) && \old(a) == \result;
//      @*/
//
//    public int[] compare(int[] a, int[] b){
//
//        return a;
//    }

}