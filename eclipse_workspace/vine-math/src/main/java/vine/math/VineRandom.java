package vine.math;

/* This is the successor to xorshift128+. It is the fastest full-period
   generator passing BigCrush without systematic failures, but due to the
   relatively short period it is acceptable only for applications with a
   mild amount of parallelism; otherwise, use a xorshift1024* generator.

   Beside passing BigCrush, this generator passes the PractRand test suite
   up to (and included) 16TB, with the exception of binary rank tests,
   which fail due to the lowest bit being an LFSR; all other bits pass all
   tests. We suggest to use a sign test to extract a random Boolean value.

   Note that the generator uses a simulated rotate operation, which most C
   compilers will turn into a single instruction. In Java, you can use
   Long.rotateLeft(). In languages that do not make low-level rotation
   instructions accessible xorshift128+ could be faster.

   The state must be seeded so that it is not everywhere zero. If you have
   a 64-bit seed, we suggest to seed a splitmix64 generator and use its
   output to fill s. */

/**
 * Written in 2016 by David Blackman and Sebastiano Vigna (vigna@acm.org)
 *
 * @author David Blackman and Sebastiano Vigna, Java-Port by Steffen Kromm,
 *         first created on 18.05.2016
 *
 */
public class VineRandom
{
    private static final long  JUMP_VALUES[] = { 0xbeac0467eba5facbL, 0xd86b048b86aa9922L };
    private final long         s[]           = new long[2];
    /**
     * Normalization constant for float. Libgdx defined
     */
    private static final float NORM_FLOAT    = (float) (1.0f / Math.pow(2, 24));

    public VineRandom()
    {
        s[0] = 763461436;
        s[1] = 821624629;
    }

    public float nextFloat()
    {
        return (next() >>> 40) * NORM_FLOAT;
    }

    public long next()
    {
        final long s0 = s[0];
        long s1 = s[1];
        final long result = s0 + s1;

        s1 ^= s0;
        s[0] = Long.rotateLeft(s0, 55) ^ s1 ^ s1 << 14; // a, b
        s[1] = Long.rotateLeft(s1, 36); // c

        return result;
    }

    /**
     * This is the jump function for the generator. It is equivalent to 2^64
     * calls to next(); it can be used to generate 2^64 non-overlapping
     * subsequences for parallel computations.
     */
    void jump()
    {
        long s0 = 0;
        long s1 = 0;
        for (int i = 0; i < 2 * 64; i++)
        {
            for (int b = 0; b < 64; b++)
            {
                if (JUMP_VALUES[i] == 1L << b)
                {
                    s0 ^= s[0];
                    s1 ^= s[1];
                }
                next();
            }
        }
        s[0] = s0;
        s[1] = s1;
    }
}
