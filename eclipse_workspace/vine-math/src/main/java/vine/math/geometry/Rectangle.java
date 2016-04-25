package vine.math.geometry;

import vine.math.Vec2f;
import vine.math.geometry.ray.Intersection;
import vine.math.geometry.ray.Ray;

/**
 * @author Steffen
 *
 */
public class Rectangle implements Shape
{
    private static final float SKIN_WIDTH = -0.000001f;
    private final Vec2f     origin;
    private final Vec2f     diagonal;

    /**
     * Creates a new rectangle object, that represent the geometric structure.
     * 
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public Rectangle(float x, float y, float width, float height)
    {
        origin = new Vec2f(x, y);
        diagonal = new Vec2f(width, height);
    }

    public float getWidth()
    {
        return diagonal.getX();
    }

    public float getHeight()
    {
        return diagonal.getY();
    }

    public final boolean contains(float x, float y)
    {
        float a = (x - origin.getX()) / diagonal.getX();
        float b = (y - origin.getY()) / diagonal.getY();
        return a > SKIN_WIDTH && a <= 1 - SKIN_WIDTH && b <= 1 - SKIN_WIDTH && b > SKIN_WIDTH;
    }

    public Intersection intersect(Ray ray)
    {
        // Find min and max X for the segment

        double minX = ray.getOrigin().getX();
        double maxX = ray.getOrigin().getX() + ray.getDirection().getX();

        if (minX > maxX)
        {
            double temp = minX;
            minX = maxX;
            maxX = temp;
        }

        // Find the intersection of the segment's and rectangle's x-projections

        if (maxX > origin.getX() + diagonal.getX())
        {
            maxX = origin.getX() + diagonal.getX();
        }

        if (minX < origin.getX())
        {
            minX = origin.getX();
        }

        if (minX > maxX) // If their projections do not intersect return false
        {
            return new Intersection(false, 0, ray, this);
        }

        // Find corresponding min and max Y for min and max X we found before

        double minY = ray.getOrigin().getY();
        double maxY = ray.getOrigin().getY() + ray.getDirection().getY();

        double dx = ray.getDirection().getX();

        if (Math.abs(dx) > 0.0000001)
        {
            double a = ray.getDirection().getY() / dx;
            double b = ray.getOrigin().getY() - a * ray.getOrigin().getX();
            minY = a * minX + b;
            maxY = a * maxX + b;
        }

        if (minY > maxY)
        {
            double tmp = maxY;
            maxY = minY;
            minY = tmp;
        }

        // Find the intersection of the segment's and rectangle's y-projections

        if (maxY > origin.getY() + diagonal.getY())
        {
            maxY = origin.getY() + diagonal.getY();
        }

        if (minY < origin.getY())
        {
            minY = origin.getY();
        }

        if (minY > maxY) // If Y-projections do not intersect return false
        {
            return new Intersection(false, 0, ray, this);
        }

        return new Intersection(true, 0, ray, this);
    }

    public boolean intersect(Rectangle rect)
    {
        if (rect.origin.getX() < this.origin.getX() + this.diagonal.getX()
                && this.origin.getX() < rect.origin.getX() + rect.diagonal.getX()
                && rect.origin.getY() < this.origin.getY() + this.diagonal.getY())
        {
            return this.origin.getY() < rect.origin.getY() + rect.diagonal.getY();
        } else
        {
            return false;
        }
    }
}
