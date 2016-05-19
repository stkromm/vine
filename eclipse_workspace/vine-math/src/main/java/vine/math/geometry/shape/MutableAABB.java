package vine.math.geometry.shape;

import vine.math.geometry.Transformable;
import vine.math.vector.Vec2f;

public class MutableAABB extends AABB implements Transformable
{
    private static final long serialVersionUID = 7927478055825379266L;

    public MutableAABB(final AABB aabb)
    {
        super(aabb);
    }

    public MutableAABB(final float x, final float y, final float width, final float height)
    {
        super(x, y, width, height);
    }

    public MutableAABB(final Vec2f origin, final Vec2f extend)
    {
        super(origin, extend);
    }

    @Override
    public void translate(final float x, final float y)
    {
        this.x += x;
        this.y += y;
    }

    @Override
    public void rotate(final float degree)
    {

    }

    @Override
    public void scale(final float x, final float y)
    {
        width *= x;
        height *= y;
    }

    @Override
    public void uniformScale(final float factor)
    {
        scale(factor, factor);
    }

    public void setHeight(final float max)
    {
        height = max;
    }

    public void setWidth(final float max)
    {
        width = max;
    }

    public void setX(final float min)
    {
        x = min;
    }

    public void setY(final float min)
    {
        y = min;
    }
}
