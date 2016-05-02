package vine.math;

import vine.math.vector.Vec2f;

public final class VineMath
{
    public static final float PIf     = 3.14159265359f;
    public static final float TWO_PIf = PIf * 2;

    public static float getSlope(final Vec2f direction)
    {
        return direction.getY() / direction.getX();
    }

    public static boolean isEven(final int value)
    {
        return value % 2 == 0;
    }

    public static final boolean isOdd(final int value)
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
        return Math.round(randomFloat(max));
    }

    public static float randomFloat(final int max)
    {
        return (float) Math.random() * max;
    }

    public static long binominalCoefficient(final int n, final int k)
    {
        return factorial(n) / (factorial(k) * factorial(n - k));
    }

    public static long factorial(final int n)
    {
        if (n < 0 || n > 20)
        {
            throw new IllegalArgumentException(n + " is no valid value to calculate a factorial.");
        }
        int result = 1;
        for (int i = n; i > 0; i--)
        {
            result *= i;
        }
        return result;
    }
}
