package vine.math;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a 3x3 Matrix, with floating point values as elements.
 * 
 * @author stkromm
 *
 */
public class Mat3f implements Serializable
{
    private static final long    serialVersionUID = -3459114123273506177L;

    /**
     * Maximum difference two floating point values can differ and still count
     * as equal.
     */
    protected static final float EPSILON          = 0.0015f;

    private float                a11              = 0;
    private float                a12              = 0;
    private float                a13              = 0;
    private float                a21              = 0;
    private float                a22              = 0;
    private float                a23              = 0;
    private float                a31              = 0;
    private float                a32              = 0;
    private float                a33              = 0;

    /**
     * Creates a new matrix identical to the given matrix.
     * 
     * @param matrix
     *            The matrix to copy.
     */
    public Mat3f(final Mat3f matrix)
    {
        setA11(matrix.getA11());
        setA12(matrix.getA12());
        setA13(matrix.getA13());
        //
        setA21(matrix.getA21());
        setA22(matrix.getA22());
        setA23(matrix.getA23());
        //
        setA31(matrix.getA31());
        setA32(matrix.getA32());
        setA33(matrix.getA33());
    }

    /**
     * Creates the null matrix.
     */
    public Mat3f()
    {
        /**
         * Leaves the matrix.
         */
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
     * @param a11
     *            row 1 column 1
     */
    public void setA11(float a11)
    {
        this.a11 = a11;
    }

    /**
     * @param a12
     *            row 1 column 2
     */
    public void setA12(float a12)
    {
        this.a12 = a12;
    }

    /**
     * @param a13
     *            row 1 column 3
     */
    public void setA13(float a13)
    {
        this.a13 = a13;
    }

    /**
     * @param a21
     *            row 2 column 1
     */
    public void setA21(float a21)
    {
        this.a21 = a21;
    }

    /**
     * @param a22
     *            row 2 column 2
     */
    public void setA22(float a22)
    {
        this.a22 = a22;
    }

    /**
     * @param a23
     *            row 2 column 3
     */
    public void setA23(float a23)
    {
        this.a23 = a23;
    }

    /**
     * @param a31
     *            row 3 column 1
     */
    public void setA31(float a31)
    {
        this.a31 = a31;
    }

    /**
     * @param a32
     *            row 3 column 2
     */
    public void setA32(float a32)
    {
        this.a32 = a32;
    }

    /**
     * @param a33
     *            row 3 column 3
     */
    public void setA33(float a33)
    {
        this.a33 = a33;
    }

    /**
     * Creates a identity matrix.
     * 
     * @return A new identity matrix
     */
    public static final Mat3f getIdentity()
    {
        final Mat3f identity = new Mat3f();
        identity.setA11(1);
        identity.setA22(1);
        identity.setA33(1);
        return identity;
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
        if (rowIndex == 0)
        {
            a11 = e1;
            a12 = e2;
            a13 = e3;
        } else if (rowIndex == 1)
        {
            a21 = e1;
            a22 = e2;
            a23 = e3;
        } else if (rowIndex == 2)
        {
            a31 = e1;
            a32 = e2;
            a33 = e3;
        } else
        {
            Logger.getGlobal().log(Level.WARNING, Messages.getString("Matrix3f.0") + rowIndex);
        }
        return this;
    }

    /**
     * Calculates the determinant of this matrix and returns it.
     * 
     * @return The determinant value of this matrix.
     */
    public final strictfp float determinant()
    {
        return a11 * a22 * a33 + a12 * a23 * a31 + a13 * a21 * a32 - a12 * a21 * a33 - a13 * a22 * a13
                - a11 * a23 * a32;
    }

    /**
     * Transposes this matrix.
     * 
     * @return TODO
     */
    public final Mat3f transpose()
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
        return this;
    }

    /**
     * Transforms this matrix into its inversed matrix. Does nothin, if the
     * matrix is not invertable.
     * 
     * @return this
     */
    public final strictfp Mat3f inverse()
    {
        final float[][] tempMatrix = new float[3][3];
        final float inversedDet = determinant();
        if (Math.abs(inversedDet) <= EPSILON)
        {
            return this;
        }
        // Calculate elements of the inverse 3x3 matrix with the inversed
        // determinant defactored.

        tempMatrix[0][0] = a22 * a33 - a32 * a23;
        tempMatrix[0][1] = a32 * a13 - a33 * a12;
        tempMatrix[0][2] = a12 * a23 - a13 * a22;
        tempMatrix[1][0] = a23 * a31 - a21 * a33;
        tempMatrix[1][1] = a11 * a33 - a13 * a31;
        tempMatrix[1][2] = a13 * a21 - a23 * a11;
        tempMatrix[2][0] = a21 * a32 - a31 * a22;
        tempMatrix[2][1] = a12 * a31 - a11 * a32;
        tempMatrix[2][2] = a11 * a22 - a12 * a21;
        // assign calculated matrix elements to the corresponding elements of
        // this matrix.

        a11 = tempMatrix[0][0];
        a12 = tempMatrix[0][1];
        a13 = tempMatrix[0][2];
        a21 = tempMatrix[1][0];
        a22 = tempMatrix[1][1];
        a23 = tempMatrix[1][2];
        a31 = tempMatrix[2][0];
        a32 = tempMatrix[2][1];
        a33 = tempMatrix[2][2];
        // Multiply with inversed determinant of this matrix
        scale(1 / inversedDet);
        return this;
    }

    /**
     * Multiplies the given matrix from the right with this Matrix3f.
     * 
     * @param matrix
     *            Matrix which is multiplied with this matrix.
     * 
     * @return this
     */
    public final strictfp Mat3f rightMultiply(Mat3f matrix)
    {
        if (matrix == null)
        {
            Logger.getGlobal().log(Level.WARNING, Messages.getString("Matrix3f.1")); //$NON-NLS-1$
            return this;
        }
        final float temp1 = a11 * matrix.getA11() + a12 * matrix.getA21() + a13 * matrix.getA31();
        final float temp2 = a11 * matrix.getA12() + a12 * matrix.getA22() + a13 * matrix.getA32();
        final float temp3 = a11 * matrix.getA13() + a12 * matrix.getA23() + a13 * matrix.getA33();
        final float temp4 = a21 * matrix.getA11() + a22 * matrix.getA21() + a23 * matrix.getA31();
        final float temp5 = a21 * matrix.getA12() + a22 * matrix.getA22() + a23 * matrix.getA32();
        final float temp6 = a21 * matrix.getA13() + a22 * matrix.getA23() + a23 * matrix.getA33();
        final float temp7 = a31 * matrix.getA11() + a32 * matrix.getA21() + a33 * matrix.getA31();
        final float temp8 = a31 * matrix.getA12() + a32 * matrix.getA22() + a33 * matrix.getA32();
        final float temp9 = a31 * matrix.getA13() + a32 * matrix.getA23() + a33 * matrix.getA33();
        // assign calculated matrix elements to the corresponding elements of
        // this matrix.
        a11 = temp1;
        a12 = temp2;
        a13 = temp3;
        a21 = temp4;
        a22 = temp5;
        a23 = temp6;
        a31 = temp7;
        a32 = temp8;
        a33 = temp9;
        return this;
    }

    /**
     * Multiplies every element of the matrix with the given scale value.
     * 
     * @param scale
     *            Uniform scale factor, that scales all element of this matrix
     * @return this
     */
    public final Mat3f scale(float scale)
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
        return this;
    }

    /**
     * @param x
     *            Scales the a11 element
     * @param y
     *            Scales the a22 element
     * @return this
     */
    public Mat3f scale(float x, float y)
    {
        a11 *= x;
        a22 *= y;
        return this;
    }

    /**
     * Adds the entries of the given matrix to the elements of this matrix.
     * 
     * @param matrix
     *            The matrix, which is added to this matrix.
     * 
     * @return this
     */
    public final Mat3f add(Mat3f matrix)
    {
        if (matrix == null)
        {
            return this;
        }
        a11 += matrix.getA11();
        a12 += matrix.getA12();
        a13 += matrix.getA13();
        //
        a21 += matrix.getA21();
        a22 += matrix.getA22();
        a23 += matrix.getA23();
        //
        a31 += matrix.getA31();
        a32 += matrix.getA32();
        a33 += matrix.getA33();
        return this;
    }

    /**
     * Returns true, if every element of this matrix is equal to the equivalent
     * element of the given matrix.
     * 
     * @param object
     *            Matrix, which is compared with this matrix.
     * @return true, if the matrix is equivalent to the given.
     */
    @Override
    public final boolean equals(Object object)
    {
        if (object == null)
        {
            return false;
        }
        if (!(object instanceof Mat3f))
        {
            return false;
        }
        Mat3f matrix = (Mat3f) object;
        final boolean isFirstRowCorrect = Math
                .abs(a11 - matrix.getA11() + a12 - matrix.getA12() + a13 - matrix.getA13()) <= 3 * EPSILON;
        final boolean isSecondRowCorrect = Math
                .abs(a21 - matrix.getA21() + a22 - matrix.getA22() + a23 - matrix.getA23()) <= 3 * EPSILON;
        final boolean isThirdRowCorrect = Math
                .abs(a31 - matrix.getA31() + a32 - matrix.getA32() + a33 - matrix.getA33()) <= 3 * EPSILON;
        return isFirstRowCorrect && isSecondRowCorrect && isThirdRowCorrect;
    }

    /**
     * Rotates this matrix with the given angle
     * 
     * @param angle
     *            Angle about which the matrix is rotated
     * @return this
     * 
     */
    public Mat3f rotate(float angle)
    {
        final Mat3f rotator = Mat3f.getIdentity();
        final double rotation = Math.toRadians(angle);
        final float cos = (float) Math.cos(rotation);
        final float sin = (float) Math.sin(rotation);
        rotator.a11 = cos;
        rotator.a12 = sin;
        rotator.a21 = -sin;
        rotator.a22 = cos;
        rightMultiply(rotator);
        return this;
    }

    /**
     * Adds the vector given by the two values as an translation. That is adds
     * the x to the a13 element and the y to the a23 element.
     * 
     * @param x
     *            x Value of the translation vector
     * @param y
     *            y Value of the translation vector
     * @return this
     */
    public Mat3f translate(float x, float y)
    {
        a13 += x;
        a23 += y;
        return this;
    }
}
