package vine.math;

import vine.math.auxilliary.FactorialLookup;
import vine.math.auxilliary.Icecore;
import vine.math.auxilliary.LookupSinCos;

public final class VineMath
{
    private static final VineRandom RANDOM   = new VineRandom();
    private static final int        ONE_BIT  = 1;

    public static final float       PIF      = 3.14159265358979323846f;
    public static final double      PI       = 3.14159265358979323846;
    public static final float       TWO_PIF  = PIF * 2;
    public static final float       HALF_PIF = 3.14159265358979323846f * 0.5f;

    private VineMath()
    {
        // Utility class
    }

    public static float abs(final float value)
    {
        return value <= 0.0f ? 0.0f - value : value;
    }

    public static double abs(final double value)
    {
        return value <= 0.0 ? 0.0 - value : value;
    }

    public static int abs(final int value)
    {
        return value >= 0 ? value : -value;
    }

    public static float max(final float t3, final float t4)
    {
        return t3 >= t4 ? t3 : t4;
    }

    public static float min(final float t3, final float t4)
    {
        return t3 <= t4 ? t3 : t4;
    }

    public static boolean isEven(final int value)
    {
        return value % 2 == 0;
    }

    public static boolean isOdd(final int value)
    {
        return value % 2 == 1;
    }

    /**
     * Clamps the given value to the interval [min,max]
     *
     * @param value
     *            The value that should be clamped
     * @param min
     *            The lower interval border
     * @param max
     *            The upper interval border
     * @return the clamped value
     */
    public static float clamp(final float value, final float min, final float max)
    {
        if (value <= min)
        {
            return min;
        }
        if (value >= max)
        {
            return max;
        }
        return value;
    }

    public static int repeat(final int value, final int min, final int max)
    {
        if (value > max)
        {
            return min + value % (max - min);
        }
        if (value <= min)
        {
            return max - (min - value);
        }
        return value;
    }

    public static int clamp(final int value, final int min, final int max)
    {
        if (value <= min)
        {
            return min;
        }
        if (value >= max)
        {
            return max;
        }
        return value;
    }

    /**
     * Clamps a negative value to zero
     *
     * @param value
     *            The value that should be clamped
     * @return The clamped value
     */
    public static float clampPositive(final float value)
    {
        if (value <= 0)
        {
            return 0;
        }
        return value;
    }

    public static int clampPositive(final int value)
    {
        if (value <= 0)
        {
            return 0;
        }
        return value;
    }

    /**
     * Returns an random integer in the interval [0,max)
     *
     * @param max
     *            The maximal random value returned
     * @return The random value in the interval
     */
    public static int randomInteger(final int max)
    {
        return StrictMath.round(randomFloat(max));
    }

    public static float randomFloat(final int max)
    {
        return RANDOM.nextFloat() * max;
    }

    public static double binominalCoefficient(final int n, final int k)
    {
        return factorial(n) / (double) (factorial(k) * factorial(n - k));
    }

    public static long factorial(final int n)
    {
        if (n < 0 || n > 20)
        {
            throw new IllegalArgumentException(n + " is no valid value to calculate a factorial.");
        }
        return FactorialLookup.factorial(n);
    }

    public static double sqrt(final double value)
    {
        return StrictMath.sqrt(value);
    }

    public static double pow(final double a, final double b)
    {
        return StrictMath.pow(a, b);
    }

    public static int pow(final int a, final int b)
    {
        int base = a;
        int power = b;
        int result = 1;
        for (; power != 0; power >>= 1)
        {
            if ((power & ONE_BIT) == ONE_BIT)
            {
                result *= base;
            }
            base *= base;
        }

        return result;
    }

    public static float exp(final float val)
    {
        final long tmp = (long) (1512775 * val + 1072632447);
        return Float.intBitsToFloat((int) (tmp << 32));
    }

    public static float log(final float x)
    {
        return 6 * (x - 1) / (x + 1 + 4 * (float) VineMath.sqrt(x));
    }

    public static float sin(final float rad)
    {
        return LookupSinCos.sin(rad);
    }

    public static float cos(final float rad)
    {
        return LookupSinCos.cos(rad);
    }

    public static float atan2(final float y, final float x)
    {
        return Icecore.atan2(y, x);
    }

    public static float toDegress(final float radians)
    {
        return radians * 180.0f * (1 / PIF);
    }

    public static float toRadians(final float angle)
    {
        return angle * (1 / 180.f) * PIF;
    }
}
