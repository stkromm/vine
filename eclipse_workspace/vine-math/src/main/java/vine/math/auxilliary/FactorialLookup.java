package vine.math.auxilliary;

public final class FactorialLookup
{
    private static final long[] FACTORIALS = new long[21];

    static
    {
        for (int n = 0; n <= 20; n++)
        {
            FACTORIALS[n] = 1;
            for (int i = n; i > 0; i--)
            {
                FACTORIALS[n] *= i;
            }
        }
    }

    private FactorialLookup()
    {
        // Utility class
    }

    public static long factorial(final int n)
    {
        return FACTORIALS[n];
    }
}
