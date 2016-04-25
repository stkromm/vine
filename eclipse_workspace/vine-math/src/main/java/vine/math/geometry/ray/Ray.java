package vine.math.geometry.ray;

import vine.math.Vec2f;

public class Ray
{
    Vec2f origin;
    Vec2f direction;

    /**
     * Creates a ray with the given origin, that goes to the given direction.
     * The direction object is going to be normalized.
     */
    public Ray(Vec2f origin, Vec2f direction)
    {
        this.origin = origin;
        if (!direction.isNormalized())
            throw new IllegalArgumentException("Ray direction has to be normalized.");
        this.direction = direction;
    }

    public Vec2f getOrigin()
    {
        return origin;
    }

    public Vec2f getDirection()
    {
        return direction;
    }
}
