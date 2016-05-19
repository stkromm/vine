package vine.math.geometry;

/**
 * Defines a 2d object a affine transformation can be used on.
 *
 * @author Steffen Kromm, first created on 04.05.2016
 */
public interface Transformable
{
    /**
     * Translates the Transformable by the given vector.
     *
     * @param x
     *            The translation in the x dimension.
     * @param y
     *            The translation in the y dimension.
     */
    void translate(float x, float y);

    /**
     * Rotates the Transformable by the given angle in degree (positive values
     * are counterclockwise rotation).
     *
     * @param degree
     *            The angle of the rotation in degrees.
     */
    void rotate(float degree);

    /**
     * Performs a scaling with separate scaling factors for each dimension.
     *
     * @param x
     *            The scaling factor of the x dimension.
     * @param y
     *            The scaling factor of the y dimension.
     */
    void scale(float x, float y);

    /**
     * Performs a scaling on both dimensions with the given factor.
     *
     * @param factor
     *            The factor by which the transformable is scaled.
     */
    void uniformScale(float factor);
}
