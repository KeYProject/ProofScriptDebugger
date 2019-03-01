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


    /*@ helper normal_behavior
      @ ensures this.mag == magnitude;
      @*/
    BigInteger(int[] magnitude, int signum) {

    }





}
