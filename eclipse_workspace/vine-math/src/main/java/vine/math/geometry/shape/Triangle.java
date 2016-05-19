package vine.math.geometry.shape;

import static vine.math.geometry.shape.ShapeUtil.triangleArea;
import static vine.math.geometry.shape.ShapeUtil.trianglePerimeter;

import java.io.Serializable;

import vine.math.vector.Vec2f;

public class Triangle implements Shape, Serializable
{
    private static final long serialVersionUID = -6160427959674957669L;

    float                     x1, y1;
    float                     x2, y2;
    float                     x3, y3;

    @Override
    public boolean contains(final float x, final float y)
    {
        return false;
    }

    @Override
    public boolean contains(final Vec2f point)
    {
        return contains(point.getX(), point.getY());
    }

    @Override
    public float getArea()
    {
        return triangleArea(x1, y1, x2, y2, x3, y3);
    }

    @Override
    public float getCircumference()
    {
        return trianglePerimeter(x1, y1, x2, y2, x3, y3);
    }

    @Override
    public String toString()
    {
        return super.toString() + " center:[(" + x1 + "," + y1 + "),(" + x2 + y2 + "),(" + x3 + "," + y3 + ")";
    }

    @Override
    public int hashCode()
    {
        int result = 1;
        result += result * 13 + Float.floatToIntBits(x1 * y1);
        result += result * 7 + Float.floatToIntBits(x2 * y2);
        result += result * 2 + Float.floatToIntBits(x3 * y3);
        return result;
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object == this)
        {
            return true;
        }
        if (!(object instanceof Triangle))
        {
            return false;
        }
        final Triangle triangle = (Triangle) object;
        return triangle.x1 == x1 && triangle.y1 == y1 && //
                triangle.x2 == x2 && triangle.y2 == y2 && //
                triangle.x3 == x3 && triangle.y3 == y3;
    }
}
