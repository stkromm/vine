package vine.math.geometry.shape;

import vine.math.geometry.Transformable;
import vine.math.vector.Vec2f;

public class MutableCircle extends Circle implements Transformable
{
    private static final long serialVersionUID = 3497970490439619403L;

    public MutableCircle(final Circle circle)
    {
        super(circle);
    }

    public MutableCircle(final Vec2f center, final float radius)
    {
        super(center, radius);
    }

    public MutableCircle(final float x, final float y, final float radius)
    {
        super(x, y, radius);
    }

    public final void setX(final float x)
    {
        this.x = x;
    }

    public final void setY(final float y)
    {
        this.y = y;
    }

    public final void setRadius(final float radius)
    {
        this.radius = radius;
    }

    @Override
    public final void translate(final float x, final float y)
    {
        this.x += x;
        this.y += y;
    }

    @Override
    public final void rotate(final float degree)
    {
        // Rotation has no effect on a circle
    }

    @Override
    public final void scale(final float x, final float y)
    {
        this.uniformScale(Math.max(x, y));
    }

    @Override
    public final void uniformScale(final float factor)
    {
        radius *= Math.abs(factor);
    }
}
