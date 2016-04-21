package vine.math.geometry.ray;

import vine.math.Vector2f;

public class Ray
{
    Vector2f origin;
    Vector2f direction;

    /**
     * Creates a ray with the given origin, that goes to the given direction.
     * The direction object is going to be normalized.
     */
    public Ray(Vector2f origin, Vector2f direction)
    {
        this.origin = origin;
        direction.normalize();
        this.direction = direction;
    }

    public Vector2f getOrigin()
    {
        return origin;
    }

    public Vector2f getDirection()
    {
        return direction;
    }
}
