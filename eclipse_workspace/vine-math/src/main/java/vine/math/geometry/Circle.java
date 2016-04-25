package vine.math.geometry;

import vine.math.Vec2f;
import vine.math.MutableVec2f;
import vine.math.geometry.ray.Intersection;
import vine.math.geometry.ray.Ray;

/**
 * @author Steffen
 *
 */
public class Circle implements Shape
{
    private float          x;
    private float          y;
    private float          radius;
    private final MutableVec2f distanceVector = new MutableVec2f(0, 0);

    /**
     * Constructs a new circle object, that represents the geometric object.
     * 
     * @param x
     * @param y
     * @param radius
     */
    public Circle(float x, float y, float radius)
    {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public boolean contains(float x, float y)
    {
        distanceVector.setX(x - this.x);
        distanceVector.setY(y - this.y);
        return Math.abs(distanceVector.length()) <= radius;
    }

    public Intersection intersect(Ray ray)
    {
        Vec2f rayOrigin = ray.getOrigin();
        Vec2f rayDirection = ray.getDirection();
        MutableVec2f f = new MutableVec2f(-x, -y);
        f.add(rayOrigin);
        float a = rayDirection.dot(rayDirection);
        float b = 2 * f.dot(rayDirection);
        float c = f.dot(f) - radius * radius;
        double delta = b * b - (4 * a * c);
        if (delta < 0)
        {
            return new Intersection(false, 0, ray, this);
        } else if (delta > 0.0000001)
        { // One intersection
            double squareRootDelta = Math.sqrt(delta);
            float u = (float) (-b + squareRootDelta) / (2 * a);
            return new Intersection(true, u, ray, this);
        } else
        { // Ray tangent the circle
            float u = -b / (2 * a);
            return new Intersection(true, u, ray, this);
        }
    }
}
