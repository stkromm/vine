package vine.math;

/**
 * @author Steffen
 *
 */
public class Vector3f {
    /**
     * Maximum difference two floating point values can differ and still count
     * as equal.
     */
    protected static final float EPSILON = 0.000001f;
    private float x;
    private float y;
    private float z;

    /**
     * Creates a new Vector3f with the given x,y and z elements.
     * 
     * @param x
     *            The x element of the new vector
     * @param y
     *            The y element of the new vector
     * @param z
     *            The z element of the new vector
     */
    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * @return The x element value
     */
    public float getX() {
        return x;
    }

    /**
     * @param x
     *            The new x element value
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * @return y element value
     */
    public float getY() {
        return y;
    }

    /**
     * @param y
     *            The new y element value
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * @return The z element value
     */
    public float getZ() {
        return z;
    }

    /**
     * @param z
     *            The new z element value
     */
    public void setZ(float z) {
        this.z = z;
    }

    /**
     * Adds the given values to the corresponding elements of this Vector3f.
     * 
     * @param x
     *            The addition to the x element
     * @param y
     *            The addition to the y element
     * @param z
     *            The addition to the z element
     */
    public void add(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
    }

    /**
     * Adds the elements of the given vector the elements of this Vector3f.
     * 
     * @param vector
     *            The vector, which elements are added to the corresponding
     *            elements of this vector.
     */
    public void add(Vector3f vector) {
        if (vector == null) {
            return;
        }
        add(vector.getX(), vector.getY(), vector.getZ());
    }

    /**
     * Returns the dot product of this Vector2f and the given. Callers guarantee
     * that the given vector is a valid object.
     * 
     * @param vector
     *            The vector used to calculate a dot product with this vector
     * @return The dot product of this and the given vector
     */
    public strictfp float dot(Vector3f vector) {
        return vector == null ? 0 : vector.getX() * x + vector.getY() * y + z * vector.getZ();
    }

    /**
     * Multiplies the elements of this vector with the given scale float value.
     * 
     * @param factor
     *            The factor that is multiplied with the elements of this
     *            vector.
     */
    public void scale(double factor) {
        x *= factor;
        y *= factor;
        z *= factor;
    }

    /**
     * Returns the length of this Vector2f.
     * 
     * @return The length of this vector
     */
    public double length() {
        return Math.sqrt(dot(this));
    }

    /**
     * Calculates the inner angle between this and the given vector.
     * 
     * @param vector
     *            The vector, which angle between this vector is calculated
     * @return The angle between this and the given vector.
     */
    public double getAngle(Vector3f vector) {
        if (vector == null) {
            return 0;
        }
        final double length = length();
        if (length <= EPSILON) {
            return 0;
        }
        final double vectorLength = vector.length();
        if (vectorLength <= EPSILON) {
            return 0;
        }
        return this.dot(vector) / (length * vectorLength);
    }

    /**
     * Calculates the cross product of this and the given vector.
     * 
     * @param vector
     *            The vector which is cross multiplied with this vector
     * @return The cross product of this and the given vector, which is a
     *         Vector3f object.
     */
    public Vector3f cross(Vector3f vector) {
        if (vector == null) {
            return new Vector3f(0, 0, 0);
        }
        return new Vector3f(y * vector.getZ() - z * vector.getY(), z * vector.getX() - x * vector.getZ(),
                x * vector.getY() - y * vector.getX());
    }

    /**
     * Normalizes this vector.
     */
    public void normalize() {
        final float length = dot(this);
        if (length <= EPSILON) {
            return;
        }
        final float inversedLength = (float) (1 / Math.sqrt(dot(this)));
        scale(inversedLength);
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) {
            return false;
        }
        if (!(object instanceof Vector3f)) {
            return false;
        }
        final Vector3f vector = (Vector3f) object;
        return Math.abs(vector.getX() - x + vector.getY() - y + vector.getZ() - z) <= 3 * EPSILON;
    }

}
