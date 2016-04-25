package vine.math;

public class VineMath
{

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
    public static final float clamp(float value, float min, float max)
    {
        if (value <= min)
            return min;
        if (value >= max)
            return max;
        return value;
    }

    public static final int clamp(int value, int min, int max)
    {
        if (value <= min)
            return min;
        if (value >= max)
            return max;
        return value;
    }

    /**
     * Clamps a negative value to zero
     * 
     * @param value
     *            The value that should be clamped
     * @return The clamped value
     */
    public static final float clampPositive(float value)
    {
        if (value <= 0)
            return 0;
        return value;
    }

    public static final int clampPositive(int value)
    {
        if (value <= 0)
            return 0;
        return value;
    }

    /**
     * Returns an random integer in the interval [0,max)
     * 
     * @param max
     *            The maximal random value returned
     * @return The random value in the interval
     */
    public static final int randomInteger(int max)
    {
        return Math.round(randomFloat(max));
    }

    public static final float randomFloat(int max)
    {
        return (float) Math.random() * max;
    }
}
