package vine.math.geometry.shape;

import vine.math.geometry.Transformable;
import vine.math.vector.MutableVec2f;

public class MutableRectangle extends Rectangle implements Transformable
{
    private static final long  serialVersionUID = -8791611325962605241L;
    /**
     * Temporary vector to avoid unnecessary allocations when performing
     * transformations on the rectangle.
     */
    private final MutableVec2f tmp              = new MutableVec2f();

    public MutableRectangle(final float x1, final float y1, final float x2, final float y2, final float x3,
            final float y3)
    {
        super(x1, y1, x2, y2, x3, y3);
    }

    public final void setX(final float x)
    {
        lowerLeftX = x;
    }

    public final void setY(final float y)
    {
        lowerLeftY = y;
    }

    public final void setWidth(final float width)
    {
        this.width = width;
    }

    public final void setHeight(final float height)
    {
        this.height = height;
    }

    public final void translate(final float x, final float y)
    {
        lowerLeftX += x;
        lowerLeftY += y;
    }

    public final void rotate(final float degree)
    {
        tmp.set(lowerRightX - lowerLeftX, lowerRightY - lowerLeftY);
        tmp.rotate(degree);
        lowerRightX = lowerLeftX + tmp.getX();
        lowerRightY = lowerLeftY + tmp.getY();

        tmp.set(upperLeftX - lowerLeftX, upperLeftY - lowerLeftY);
        tmp.rotate(degree);
        upperLeftX = lowerLeftX + tmp.getX();
        upperLeftY = lowerLeftY + tmp.getY();
    }

    public final void scale(final float x, final float y)
    {
        tmp.set(lowerRightX - lowerLeftX, lowerRightY - lowerLeftY);
        double previousLength = tmp.length();
        tmp.normalize();
        width = (float) previousLength * x;
        tmp.uniformScale(width);
        lowerRightX = lowerLeftX + tmp.getX();
        lowerRightY = lowerLeftY + tmp.getY();
        tmp.set(upperLeftX - lowerLeftX, upperLeftY - lowerLeftY);
        previousLength = tmp.length();
        tmp.normalize();
        height = (float) (previousLength * x);
        tmp.uniformScale(height);
        upperLeftX = lowerLeftX + tmp.getX();
        upperLeftY = lowerLeftY + tmp.getY();
    }

    public final void uniformScale(final float factor)
    {
        this.scale(factor, factor);
    }
}
