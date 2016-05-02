package vine.math;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import vine.math.matrix.Mat3f;

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
        Mat3f matrix = new Mat3f();
        matrix.setRow(0, 1.f, 0.f, 0.f);
        matrix.setRow(1, 0.f, 0.f, 1.f);
        matrix.setRow(2, 0.f, 1.f, 0.f);
        assertTrue(!matrix.equals(null));
        Mat3f matrix2 = new Mat3f();
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

    /**
     * Tests the performance of matrix operations.
     */
    @Test
    public void testPerformance()
    {

        Mat3f matrix = new Mat3f().setRow(0, 1.f, 0.f, 0.f).setRow(1, 0.f, 1.f, 0.f).setRow(2, 0.f, 0.f, 1.f);

        int count = 0;
        int times = 100;
        while (times > 0)
        {
            long time = System.currentTimeMillis();
            while (System.currentTimeMillis() - time < 33)
            {
                matrix.inverse();
                count++;
            }
            times--;
        }
        count /= 100;
        System.out.println("Can invert a matrix about:" + count + "times in 33 milliseconds\n");

        count = 0;
        times = 100;
        while (times > 0)
        {
            long time = System.currentTimeMillis();
            while (System.currentTimeMillis() - time < 33)
            {
                matrix.rightMultiply(matrix);
                count++;
            }
            times--;
        }
        count /= 100;
        System.out
                .println("Can multiply a matrix about:" + count + "times in 33 milliseconds without object creation\n");

        count = 0;
        times = 100;
        while (times > 0)
        {
            long time = System.currentTimeMillis();
            while (System.currentTimeMillis() - time < 33)
            {
                Mat3f matrix2 = new Mat3f();
                matrix2.setRow(0, 1.f, 0.f, 0.f);
                matrix2.setRow(1, 0.f, 1.f, 0.f);
                matrix2.setRow(2, 0.f, 0.f, 1.f);
                matrix.rightMultiply(matrix2);
                count++;
            }
            times--;
        }
        count /= 100;
    }

    /**
     * 
     */
    @Test
    public void testPropertyAccess()
    {
        Mat3f matrix = new Mat3f();
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
        Mat3f matrix2 = new Mat3f(matrix);
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
        Mat3f matrix = new Mat3f();
        matrix.setRow(0, 1.f, 0.f, 0.f);
        matrix.setRow(1, 0.f, 0.f, 1.f);
        matrix.setRow(2, 0.f, 1.f, 0.f);
        matrix.transpose();
        matrix.rightMultiply(matrix);
        Mat3f mat = Mat3f.getIdentity();
        assertTrue(mat.equals(matrix));
    }

    /**
     * Tests transpose of Matrix3f.
     */
    @Test
    public void testTranspose()
    {
        Mat3f matrix = new Mat3f();
        matrix.setRow(0, 1.f, 1.f, 1.f);
        matrix.setRow(1, 6.f, 5.f, 4.f);
        matrix.setRow(2, 1.f, 1.f, 0.f);
        Mat3f transposedMatrix = new Mat3f();
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
        Mat3f matrix = new Mat3f();
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
        Mat3f matrix = new Mat3f();
        matrix.setRow(0, 1.f, 1.f, 1.f);
        matrix.setRow(1, 6.f, 5.f, 4.f);
        matrix.setRow(2, 1.f, 1.f, 0.f);
        Mat3f mat = matrix;
        assertTrue(mat.determinant() == 1.f);
        matrix = new Mat3f();
        mat = matrix;
        assertTrue(mat.determinant() == 0.f);
        Mat3f notInvertable = new Mat3f(matrix);
        mat.inverse();
        assertTrue(matrix.equals(notInvertable));
    }

    /**
     * Tests, that Matrix3f invert is correct.
     */
    @Test
    public void testInvert()
    {
        Mat3f matrix = new Mat3f();
        matrix.setRow(0, 1.f, 1.f, 1.f);
        matrix.setRow(1, 6.f, 5.f, 4.f);
        matrix.setRow(2, 1.f, 1.f, 0.f);
        Mat3f invertedMatrix = new Mat3f();
        invertedMatrix.setRow(0, 1.f, 1.f, 1.f);
        invertedMatrix.setRow(1, 6.f, 5.f, 4.f);
        invertedMatrix.setRow(2, 1.f, 1.f, 0.f);
        invertedMatrix.inverse();
        invertedMatrix.rightMultiply(matrix);
        assertTrue(Mat3f.getIdentity().equals(invertedMatrix));
        matrix = new Mat3f();
        matrix.setRow(0, 8.f, 1.f, .4f);
        matrix.setRow(1, 6.f, 5.1f, 4.5f);
        matrix.setRow(2, 1.f, 11.1f, 0.3f);
        invertedMatrix = new Mat3f(matrix);
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
        Mat3f matrix = new Mat3f();
        matrix.setRow(0, 1.f, 1.f, 1.f);
        matrix.setRow(1, 6.f, 5.f, 4.f);
        matrix.setRow(2, 1.f, 1.f, 0.f);
        matrix.add(matrix);
        Mat3f resultMatrix = new Mat3f();
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
        Mat3f matrix = new Mat3f();
        matrix.setRow(0, 1.f, 1.f, 1.f);
        matrix.setRow(1, 6.f, 5.f, 4.f);
        matrix.setRow(2, 1.f, 1.f, 0.f);
        matrix.rightMultiply(matrix);

        Mat3f resultMatrix = new Mat3f();
        resultMatrix.setRow(0, 8.f, 7.f, 5.f);
        resultMatrix.setRow(1, 40.f, 35.f, 26.f);
        resultMatrix.setRow(2, 7.f, 6.f, 5.f);
        assertTrue(matrix.equals(resultMatrix));
        resultMatrix.rightMultiply(null);
        assertTrue(matrix.equals(resultMatrix));
    }

}
