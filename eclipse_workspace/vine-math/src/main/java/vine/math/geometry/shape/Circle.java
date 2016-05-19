package vine.math.geometry.shape;

import static vine.math.geometry.shape.ShapeUtil.circleArea;
import static vine.math.geometry.shape.ShapeUtil.circleContainsPoint;
import static vine.math.geometry.shape.ShapeUtil.circlePerimeter;

import java.io.Serializable;

import vine.math.vector.Vec2f;

public class Circle implements Shape, Serializable
{
    private static final long serialVersionUID = -3376067757270617966L;
    protected float           x;
    protected float           y;
    protected float           radius;

    public Circle(final Circle circle)
    {
        if (circle == null)
        {
            throw new IllegalArgumentException(
                    "Tried to construct a Circle Object with a given Circle object that is null.");
        }
        x = circle.x;
        y = circle.y;
        radius = circle.radius;
    }

    public Circle(final Vec2f center, final float radius)
    {
        if (center == null)
        {
            throw new IllegalArgumentException(
                    "Tried to construct a Circle Object with a given Vec2f object that is null.");
        }
        this.radius = radius;
        x = center.getX();
        y = center.getY();
    }

    public Circle(final float x, final float y, final float radius)
    {
        this.radius = Math.abs(radius);
        this.x = x;
        this.y = y;
    }

    /**
     * Getter.
     *
     * @return the x coordinate of the center of the circle
     */
    public final float getX()
    {
        return x;
    }

    /**
     * Getter.
     *
     * @return the y coordinate of the center of the circle
     */
    public final float getY()
    {
        return y;
    }

    /**
     * Getter.
     *
     * @return the radius of this circle.
     */
    public final float getRadius()
    {
        return radius;
    }

    @Override
    public final boolean contains(final Vec2f point)
    {
        return circleContainsPoint(x, y, radius, point.getX(), point.getY());
    }

    @Override
    public final boolean contains(final float pointX, final float pointY)
    {
        return circleContainsPoint(x, y, radius, pointX, pointY);
    }

    @Override
    public final float getArea()
    {
        return circleArea(radius);
    }

    @Override
    public final float getCircumference()
    {
        return circlePerimeter(radius);
    }

    @Override
    public final String toString()
    {
        return super.toString() + " center:(" + x + "," + y + "),radius:" + radius;
    }

    @Override
    public final int hashCode()
    {
        int result = 1;
        result += result * 7 + Float.floatToIntBits(x);
        result += result * 2 + Float.floatToIntBits(y);
        result += result * 13 + Float.floatToIntBits(radius);
        return result;
    }

    @Override
    public final boolean equals(final Object object)
    {
        if (object == this)
        {
            return true;
        }
        if (!(object instanceof Circle))
        {
            return false;
        }
        final Circle circle = (Circle) object;
        return circle.x == x && circle.y == y && circle.radius == radius;
    }
}
