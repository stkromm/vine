package vine.math.matrix;

import java.io.Serializable;

import vine.math.VineMath;

/**
 * Represents a 3x3 Matrix, with floating point values as elements.
 *
 * @author stkromm
 */
public class Mat3f implements Serializable
{
    private static final long    serialVersionUID = -3459114123273506177L;

    /**
     * Maximum difference two floating point values can differ and still count
     * as equal.
     */
    protected static final float EPSILON          = 0.0015f;

    protected float              a11;
    protected float              a12;
    protected float              a13;
    protected float              a21;
    protected float              a22;
    protected float              a23;
    protected float              a31;
    protected float              a32;
    protected float              a33;

    public Mat3f(final float[] values)
    {
        if (values == null || values.length != 9)
        {
            throw new IllegalArgumentException("Tried to construct Mat3f from an invalid value array");
        }
        a11 = values[0];
        a12 = values[1];
        a13 = values[2];
        a21 = values[3];
        a22 = values[4];
        a23 = values[5];
        a31 = values[6];
        a32 = values[7];
        a33 = values[8];
    }

    /**
     * Creates a new matrix identical to the given matrix.
     *
     * @param matrix
     *            The matrix to copy.
     */
    public Mat3f(final Mat3f matrix)
    {
        if (matrix == null)
        {
            throw new IllegalArgumentException("Tried to copy construct a Mat3f from null");
        }
        a11 = matrix.a11;
        a12 = matrix.a12;
        a13 = matrix.a13;
        a21 = matrix.a21;
        a22 = matrix.a22;
        a23 = matrix.a23;
        a31 = matrix.a31;
        a32 = matrix.a32;
        a33 = matrix.a33;
    }

    /**
     * Creates the null matrix.
     */
    public Mat3f()
    {
        // null matrix
    }

    public Mat3f(final float a11, final float a12, final float a13, final float a21, final float a22, final float a23,
            final float a31, final float a32, final float a33)
    {
        this.a11 = a11;
        this.a12 = a12;
        this.a13 = a13;
        this.a21 = a21;
        this.a22 = a22;
        this.a23 = a23;
        this.a31 = a31;
        this.a32 = a32;
        this.a33 = a33;
    }

    /**
     * @return row 1 column 1
     */
    public float getA11()
    {
        return a11;
    }

    /**
     * @return row 1 column 2
     */
    public float getA12()
    {
        return a12;
    }

    /**
     * @return row 1 column 3
     */
    public float getA13()
    {
        return a13;
    }

    /**
     * @return row 2 column 1
     */
    public float getA21()
    {
        return a21;
    }

    /**
     * @return row 2 column 2
     */
    public float getA22()
    {
        return a22;
    }

    /**
     * @return row 2 column 3
     */
    public float getA23()
    {
        return a23;
    }

    /**
     * @return row 3 column 1
     */
    public float getA31()
    {
        return a31;
    }

    /**
     * @return row 3 column 2
     */
    public float getA32()
    {
        return a32;
    }

    /**
     * @return row 3 column 3
     */
    public float getA33()
    {
        return a33;
    }

    /**
     * Creates a identity matrix.
     *
     * @return A new identity matrix
     */
    public static final Mat3f getIdentity()
    {
        final Mat3f identity = new Mat3f();
        identity.a11 = 1;
        identity.a22 = 1;
        identity.a33 = 1;
        return identity;
    }

    /**
     * Calculates the determinant of this matrix and returns it.
     *
     * @return The determinant value of this matrix.
     */
    public final float determinant()
    {
        return a11 * a22 * a33 + a12 * a23 * a31 + a13 * a21 * a32 - a12 * a21 * a33 - a13 * a22 * a13
                - a11 * a23 * a32;
    }

    @Override
    public final boolean equals(final Object object)
    {
        if (!(object instanceof Mat3f))
        {
            return false;
        }
        if (object == this)
        {
            return true;
        }
        final Mat3f matrix = (Mat3f) object;
        final boolean isFirstRowCorrect = VineMath.abs(a11 - matrix.a11 + a12 - matrix.a12 + a13 - matrix.a13) <= 3
                * EPSILON;
        final boolean isSecondRowCorrect = VineMath.abs(a21 - matrix.a21 + a22 - matrix.a22 + a23 - matrix.a23) <= 3
                * EPSILON;
        final boolean isThirdRowCorrect = VineMath.abs(a31 - matrix.a31 + a32 - matrix.a32 + a33 - matrix.a33) <= 3
                * EPSILON;
        return isFirstRowCorrect && isSecondRowCorrect && isThirdRowCorrect;
    }

    @Override
    public final int hashCode()
    {
        int result = 1;
        result = 2 * result + Float.floatToIntBits(a11);
        result = 3 * result + Float.floatToIntBits(a12);
        result = 5 * result + Float.floatToIntBits(a13);
        result = 7 * result + Float.floatToIntBits(a21);
        result = 11 * result + Float.floatToIntBits(a22);
        result = 13 * result + Float.floatToIntBits(a23);
        result = 17 * result + Float.floatToIntBits(a31);
        result = 19 * result + Float.floatToIntBits(a32);
        result = 23 * result + Float.floatToIntBits(a33);
        return result;
    }

    @Override
    public String toString()
    {
        return "Matrix3f(\n(" + a11 + "," + a12 + "," + a13 + "),\n(" + a21 + "," + a22 + "," + a23 + "),(" + a31
                + ",\n" + a32 + "," + a33 + "))";
    }
}
