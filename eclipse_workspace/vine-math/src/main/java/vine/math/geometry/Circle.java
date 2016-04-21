package vine.math.geometry;

import vine.math.Vector2f;
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
    private final Vector2f distanceVector = new Vector2f(0, 0);

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
        Vector2f rayOrigin = ray.getOrigin();
        Vector2f rayDirection = ray.getDirection();
        Vector2f f = new Vector2f(-x, -y);
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
