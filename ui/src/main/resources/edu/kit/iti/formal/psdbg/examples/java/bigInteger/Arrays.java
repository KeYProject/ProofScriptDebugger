package java.util;

public class Arrays {
    /*@ public normal_behavior
      @ requires 0 <= from && from <= original.length && from <= to && original != null;
      @ ensures \result != original;                                    // always returns a new array
      @ ensures \result.length == to - from;
      @ ensures (to <= original.length)
      @      ==> (\forall int i; from <= i && i < to; \result[i - from] == original[i]);
      @ ensures (to > original.length)
      @      ==> ((\forall int i; from <= i && i < original.length; \result[i - from] == original[i])
      @          && (\forall int i; original.length <= i && i < to; \result[i - from] == 0));
      @ assignable \nothing;
      @
      @ also
      @
      @ public exceptional_behavior
      @ requires from < 0 || from > original.length || from > to || original == null;
      @ signals (IllegalArgumentException e) from > to;
      @ signals (ArrayIndexOutOfBoundsException e) from < 0 || from > original.length;
      @ signals (NullPointerException e) original == null;
      @ signals_only IllegalArgumentException, ArrayIndexOutOfBoundsException, NullPointerException;
      @ assignable \nothing;
      @*/
    public static int[] copyOfRange(int[] original, int from, int to) {
        int newLength = to - from;
        if (newLength < 0)
            throw new IllegalArgumentException(from + " > " + to);
        int[] copy = new int[newLength];
        System.arraycopy(original, from, copy, 0,
                         Math.min(original.length - from, newLength));
        return copy;
    }
}
