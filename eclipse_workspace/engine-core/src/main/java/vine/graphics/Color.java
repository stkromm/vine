package vine.graphics;

public class Color
{
    int rgba;

    public Color(final int rgba)
    {
        this.rgba = rgba;
    }

    public Color(final int r, final int g, final int b)
    {
        this(r, g, b, 0);
    }

    public Color(final int r, final int g, final int b, final int a)
    {
        this.setRed(r);
        this.setBlue(b);
        this.setGreen(g);
        this.setAlpha(a);
    }

    public Color(final Color color)
    {
        this.rgba = color.rgba;
    }

    public int parseRedComponent(final int color)
    {
        return color & 0xFF000000 >> 24;
    }

    public final int getRed()
    {
        return this.parseRedComponent(this.rgba);
    }

    public int parseGreenComponent(final int color)
    {
        return color & 0x00FF0000 >> 16;
    }

    public final int getGreen()
    {
        return this.parseGreenComponent(this.rgba);
    }

    public int parseBlueComponent(final int color)
    {
        return color & 0x0000FF00 >> 8;
    }

    public final int getBlue()
    {
        return this.parseBlueComponent(this.rgba);
    }

    public final int getAlpha()
    {
        return this.rgba & 0x000000FF;
    }

    public int getColor()
    {
        return this.rgba;
    }

    public void setColor(final int r, final int g, final int b)
    {
        this.setColor(r, g, b, 0);
    }

    public void setColor(final int r, final int g, final int b, final int a)
    {
        //
    }

    public void addColor(final int r, final int g, final int b, final int a)
    {
        //
    }

    public void addTransparency(final int alpha)
    {
        //
    }

    public void darken(final float percent)
    {
        //
    }

    public void lighten(final float percent)
    {
        //
    }

    public void normalize()
    {
        //
    }

    public void premultiplyAlpha()
    {
        //
    }

    public void multiply(final Color color)
    {
        //
    }

    public void multiply(final int r, final int g, final int b, final int a)
    {
        //
    }

    public void setRed(final int r)
    {
        this.rgba = this.rgba & 0x00FFFFFF | 0xFF0000 & r;
    }

    public void setGreen(final int g)
    {
        this.rgba = this.rgba & 0xFF00FFFF | 0x00FF0000 & g;
    }

    public void setBlue(final int b)
    {
        this.rgba = this.rgba & 0xFFFF00FF | 0x0000FF00 & b;
    }

    public final void setAlpha(final int i)
    {
        this.rgba = this.rgba & 0xFFFFFF00 | 0x000000FF & i;
    }

    @Override
    public String toString()
    {
        return "Color red:" + this.parseRedComponent(this.rgba) + ",green:" + ",blue:" + "alpha:";

    }

    @Override
    public boolean equals(final Object object)
    {
        if (this == object)
        {
            return true;
        }
        return object != null//
                && object instanceof Color//
                && ((Color) object).rgba == this.rgba;
    }
}
