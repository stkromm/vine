package vine.math.geometry.shape;

import java.io.Serializable;

import vine.math.vector.Vec2f;

/**
 * Axis aligned bounding box. An AABB is a Rectangle, which sides are
 * "axis aligned" as the name suggests. It's defined by the origin, which is
 * always the lower left corner of the rectangle and by an 2 dimensional vector
 * extend, which defines the extends of the rectangle in the corresponding axis.
 *
 * @author Steffen Kromm, first created on 06.05.2016
 *
 */
public class AABB implements Shape, Serializable
{
    private static final long serialVersionUID = -4819496554656836334L;
    protected float           x;
    protected float           y;
    protected float           width;
    protected float           height;

    public AABB(final float x, final float y, final float width, final float height)
    {
        this.x = x;
        this.y = y;
        this.width = Math.abs(width);
        this.height = Math.abs(height);
    }

    public AABB(final Vec2f origin, final Vec2f extend)
    {
        if (origin == null || extend == null)
        {
            throw new IllegalArgumentException("Tried to create AABB with null");
        }
        x = origin.getX();
        y = origin.getY();
        width = Math.abs(extend.getX());
        height = Math.abs(extend.getY());
    }

    /**
     * Copy constructor for a AABBs.
     *
     * @param aabb
     *            The aabb to copy.
     */
    public AABB(final AABB aabb)
    {
        x = aabb.x;
        y = aabb.y;
        width = aabb.width;
        height = aabb.height;
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
        return width;
    }

    public float getHeight()
    {
        return height;
    }

    @Override
    public boolean contains(final float x, final float y)
    {
        return x >= this.x && y >= this.y && //
                x <= this.x + width && y <= this.y + height;
    }

    @Override
    public boolean contains(final Vec2f point)
    {
        return point.getX() >= x && point.getY() >= y && //
                point.getX() <= x + width && point.getY() <= y + height;
    }

    @Override
    public float getArea()
    {
        return height * width;
    }

    @Override
    public float getCircumference()
    {
        return 2 * width + 2 * height;
    }

    @Override
    public int hashCode()
    {
        int result = 1;
        result += result * 2 + Float.floatToIntBits(x);
        result += result * 5 + Float.floatToIntBits(y);
        result += result * 7 + Float.floatToIntBits(width);
        result += result * 11 + Float.floatToIntBits(height);
        return result;
    }

    @Override
    public String toString()
    {
        return "AABB Origin(" + x + "," + "y" + "),Extends(" + width + "," + height + ")";
    }

    @Override
    public boolean equals(final Object object)
    {
        if (!(object instanceof AABB))
        {
            return false;
        }
        final AABB aabb = (AABB) object;
        return x == aabb.x && y == aabb.y && width == aabb.width && height == aabb.height;
    }
}
