package java.math;

import java.util.Arrays;

public class BigInteger extends Number implements Comparable/*<BigInteger>*/ {
    /**
     * The signum of this BigInteger: -1 for negative, 0 for zero, or
     * 1 for positive.  Note that the BigInteger zero <i>must</i> have
     * a signum of 0.  This is necessary to ensures that there is exactly one
     * representation for each BigInteger value.
     *
     * @serial
     */
    final int signum;

    /**
     * The magnitude of this BigInteger, in <i>big-endian</i> order: the
     * zeroth element of this array is the most-significant int of the
     * magnitude.  The magnitude must be "minimal" in that the most-significant
     * int ({@code mag[0]}) must be non-zero.  This is necessary to
     * ensure that there is exactly one representation for each BigInteger
     * value.  Note that this implies that the BigInteger zero has a
     * zero-length mag array.
     */
    final int[] mag;

    //@ static invariant BigInteger.ZERO.<inv>;
    //@ static invariant BigInteger.ZERO.value == 0;

    //@ ghost \bigint value = 0;

    /*@ invariant value == calcValue(signum, mag);
      @ invariant signum == 0 || signum == 1 || signum == -1;
      @ invariant signum == 0 <==> mag.length == 0;
      @ invariant mag.length > 0 ==> mag[0] != 0;
      @ accessible \inv: signum, mag, mag[*], value;
      @*/

    // no valid invariant (after RuntimeException inv. has to hold)
    /*@ invariant mag.length < MAX_MAG_LENGTH || mag.length == MAX_MAG_LENGTH && mag[0] >= 0;
    */

    // returns the value of the input integer as if it was unsigned
    /*@ model_behavior
      @ ensures value == 0 ==> \result == 0;
      @ ensures value != 0 ==> \result > 0;
      @ ensures value > 0 ==> \result == value;
      @ ensures value < 0 ==> \result == value + 0x100000000L;
      @ ensures \result >= 0;
      @ ensures \result < 0x100000000L;
      @ ensures (\forall int i; value != i ==> \result != toUnsigned(i));    // injective function
      @ accessible \nothing;
      @ static helper model long toUnsigned(int value) {
      @     return (long)value & 0xffffffffL;
      @ }
      @*/

    // returns 2^(32*exp) = (2^(32))^exp
    /*@ model_behavior
      @ requires exp >= 0;
      @ ensures \result >= 1;
      @ ensures (\forall int i; 0 <= i && i < exp; twopower(i) < \result);   // strictly increasing
      @ measured_by exp;
      @ accessible \nothing;
      @ static helper model \bigint twopower(int exp) {
      @     return exp == 0 ? 1 : twopower(exp - 1) * 0x100000000L;
      @ }
      @*/
 
    // returns the value of the BigInteger as \bigint
    /*@ model_behavior
      @ requires m.length > 0 ==> m[0] != 0;
      @ requires s == 0 <==> m.length == 0;
      @ ensures \result
      @         == s*(\sum int i; 0 <= i && i < m.length; toUnsigned(m[i]) * twopower(m.length-i-1));
      @ ensures s < 0 ==> \result < 0;
      @ ensures s == 0 ==> \result == 0;
      @ ensures s > 0 ==> \result > 0;
      @ accessible m[*];    // m.length is a function in KeY, not a field, and thus not included here
      @ static helper model \bigint calcValue(int s, int[] m) {
      @     return s*(\sum int i; 0 <= i && i < m.length; toUnsigned(m[i]) * twopower(m.length-i-1));
      @ }
      @*/

    /**
     * This constant limits {@code mag.length} of BigIntegers to the supported
     * range.
     */
    private static final int MAX_MAG_LENGTH = Integer.MAX_VALUE / Integer.SIZE + 1; // (1 << 26)

    /**
     * This mask is used to obtain the value of an int as if it were unsigned.
     */
    final static long LONG_MASK = 0xffffffffL;

    /**
     * The BigInteger constant zero.
     *
     * @since   1.2
     */
    public static final BigInteger ZERO = new BigInteger(new int[0], 0);

    /**
     * This internal constructor differs from its public cousin
     * with the arguments reversed in two ways: it assumes that its
     * arguments are correct, and it doesn't copy the magnitude array.
     */
    /*@ helper normal_behavior 
      @ requires (   magnitude.length < MAX_MAG_LENGTH
      @           || magnitude.length == MAX_MAG_LENGTH && magnitude[0] >= 0)
      @          && (signum == 0 || signum == 1 || signum == -1)
      @          && (magnitude.length > 0 ==> magnitude[0] != 0)
      @          && (signum == 0 ==> magnitude.length == 0)
      @			 && magnitude != null;
      @	ensures \invariant_for(this);
      @ ensures this.mag == magnitude;
      @ ensures (magnitude.length == 0 ==> this.signum == 0) &&
      @         (magnitude.length != 0 ==> this.signum == signum);
      @ assignable \nothing;                                // special purity rules for constructors
      @
      @ also
      @
      @ exceptional_behavior
      @	requires magnitude != null;
      @ requires     magnitude.length > MAX_MAG_LENGTH
      @           || magnitude.length == MAX_MAG_LENGTH && magnitude[0] < 0;
      @ signals_only ArithmeticException;
      @ signals (ArithmeticException e) magnitude.length > MAX_MAG_LENGTH
      @                              || magnitude.length == MAX_MAG_LENGTH && magnitude[0] < 0;
      @ assignable \nothing;                                // special purity rules for constructors
      @*/
    BigInteger(int[] magnitude, int signum) {
        this.signum = (magnitude.length == 0 ? 0 : signum);
        this.mag = magnitude;
        if (mag.length >= MAX_MAG_LENGTH) {
            checkRange();
        }
        /*@ set value = calcValue(this.signum, this.mag); @*/
    }
    
    /**
     * Returns a BigInteger whose value is {@code (-this)}.
     *
     * @return {@code -this}
     */
    /*@public normal_behavior
      @ requires \static_invariant_for(BigInteger);
      @ ensures \result.value == (-1) * value;
      @ ensures \invariant_for(result);
      @assignable \nothing;
      @*/
    public BigInteger negate() {
        return new BigInteger(this.mag, -this.signum);
    }
    
    /**
     * Returns a BigInteger whose value is the absolute value of this
     * BigInteger.
     *
     * @return {@code abs(this)}
     */
    /*@public normal_behavior
      @ requires \static_invariant_for(BigInteger);
      @ ensures (value < 0) ==> (\result.value == (-1) * value);
      @ ensures (value >= 0) ==> (\result.value == value);
      @ ensures \invariant_for(result);
      @assignable \nothing;
      @*/
    public BigInteger abs() {
        return (signum >= 0 ? this : this.negate());
    }

    /**
     * Returns a BigInteger whose value is {@code (this + val)}.
     *
     * @param  val value to be added to this BigInteger.
     * @return {@code this + val}
     */
    /*@ public normal_behavior
      @ requires val.<inv> && \static_invariant_for(BigInteger);
      @ requires val.mag.length < MAX_MAG_LENGTH
      @       || val.mag.length == MAX_MAG_LENGTH && val.mag[0] >= 0;
      @ requires this.mag.length < MAX_MAG_LENGTH
      @       || this.mag.length == MAX_MAG_LENGTH && this.mag[0] >= 0;
      @ ensures \result.value == val.value + this.value;
      @ ensures \result.<inv>;
      @ assignable \nothing;
      @*/
    public BigInteger add(BigInteger val) {
        if (val.signum == 0)
            return this;
        if (signum == 0)
            return val;
        if (val.signum == signum)
            return new BigInteger(add(mag, val.mag), signum);

        int cmp = compareMagnitude(val);
        if (cmp == 0)
            return ZERO;
        int[] resultMag = (cmp > 0 ? subtract(mag, val.mag)
                           : subtract(val.mag, mag));
        resultMag = trustedStripLeadingZeroInts(resultMag);

        return new BigInteger(resultMag, cmp == signum ? 1 : -1);
    }

    /**
     * Adds the contents of the int arrays x and y. This method allocates
     * a new int array to hold the answer and returns a reference to that
     * array.
     */
    /*@ private normal_behavior
      @ requires \static_invariant_for(BigInteger);
      @ ensures simpleSum(\result) == simpleSum(x) + simpleSum(y);
      @ ensures \result.length <= (x.length > y.length ? x.length : y.length) + 1;
      @ assignable \nothing;
      @*/
    private static /*@ helper @*/ int[] add(int[] x, int[] y) {
        // If x is shorter, swap the two arrays
        if (x.length < y.length) {
            int[] tmp = x;
            x = y;
            y = tmp;
        }

        int xIndex = x.length;
        int yIndex = y.length;
        int result[] = new int[xIndex];
        long sum = 0;
        if (yIndex == 1) {
            sum = (x[--xIndex] & LONG_MASK) + (y[0] & LONG_MASK) ;
            result[xIndex] = (int)sum;
        } else {
            // Add common parts of both numbers
        	/*@ loop_invariant 0 <= yIndex && yIndex <= y.length
            @             && 0 <= xIndex && xIndex <= x.length
            @             && x.length - xIndex == y.length - yIndex
            @             && partialSum(result, xIndex)
            @                + (sum>>>32)*twopower(x.length-xIndex) // borrow from next digit
            @               == partialSum(x, xIndex)
            @                + partialSum(y, yIndex);
            @ assignable result[(result.length-y.length)..(result.length-1)];
            @ decreases yIndex;
            @*/
            while (yIndex > 0) {
            	
            	/*@ ensures result[xIndex - 1] == ((toUnsigned(x[xIndex - 1])
            	@ 	+ toUnsigned(y[yIndex - 1]) + (sum / twopower(1)))) % twopower(1);		 
            	@ assignable result[xIndex - 1];
            	@*/
            	{
                sum = (x[--xIndex] & LONG_MASK) +
                      (y[--yIndex] & LONG_MASK) + (sum >>> 32);
                result[xIndex] = (int)sum;
            	}
            }
        }
        // Copy remainder of longer number while carry propagation is required
        boolean carry = (sum >>> 32 != 0);
        /*@ loop_invariant 0 <= xIndex && xIndex <= x.length
        @                && partialSum(result, xIndex)
        @                 == partialSum(x, xIndex) + partialSum(y, 0)
        @                    + (carry ? twopower(x.length-xIndex) : (\bigint)0);
        @ assignable result[*];
        @ decreases xIndex;
        @*/
        while (xIndex > 0 && carry)
            carry = ((result[--xIndex] = x[xIndex] + 1) == 0);

        /*@ loop_invariant xIndex >= 0
        @             && partialSum(result, xIndex) == partialSum(x, xIndex)
        @                                              + partialSum(y, 0);
        @ assignable result[*];
        @ decreases xIndex;
        @*/
        while (xIndex > 0)
            result[--xIndex] = x[xIndex];

        // Grow result if necessary
        if (carry) {
            int bigger[] = new int[result.length + 1];
            System.arraycopy(result, 0, bigger, 1, result.length);
            bigger[0] = 0x01;
            return bigger;
        }
        return result;
    }

    // no assumptions for zeros, parameter and result may have arbitrary amount of leading zeros
    /*@ private static helper model \bigint simpleSum(int[] m) {
      @     return (\sum int i; 0 <= i && i < m.length; toUnsigned(m[i]) * twopower(m.length-i-1));
      @ }
      @*/

    // partial sum of the array, starting from startIndex (inclusive)
    /*@ private model_behavior
      @ accessible m[startIndex .. (m.length-1)];
      @ private static helper model \bigint partialSum(int[] m, int startIndex) {
      @     return (\sum int i;
      @                  startIndex <= i && i < m.length;
      @                  toUnsigned(m[i]) * twopower(m.length-i-1));
      @ }
      @*/

    /**
     * Subtracts the contents of the second int arrays (little) from the
     * first (big).  The first int array (big) must represent a larger number
     * than the second.  This method allocates the space necessary to hold the
     * answer.
     */
    /*@ private normal_behavior
      @ requires calcMagSum(big) > calcMagSum(little);
      @ requires (big.length > 0 ==> big[0] != 0) && (little.length > 0 ==> little[0] != 0);
      @ ensures simpleSum(\result) == simpleSum(big) - simpleSum(little);   // may have leading zeros
      @ assignable \nothing;
      @*/
    private static /*@ helper @*/ int[] subtract(int[] big, int[] little) {
        int bigIndex = big.length;
        int result[] = new int[bigIndex];
        int littleIndex = little.length;
        long difference = 0;

        // Subtract common parts of both numbers
        /*@ loop_invariant 0 <= littleIndex && littleIndex <= little.length
          @             && 0 <= bigIndex && bigIndex <= big.length
          @             && big.length - bigIndex == little.length - littleIndex
          @             && partialSum(result, bigIndex)
          @                + (difference>>32)*twopower(big.length-bigIndex) // borrow from next digit
          @               == partialSum(big, bigIndex)
          @                - partialSum(little, littleIndex);
          @ assignable result[(result.length-little.length)..(result.length-1)];
          @ decreases littleIndex;
          @*/
        while (littleIndex > 0) {
            difference = (big[--bigIndex] & LONG_MASK) -
                         (little[--littleIndex] & LONG_MASK) +
                         (difference >> 32);
            result[bigIndex] = (int)difference;
        }

        // Subtract remainder of longer number while borrow propagates
        boolean borrow = (difference >> 32 != 0);
        /*@ loop_invariant 0 <= bigIndex && bigIndex <= big.length
          @                && partialSum(result, bigIndex)
          @                 == partialSum(big, bigIndex) - partialSum(little, 0)
          @                    + (borrow ? twopower(big.length-bigIndex) : (\bigint)0);
          @ assignable result[*];
          @ decreases bigIndex;
          @*/
        while (bigIndex > 0 && borrow)
            borrow = ((result[--bigIndex] = big[bigIndex] - 1) == -1);
            /* --bigIndex;
             * result[bigIndex] = big[bigIndex] - 1;
             * borrow = (big[bigIndex] == 0);
             */

        // Copy remainder of longer number
        /*@ loop_invariant bigIndex >= 0
          @             && partialSum(result, bigIndex) == partialSum(big, bigIndex)
          @                                              - partialSum(little, 0);
          @ assignable result[*];
          @ decreases bigIndex;
          @*/
        while (bigIndex > 0)
            result[--bigIndex] = big[bigIndex];

        return result;
    }

    /**
     * Throws an {@code ArithmeticException} if the {@code BigInteger} would be
     * out of the supported range.
     *
     * @throws ArithmeticException if {@code this} exceeds the supported range.
     */
    /*@ private normal_behavior
      @ requires mag.length <= MAX_MAG_LENGTH
      @      && (mag.length != MAX_MAG_LENGTH || mag[0] >= 0);
      @ assignable \nothing;
      @
      @ also
      @ 
      @ private exceptional_behavior
      @ requires mag.length > MAX_MAG_LENGTH
      @       || mag.length == MAX_MAG_LENGTH && mag[0] < 0;
      @ signals (ArithmeticException e) mag.length > MAX_MAG_LENGTH
      @                              || mag.length == MAX_MAG_LENGTH && mag[0] < 0;
      @ signals_only ArithmeticException;
      @ assignable \nothing;
      @*/
    private void checkRange() {
        if (mag.length > MAX_MAG_LENGTH || mag.length == MAX_MAG_LENGTH && mag[0] < 0) {
            reportOverflow();
        }
    }

    /*@ private exceptional_behavior
      @ signals (ArithmeticException e) true;
      @ signals_only ArithmeticException;
      @ assignable \nothing;
      @*/
    private static /*@ helper @*/ void reportOverflow() {
        throw new ArithmeticException("BigInteger would overflow supported range");
    }

    /*@ private model_behavior
      @ requires m.length > 0 ==> m[0] != 0;
      @ ensures \result
      @         == (\sum int i; 0 <= i && i < m.length; toUnsigned(m[i]) * twopower(m.length-i-1));
      @ ensures m.length > 0 ==> \result > 0;
      @ ensures \result >= 0;
      @ ensures (\forall int[] m2;                  // A: a.length < b.length -> a.result < b.result
      @             0 <= m2.length && m2.length < m.length && (m2.length > 0 ==> m2[0] != 0);
      @             calcMagSum(m2) < \result);
      @ ensures (\forall int[] m3;                  // B: equal lengths and elements -> equal results
      @             m3.length == m.length && (\forall int j; 0 <= j && j < m.length; m3[j] == m[j]);
      @             \result == calcMagSum(m3));
      @ accessible m[*];
      @ private static helper model \bigint calcMagSum(int[] m) {
      @     return (\sum int i; 0 <= i && i < m.length; toUnsigned(m[i]) * twopower(m.length-i-1));
      @ }
      @*/

    /**
     * Compares the magnitude array of this BigInteger with the specified
     * BigInteger's. This is the version of compareTo ignoring sign.
     *
     * @param val BigInteger whose magnitude array to be compared.
     * @return -1, 0 or 1 as this magnitude array is less than, equal to or
     *         greater than the magnitude aray for the specified BigInteger's.
     */
    /*@ normal_behavior
      @ requires val.<inv>;
      @ ensures calcMagSum(val.mag) == calcMagSum(mag) ==> \result == 0;
      @ ensures calcMagSum(val.mag) > calcMagSum(mag) ==> \result == -1;
      @ ensures calcMagSum(val.mag) < calcMagSum(mag) ==> \result == 1;
      @ assignable \nothing;
      @*/
    final int compareMagnitude(BigInteger val) {
        int[] m1 = mag;
        int len1 = m1.length;
        int[] m2 = val.mag;
        int len2 = m2.length;
        if (len1 < len2)
            return -1;
        if (len1 > len2)
            return 1;
          
        /*@ loop_invariant 0 <= i && i <= len1 && (\forall int j; 0 <= j && j < i; m1[j] == m2[j]);
          @ assignable \nothing;
          @ decreases len1 - i;
          @*/
        for (int i = 0; i < len1; i++) {
            int a = m1[i];
            int b = m2[i];
            if (a != b)
                return ((a & LONG_MASK) < (b & LONG_MASK)) ? -1 : 1;
        }
        return 0;
    }

    /*@ private model_behavior
      @ requires arr.length > 0;                                          // case 1: arr = {}
      @ requires (\exists int i; 0 <= i && i < arr.length; arr[i] != 0);  // case 2: arr = {0 .. 0}
      @ ensures \result >= 0;
      @ ensures \result < arr.length && arr[\result] != 0
      @      && (\forall int i; 0 <= i && i < \result; arr[i] == 0);      // case 3: arr = {x1 .. xn}
      @ private static helper model int firstNonZeroIndex(int[] arr);
      @*/

    /**
     * Returns a copy of the input array stripped of any leading zero bytes.
     */
    /*@ private normal_behavior
      @ ensures \result != val;                        // difference to trustedStripLeadingZeroInts
      @ ensures \result.length <= val.length;
      @ ensures val.length == 0 ==> result.length == 0;             // case 1: arr = {}
      @ ensures (\forall int i; 0 <= i && i < val.length; val[i] == 0)
      @                 ==> \result.length == 0;                    // case 2: arr = {0 .. 0}
      @ ensures (\exists int i; 0 <= i && i < val.length; val[i] != 0)
      @                 ==> \result[0] != 0;                        // case 3: arr = {x1 .. xn}
      @ ensures (\forall int i; 0 <= i && i < \result.length;
      @             \result[i] == val[firstNonZeroIndex(val) + i]); // copy of old values, same order
      @ assignable \nothing;
      @*/
    private static /*@ helper @*/ int[] stripLeadingZeroInts(int val[]) {
        int vlen = val.length;
        int keep;

        // Find first nonzero int
        /*@ loop_invariant 0 <= keep && keep <= vlen
          @             && (\forall int i; 0 <= i && i < keep; val[i] == 0);
          @ assignable \strictly_nothing;
          @ decreases vlen - keep;
          @*/
        for (keep = 0; keep < vlen && val[keep] == 0; keep++)
            ;
        return java.util.Arrays.copyOfRange(val, keep, vlen);
    }

    /**
     * Returns the input array stripped of any leading zero bytes.
     * Since the source is trusted the copying may be skipped.
     */
    /*@ private normal_behavior
      @
      @ ensures \result.length <= val.length;
      @ ensures val.length == 0 ==> result.length == 0;             // case1: arr = {}
      @ ensures (\forall int i; 0 <= i && i < val.length; val[i] == 0)
      @                 ==> \result.length == 0;                    // case2: arr = {0 .. 0}
      @ ensures (\exists int i; 0 <= i && i < val.length; val[i] != 0)
      @                 ==> \result[0] != 0;                        // case3: arr = {x1 .. xn}
      @ ensures (\forall int i; 0 <= i && i < \result.length;
      @             \result[i] == val[firstNonZeroIndex(val) + i]); // copy of old values, same order
      @ assignable \nothing;
      @*/
    private static /*@ helper @*/ int[] trustedStripLeadingZeroInts(int val[]) {
        int vlen = val.length;
        int keep;

        // Find first nonzero int
        /*@ loop_invariant 0 <= keep && keep <= vlen
          @             && (\forall int i; 0 <= i && i < keep; val[i] == 0);
          @ assignable \strictly_nothing;
          @ decreases vlen - keep;
          @*/
        for (keep = 0; keep < vlen && val[keep] == 0; keep++)
            ;
        return keep == 0 ? val : java.util.Arrays.copyOfRange(val, keep, vlen);
    }
    
    /**
     * Multiplies int arrays x and y to the specified lengths and places
     * the result into z. There will be no leading zeros in the resultant array.
     */
    private static int[] multiplyToLen(int[] x, int xlen, int[] y, int ylen, int[] z) {
        int xstart = xlen - 1;
        int ystart = ylen - 1;

        if (z == null || z.length < (xlen+ ylen))
            z = new int[xlen+ylen];

        long carry = 0;
        for (int j=ystart, k=ystart+1+xstart; j >= 0; j--, k--) {
            long product = (y[j] & LONG_MASK) *
                           (x[xstart] & LONG_MASK) + carry;
            z[k] = (int)product;
            carry = product >>> 32;
        }
        z[xstart] = (int)carry;

        for (int i = xstart-1; i >= 0; i--) {
            carry = 0;
            for (int j=ystart, k=ystart+1+i; j >= 0; j--, k--) {
                long product = (y[j] & LONG_MASK) *
                               (x[i] & LONG_MASK) +
                               (z[k] & LONG_MASK) + carry;
                z[k] = (int)product;
                carry = product >>> 32;
            }
            z[i] = (int)carry;
        }
        return z;
    }
}
