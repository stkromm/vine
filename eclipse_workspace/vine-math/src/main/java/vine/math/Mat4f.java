package vine.math;

/**
 * @author Steffen
 *
 */
public class Mat4f
{

    private static final int SIZE     = 4 * 4;
    private final float[]    elements = new float[SIZE];

    /**
     * 
     */
    public Mat4f()
    {

    }

    /**
     * @param matrix
     *            Matrix3f, that should be a 2d transformation matrix, so the
     *            Conversion is valid.
     */
    public Mat4f(Mat3f matrix)
    {
        // TODO Implement
    }

    public float[] getElements()
    {
        return elements;
    }

    /**
     * @return The identity Matrix4f
     */
    public static Mat4f identity()
    {
        Mat4f result = new Mat4f();
        for (int i = 0; i < SIZE; i++)
        {
            result.elements[i] = 0.0f;
        }
        result.elements[0 + 0 * 4] = 1.0f;
        result.elements[1 + 1 * 4] = 1.0f;
        result.elements[2 + 2 * 4] = 1.0f;
        result.elements[3 + 3 * 4] = 1.0f;

        return result;
    }

    /**
     * Creates a orthographic projection matrix.
     */
    public static Mat4f orthographic(float left, float right, float bottom, float top, float near, float far)
    {
        Mat4f result = identity();

        result.elements[0 + 0 * 4] = 2.0f / (right - left);

        result.elements[1 + 1 * 4] = 2.0f / (top - bottom);

        result.elements[2 + 2 * 4] = 2.0f / (near - far);

        result.elements[0 + 3 * 4] = (left + right) / (left - right);
        result.elements[1 + 3 * 4] = (bottom + top) / (bottom - top);
        result.elements[2 + 3 * 4] = (far + near) / (far - near);

        return result;
    }

    /**
     * @param vector
     * @return
     */
    public static Mat4f translate(Vec3f vector)
    {
        Mat4f result = identity();
        result.elements[0 + 3 * 4] = vector.getX();
        result.elements[1 + 3 * 4] = vector.getY();
        result.elements[2 + 3 * 4] = vector.getZ();
        return result;
    }

    public void setTranslation(float x, float y, float z)
    {
        elements[0 + 3 * 4] = x;
        elements[1 + 3 * 4] = y;
        elements[2 + 3 * 4] = z;
    }

    /**
     * @param angle
     * @return
     */
    public static Mat4f rotate(float angle)
    {
        final Mat4f result = identity();
        float r = (float) Math.toRadians(angle);
        float cos = (float) Math.cos(r);
        float sin = (float) Math.sin(r);

        result.elements[0 + 0 * 4] = cos;
        result.elements[1 + 0 * 4] = sin;

        result.elements[0 + 1 * 4] = -sin;
        result.elements[1 + 1 * 4] = cos;

        return result;
    }

    /**
     * @param matrix
     * @return
     */
    public Mat4f multiply(Mat4f matrix)
    {
        final Mat4f result = new Mat4f();
        for (int y = 0; y < 4; y++)
        {
            for (int x = 0; x < 4; x++)
            {
                float sum = 0.0f;
                for (int e = 0; e < 4; e++)
                {
                    sum += this.elements[x + e * 4] * matrix.elements[e + y * 4];
                }
                result.elements[x + y * 4] = sum;
            }
        }
        return result;
    }

}