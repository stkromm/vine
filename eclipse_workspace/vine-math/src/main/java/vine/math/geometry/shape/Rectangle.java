package vine.math.geometry.shape;

import java.io.Serializable;

import vine.math.vector.Vec2f;
import vine.math.vector.Vec2Util;

public class Rectangle implements Shape, Serializable
{
    private static final long serialVersionUID = 3153100323906506514L;
    /**
     * Lower left corner x coordinate.
     */
    float                     lowerLeftX;
    /**
     * Lower left corner y coordinate.
     */
    float                     lowerLeftY;
    /**
     * Lower right corner x coordinate.
     */
    float                     lowerRightX;
    /**
     * Lower right corner y coordinate.
     */
    float                     lowerRightY;
    /**
     * Upper left corner x coordinate.
     */
    float                     upperLeftX;
    /**
     * Upper left corner y coordinate.
     */
    float                     upperLeftY;
    /**
     * Width of the rectangle.
     */
    float                     width;
    /**
     * Height of the rectangle.
     */
    float                     height;

    /**
     * Creates a new rectangle defined by the given lower left and right and
     * upper left corner.
     */
    public Rectangle(final float x1, final float y1, final float x2, final float y2, final float x3, final float y3)
    {
        // TODO Check if the points are in valid order and - reorder ?
        lowerLeftX = x1;
        lowerLeftY = y1;
        lowerRightX = x2;
        lowerRightY = y2;
        upperLeftX = x3;
        upperLeftY = y3;
        height = (float) Vec2Util.length(x3 - x1, y3 - y1);
        width = (float) Vec2Util.length(x2 - x1, y2 - y1);
    }

    /**
     * Getter.
     *
     * @return the x coordinate of the lower left corner of the rectangle.
     */
    public final float getLowerLeftX()
    {
        return lowerLeftX;
    }

    /**
     * Getter.
     *
     * @return the y coordinate of the lower left corner of the rectangle.
     */
    public final float getLowerLeftY()
    {
        return lowerLeftY;
    }

    /**
     * Getter.
     *
     * @return the y coordinate of the lower left corner of the rectangle.
     */
    public final float getLowerRightX()
    {
        return lowerRightX;
    }

    /**
     * Getter.
     *
     * @return the y coordinate of the lower left corner of the rectangle.
     */
    public final float getLowerRightY()
    {
        return lowerRightY;
    }

    /**
     * Getter.
     *
     * @return the y coordinate of the lower left corner of the rectangle.
     */
    public final float getUpperLeftX()
    {
        return upperLeftX;
    }

    /**
     * Getter.
     *
     * @return the y coordinate of the lower left corner of the rectangle.
     */
    public final float getUpperLeftY()
    {
        return upperLeftY;
    }

    /**
     * Getter.
     *
     * @return the width of the rectangle, that is, the extend in x direction.
     */
    public final float getWidth()
    {
        return width;
    }

    /**
     * Getter.
     *
     * @return the height of the rectangle, that is, the extend in y direction.
     */
    public final float getHeight()
    {
        return height;
    }

    @Override
    public final boolean contains(final float x, final float y)
    {
        return x >= lowerLeftX && x <= lowerLeftX + width && y >= lowerLeftY && y <= lowerLeftY + height;
    }

    @Override
    public final boolean contains(final Vec2f point)
    {
        return contains(point.getX(), point.getY());
    }

    @Override
    public final float getArea()
    {
        return width * height;
    }

    @Override
    public final float getCircumference()
    {
        return 2 * height + 2 * width;
    }

    @Override
    public final String toString()
    {
        return super.toString() + " lowerLeftCorner:(" + lowerLeftX + "," + lowerLeftY + "),lowerRightCorner:("
                + lowerRightX + "," + lowerRightY + "),upperLeftCorner:(" + upperLeftX + upperLeftY + ")";
    }

    @Override
    public final int hashCode()
    {
        int result = 1;
        result += result * 2 + Float.floatToIntBits(lowerLeftX);
        result += result * 7 + Float.floatToIntBits(lowerLeftY);
        result += result * 11 + Float.floatToIntBits(lowerRightX);
        result += result * 13 + Float.floatToIntBits(lowerRightY);
        result += result * 17 + Float.floatToIntBits(upperLeftX);
        result += result * 23 + Float.floatToIntBits(upperLeftY);
        return result;
    }

    @Override
    public final boolean equals(final Object object)
    {
        if (object == this)
        {
            return true;
        }
        if (!(object instanceof Rectangle))
        {
            return false;
        }
        final Rectangle rectangle = (Rectangle) object;
        return rectangle.lowerLeftX == lowerLeftX && rectangle.lowerLeftY == lowerLeftY
                && rectangle.lowerRightX == lowerRightX && rectangle.lowerRightY == lowerRightY
                && rectangle.upperLeftX == upperLeftX && rectangle.upperLeftY == upperLeftY && rectangle.width == width
                && rectangle.height == height;
    }
}
