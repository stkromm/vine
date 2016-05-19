package vine.math;

import vine.math.vector.Vec2f;

public class CubicBezierCurve
{

    Vec2f b0;
    Vec2f b1;
    Vec2f b2;
    Vec2f b3;

    public CubicBezierCurve(final Vec2f b1, final Vec2f b2, final Vec2f b3, final Vec2f b4)
    {
        b0 = b1;
        this.b1 = b2;
        this.b2 = b3;
        this.b3 = b4;
    }

    public Vec2f getB0()
    {
        return b0;
    }

    public void setB0(final Vec2f b0)
    {
        this.b0 = b0;
    }

    public Vec2f getB1()
    {
        return b1;
    }

    public void setB1(final Vec2f b1)
    {
        this.b1 = b1;
    }

    public Vec2f getB2()
    {
        return b2;
    }

    public void setB2(final Vec2f b2)
    {
        this.b2 = b2;
    }

    public Vec2f getB3()
    {
        return b3;
    }

    public void setB3(final Vec2f b3)
    {
        this.b3 = b3;
    }

    public float getX(final float u)
    {
        final float u2 = u * u;
        final float u3 = u2 * u;
        final float iu = 1 - u;
        final float iu2 = iu * iu;
        final float iu3 = iu2 * iu;

        return iu3 * b0.getX() + //
                3 * u * iu2 * b1.getX() + //
                3 * u2 * iu * b2.getX() + //
                u3 * b3.getX();
    }

    public float getY(final float u)
    {
        final float u2 = u * u;
        final float u3 = u2 * u;
        final float iu = 1 - u;
        final float iu2 = iu * iu;
        final float iu3 = iu2 * iu;

        return iu3 * b0.getY() + //
                3 * u * iu2 * b1.getY() + //
                3 * u2 * iu * b2.getY() + //
                u3 * b3.getY();
    }
}
