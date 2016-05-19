package vine.math;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import vine.math.matrix.Mat3f;
import vine.math.matrix.MutableMat3f;

/**
 * @author Steffen
 *
 */
public class Matrix3fTest
{

    /**
     *
     */
    @Test
    public void testEquality()
    {
        final MutableMat3f matrix = new MutableMat3f();
        matrix.setRow(0, 1.f, 0.f, 0.f);
        matrix.setRow(1, 0.f, 0.f, 1.f);
        matrix.setRow(2, 0.f, 1.f, 0.f);
        assertTrue(!matrix.equals(null));
        final MutableMat3f matrix2 = new MutableMat3f();
        assertTrue(!matrix.equals(matrix2));
        matrix2.setRow(0, 1.f, 0.f, 0.f);
        assertTrue(!matrix.equals(matrix2));
        matrix2.setRow(1, 0.f, 0.f, 1.f);
        assertTrue(!matrix.equals(matrix2));
        matrix2.setRow(0, 0.f, 0.f, 0.f);
        assertTrue(!matrix.equals(matrix2));
        matrix2.setRow(2, 0.f, 1.f, 0.f);
        assertTrue(!matrix.equals(matrix2));
        matrix2.setRow(1, 0.f, 0.f, 0.f);
        assertTrue(!matrix.equals(matrix2));
    }

    @Test
    public void testPropertyAccess()
    {
        final MutableMat3f matrix = new MutableMat3f();
        matrix.setA11(1);
        matrix.setA12(1);
        matrix.setA13(1);
        matrix.setA21(1);
        matrix.setA22(1);
        matrix.setA23(1);
        matrix.setA31(1);
        matrix.setA32(1);
        matrix.setA33(1);
        assertTrue(matrix.getA11() == 1);
        assertTrue(matrix.getA12() == 1);
        assertTrue(matrix.getA13() == 1);
        assertTrue(matrix.getA21() == 1);
        assertTrue(matrix.getA22() == 1);
        assertTrue(matrix.getA23() == 1);
        assertTrue(matrix.getA31() == 1);
        assertTrue(matrix.getA32() == 1);
        assertTrue(matrix.getA33() == 1);
        final MutableMat3f matrix2 = new MutableMat3f(matrix);
        matrix2.setRow(-1, 1, 3, 4);
        assertTrue(matrix.equals(matrix2));
    }

    /**
     * A correctly transposed , orthogonal Matrix multiplied with the not
     * transposed Matrix creates the identity matrix.
     */
    @Test
    public void testOrthogonalInversed()
    {
        final MutableMat3f matrix = new MutableMat3f();
        matrix.setRow(0, 1.f, 0.f, 0.f);
        matrix.setRow(1, 0.f, 0.f, 1.f);
        matrix.setRow(2, 0.f, 1.f, 0.f);
        matrix.transpose();
        matrix.rightMultiply(matrix);
        final Mat3f mat = Mat3f.getIdentity();
        assertTrue(mat.equals(matrix));
    }

    /**
     * Tests transpose of Matrix3f.
     */
    @Test
    public void testTranspose()
    {
        final MutableMat3f matrix = new MutableMat3f();
        matrix.setRow(0, 1.f, 1.f, 1.f);
        matrix.setRow(1, 6.f, 5.f, 4.f);
        matrix.setRow(2, 1.f, 1.f, 0.f);
        final MutableMat3f transposedMatrix = new MutableMat3f();
        transposedMatrix.setRow(0, 1.f, 6.f, 1.f);
        transposedMatrix.setRow(1, 1.f, 5.f, 1.f);
        transposedMatrix.setRow(2, 1.f, 4.f, 0.f);
        matrix.transpose();
        assertTrue(matrix.equals(transposedMatrix));
    }

    /**
     *
     */
    @Test
    public void testIdentity()
    {
        final MutableMat3f matrix = new MutableMat3f();
        matrix.setRow(0, 2.f, 0.f, 0.f);
        matrix.setRow(1, 0.f, 2.f, 0.f);
        matrix.setRow(2, 0.f, 0.f, 2.f);
        assertTrue(matrix.equals(matrix));
        assertTrue(matrix.equals(matrix));
    }

    /**
     * Tests, that the calculation of the determinant of given matrices is
     * correct.
     */
    @Test
    public void testCalculateDeterminant()
    {
        MutableMat3f matrix = new MutableMat3f();
        matrix.setRow(0, 1.f, 1.f, 1.f);
        matrix.setRow(1, 6.f, 5.f, 4.f);
        matrix.setRow(2, 1.f, 1.f, 0.f);
        MutableMat3f mat = new MutableMat3f(matrix);
        assertTrue(mat.determinant() == 1.f);
        matrix = new MutableMat3f();
        mat = matrix;
        assertTrue(mat.determinant() == 0.f);
        final Mat3f notInvertable = new Mat3f(matrix);
        mat.inverse();
        assertTrue(matrix.equals(notInvertable));
    }

    /**
     * Tests, that Matrix3f invert is correct.
     */
    @Test
    public void testInvert()
    {
        MutableMat3f matrix = new MutableMat3f();
        matrix.setRow(0, 1.f, 1.f, 1.f);
        matrix.setRow(1, 6.f, 5.f, 4.f);
        matrix.setRow(2, 1.f, 1.f, 0.f);
        MutableMat3f invertedMatrix = new MutableMat3f();
        invertedMatrix.setRow(0, 1.f, 1.f, 1.f);
        invertedMatrix.setRow(1, 6.f, 5.f, 4.f);
        invertedMatrix.setRow(2, 1.f, 1.f, 0.f);
        invertedMatrix.inverse();
        invertedMatrix.rightMultiply(matrix);
        assertTrue(Mat3f.getIdentity().equals(invertedMatrix));
        matrix = new MutableMat3f();
        matrix.setRow(0, 8.f, 1.f, .4f);
        matrix.setRow(1, 6.f, 5.1f, 4.5f);
        matrix.setRow(2, 1.f, 11.1f, 0.3f);
        invertedMatrix = new MutableMat3f(matrix);
        invertedMatrix.inverse();
        matrix.rightMultiply(invertedMatrix);
        assertTrue(Mat3f.getIdentity().equals(matrix));
    }

    /**
     *
     */
    @Test
    public void testAddition()
    {
        final MutableMat3f matrix = new MutableMat3f();
        matrix.setRow(0, 1.f, 1.f, 1.f);
        matrix.setRow(1, 6.f, 5.f, 4.f);
        matrix.setRow(2, 1.f, 1.f, 0.f);
        matrix.add(matrix);
        final MutableMat3f resultMatrix = new MutableMat3f();
        resultMatrix.setRow(0, 2.f, 2.f, 2.f);
        resultMatrix.setRow(1, 12.f, 10.f, 8.f);
        resultMatrix.setRow(2, 2.f, 2.f, 0.f);
        assertTrue(matrix.equals(resultMatrix));
        matrix.add(null);
        assertTrue(matrix.equals(resultMatrix));
    }

    /**
     *
     */
    @Test
    public void testMultiply()
    {
        final MutableMat3f matrix = new MutableMat3f();
        matrix.setRow(0, 1.f, 1.f, 1.f);
        matrix.setRow(1, 6.f, 5.f, 4.f);
        matrix.setRow(2, 1.f, 1.f, 0.f);
        matrix.rightMultiply(matrix);

        final MutableMat3f resultMatrix = new MutableMat3f();
        resultMatrix.setRow(0, 8.f, 7.f, 5.f);
        resultMatrix.setRow(1, 40.f, 35.f, 26.f);
        resultMatrix.setRow(2, 7.f, 6.f, 5.f);
        assertTrue(matrix.equals(resultMatrix));
        resultMatrix.rightMultiply(null);
        assertTrue(matrix.equals(resultMatrix));
    }

}
