package vine.math.vector;

import vine.math.VineMath;

public final class Vec3Util
{
    /**
     * Maximum difference two floating point values can have and still count as
     * equal.
     */
    public static final float VEC3_EPSILON = 0.000001f;

    private Vec3Util()
    {
        // Utility
    }

    /**
     * Calculates the squared length of the given vector.
     *
     * @param x
     *            The x coordinate of the vector
     * @param y
     *            The y coordinate of the vector
     * @return The squared length of the each element given 2d vector.
     */
    public static double squaredLength(final double x, final double y, final double z)
    {
        return dot(x, y, z, x, y, z);
    }

    /**
     * Calculates the squared length of the given vector.
     *
     * @param x
     *            The x coordinate of the vector
     * @param y
     *            The y coordinate of the vector
     * @return The squared length of the each element given 2d vector.
     */
    public static float squaredLength(final float x, final float y, final float z)
    {
        return dot(x, y, z, x, y, z);
    }

    /**
     * Calculates the length of the given vector.
     *
     * @param x
     *            The x coordinate of the vector
     * @param y
     *            The y coordinate of the vector
     * @return The length of the elementwise given 2d vector.
     */
    public static double length(final double x, final double y, final double z)
    {
        return VineMath.sqrt(dot(x, y, z, x, y, z));
    }

    public static double dot(
            final double x1,
            final double y1,
            final double z1,
            final double x2,
            final double y2,
            final double z2)
    {
        return x1 * x2 + y1 * y2 + z1 * z2;
    }

    public static float dot(
            final float x1,
            final float y1,
            final float z1,
            final float x2,
            final float y2,
            final float z2)
    {
        return x1 * x2 + y1 * y2 + z1 * z2;
    }
}
