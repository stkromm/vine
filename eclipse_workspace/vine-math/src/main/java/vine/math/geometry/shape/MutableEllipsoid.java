package vine.math.geometry.shape;

import vine.math.geometry.Transformable;

public class MutableEllipsoid extends Ellipsoid implements Transformable
{
    private static final long serialVersionUID = -2096433971741967800L;

    public MutableEllipsoid(final float x, final float y, final float width, final float height)
    {
        super(x, y, width, height);
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
        rotation += degree;
        rotation %= 360;
    }

    @Override
    public void scale(final float x, final float y)
    {
        radius *= x;
        scale *= y;
    }

    @Override
    public void uniformScale(final float factor)
    {
        radius *= factor;
    }

}
