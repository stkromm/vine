package vine.math.geometry.shape;

import java.io.Serializable;

import vine.math.vector.Vec2Util;
import vine.math.vector.Vec2f;

public class Ellipsoid implements Shape, Serializable
{
    private static final long serialVersionUID = -9151844306521905347L;
    /**
     * Factor, by which the height-radius differs from the radius (width of the
     * ellipsoid).
     */
    protected float           scale;
    protected float           x, y;
    protected float           rotation;
    protected float           radius;

    public Ellipsoid(final float x, final float y, final float width, final float height)
    {
        scale = height / width;
        this.x = x;
        this.y = y;
        radius = width;
    }

    public float getHeight()
    {
        return radius * scale;
    }

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }

    public float getWidth()
    {
        return radius;
    }

    @Override
    public boolean contains(final float pointX, final float pointY)
    {
        final double length = Vec2Util.length(x - pointX, y - pointY * scale);
        return length <= radius;
    }

    @Override
    public boolean contains(final Vec2f point)
    {
        return contains(point.getX(), point.getY());
    }

    @Override
    public float getArea()
    {
        return ShapeUtil.ellipsoidArea(radius, radius * scale);
    }

    @Override
    public float getCircumference()
    {
        return ShapeUtil.ellipsoidPerimeter(radius, radius * scale);
    }
}
