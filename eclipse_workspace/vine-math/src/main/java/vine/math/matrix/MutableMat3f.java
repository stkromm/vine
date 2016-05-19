package vine.math.matrix;

import java.util.logging.Level;
import java.util.logging.Logger;

import vine.math.VineMath;
import vine.math.geometry.Transformable;

public class MutableMat3f extends Mat3f implements Transformable
{
    private static final Logger LOGGER           = Logger.getGlobal();
    private static final long   serialVersionUID = -5630666356153676012L;

    public MutableMat3f()
    {
        super();
    }

    public MutableMat3f(final float[] values)
    {
        super(values);
    }

    public MutableMat3f(final Mat3f matrix)
    {
        super(matrix);
    }

    public MutableMat3f(final float a11, final float a12, final float a13, final float a21, final float a22,
            final float a23, final float a31, final float a32, final float a33)
    {
        super(a11, a12, a13, a21, a22, a23, a31, a32, a33);
    }

    /**
     * @param a11
     *            row 1 column 1
     */
    public void setA11(final float a11)
    {
        this.a11 = a11;
    }

    /**
     * @param a12
     *            row 1 column 2
     */
    public void setA12(final float a12)
    {
        this.a12 = a12;
    }

    /**
     * @param a13
     *            row 1 column 3
     */
    public void setA13(final float a13)
    {
        this.a13 = a13;
    }

    /**
     * @param a21
     *            row 2 column 1
     */
    public void setA21(final float a21)
    {
        this.a21 = a21;
    }

    /**
     * @param a22
     *            row 2 column 2
     */
    public void setA22(final float a22)
    {
        this.a22 = a22;
    }

    /**
     * @param a23
     *            row 2 column 3
     */
    public void setA23(final float a23)
    {
        this.a23 = a23;
    }

    /**
     * @param a31
     *            row 3 column 1
     */
    public void setA31(final float a31)
    {
        this.a31 = a31;
    }

    /**
     * @param a32
     *            row 3 column 2
     */
    public void setA32(final float a32)
    {
        this.a32 = a32;
    }

    /**
     * @param a33
     *            row 3 column 3
     */
    public void setA33(final float a33)
    {
        this.a33 = a33;
    }

    /**
     * Sets the elements of the row that responds to the given index.
     *
     * @param rowIndex
     *            Valid values are in [0,2].
     * @param e1
     *            first column element
     * @param e2
     *            second column element
     * @param e3
     *            third column element
     * @return this
     */
    public final Mat3f setRow(final int rowIndex, final float e1, final float e2, final float e3)
    {
        switch (rowIndex) {
        case 0:
            a11 = e1;
            a12 = e2;
            a13 = e3;
        break;
        case 1:
            a21 = e1;
            a22 = e2;
            a23 = e3;
        break;
        case 2:
            a31 = e1;
            a32 = e2;
            a33 = e3;
        break;
        default:
            LOGGER.log(Level.WARNING, "Accessed matrix with invalid row-index: " + rowIndex);
        break;
        }
        return this;
    }

    /**
     * Transposes this matrix.
     */
    public final void transpose()
    {
        float temp = a21;
        a21 = a12;
        a12 = temp;
        temp = a31;
        a31 = a13;
        a13 = temp;
        temp = a32;
        a32 = a23;
        a23 = temp;
    }

    /**
     * Transforms this matrix into its inversed matrix. Does nothin, if the
     * matrix is not invertable.
     */
    public final void inverse()
    {
        final float determinant = determinant();
        if (determinant <= EPSILON && determinant >= -EPSILON)
        {
            LOGGER.log(Level.WARNING, "Tried to invert a Mat3f with determinant 0");
            return;
        }
        // Calculate elements of the inverse 3x3 matrix with the inversed
        // determinant defactored.
        final float temp00 = a22 * a33 - a32 * a23;
        final float temp01 = a32 * a13 - a33 * a12;
        final float temp02 = a12 * a23 - a13 * a22;
        final float temp10 = a23 * a31 - a21 * a33;
        final float temp11 = a11 * a33 - a13 * a31;
        final float temp12 = a13 * a21 - a23 * a11;
        final float temp20 = a21 * a32 - a31 * a22;
        final float temp21 = a12 * a31 - a11 * a32;
        final float temp22 = a11 * a22 - a12 * a21;
        a11 = temp00;
        a12 = temp01;
        a13 = temp02;
        a21 = temp10;
        a22 = temp11;
        a23 = temp12;
        a31 = temp20;
        a32 = temp21;
        a33 = temp22;
        // Multiply with inversed determinant of this matrix
        uniformScale(1 / determinant);
    }

    /**
     * Multiplies the given matrix from the right with this Matrix3f.
     *
     * @param matrix
     *            Matrix which is multiplied with this matrix.
     *
     */
    public final void rightMultiply(final Mat3f matrix)
    {
        if (matrix == null)
        {
            LOGGER.log(Level.WARNING, "Tried to right multiply with null matrix");
        } else
        {
            multiply(this, matrix);
        }
    }

    /**
     * Multiplies the given matrix from the left with this Matrix3f. The result
     * is the given matrix.
     *
     * @param matrix
     *            Matrix which is multiplied with this matrix.
     *
     */
    public final void leftMultiply(final Mat3f matrix)
    {
        if (matrix == null)
        {
            LOGGER.log(Level.WARNING, "Tried to left multiply with null matrix");
        } else
        {
            multiply(matrix, this);
        }
    }

    public final void multiply(final Mat3f left, final Mat3f right)
    {
        final float temp1 = left.a11 * right.a11 + left.a12 * right.a21 + left.a13 * right.a31;
        final float temp2 = left.a11 * right.a12 + left.a12 * right.a22 + left.a13 * right.a32;
        final float temp3 = left.a11 * right.a13 + left.a12 * right.a23 + left.a13 * right.a33;
        final float temp4 = left.a21 * right.a11 + left.a22 * right.a21 + left.a23 * right.a31;
        final float temp5 = left.a21 * right.a12 + left.a22 * right.a22 + left.a23 * right.a32;
        final float temp6 = left.a21 * right.a13 + left.a22 * right.a23 + left.a23 * right.a33;
        final float temp7 = left.a31 * right.a11 + left.a32 * right.a21 + left.a33 * right.a31;
        final float temp8 = left.a31 * right.a12 + left.a32 * right.a22 + left.a33 * right.a32;
        final float temp9 = left.a31 * right.a13 + left.a32 * right.a23 + left.a33 * right.a33;
        a11 = temp1;
        a12 = temp2;
        a13 = temp3;
        a21 = temp4;
        a22 = temp5;
        a23 = temp6;
        a31 = temp7;
        a32 = temp8;
        a33 = temp9;
    }

    /**
     * Multiplies every element of the matrix with the given scale value.
     *
     * @param scale
     *            Uniform scale factor, that scales all element of this matrix
     */
    @Override
    public final void uniformScale(final float scale)
    {
        a11 *= scale;
        a12 *= scale;
        a13 *= scale;
        //
        a21 *= scale;
        a22 *= scale;
        a23 *= scale;
        //
        a31 *= scale;
        a32 *= scale;
        a33 *= scale;
    }

    /**
     * @param x
     *            Scales the a11 element
     * @param y
     *            Scales the a22 element
     */
    @Override
    public void scale(final float x, final float y)
    {
        a11 *= x;
        a22 *= y;
    }

    /**
     * Adds the entries of the given matrix to the elements of this matrix.
     *
     * @param matrix
     *            The matrix, which is added to this matrix.
     *
     * @return this
     */
    public final void add(final Mat3f matrix)
    {
        if (matrix != null)
        {
            a11 += matrix.getA11();
            a12 += matrix.getA12();
            a13 += matrix.getA13();
            a21 += matrix.getA21();
            a22 += matrix.getA22();
            a23 += matrix.getA23();
            a31 += matrix.getA31();
            a32 += matrix.getA32();
            a33 += matrix.getA33();
        }
    }

    /**
     * Rotates this matrix with the given angle
     *
     * @param angle
     *            Angle about which the matrix is rotated
     * @return this
     *
     */
    @Override
    public void rotate(final float angle)
    {
        final Mat3f rotator = Mat3f.getIdentity();
        final float rotation = VineMath.toRadians(angle);
        final float cos = VineMath.cos(rotation);
        final float sin = VineMath.sin(rotation);
        rotator.a11 = cos;
        rotator.a12 = sin;
        rotator.a21 = -sin;
        rotator.a22 = cos;
        rightMultiply(rotator);
    }

    public void setRotation(final float angle)
    {
        final float cos = VineMath.cos(angle);
        final float sin = VineMath.sin(angle);
        a11 = cos;
        a12 = sin;
        a21 = -sin;
        a22 = cos;
    }

    /**
     * Adds the vector given by the two values as an translation. That is adds
     * the x to the a13 element and the y to the a23 element.
     *
     * @param x
     *            x Value of the translation vector
     * @param y
     *            y Value of the translation vector
     */
    @Override
    public void translate(final float x, final float y)
    {
        a13 += x;
        a23 += y;
    }

    public void setTranslation(final float x, final float y)
    {
        a13 = x;
        a23 = x;
    }
}
