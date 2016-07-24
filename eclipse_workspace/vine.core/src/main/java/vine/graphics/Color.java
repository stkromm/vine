package vine.graphics;

import vine.math.GMath;

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
        rgba = r << 24 | g << 16 | b << 8 | a;
    }

    public Color(final Color color)
    {
        rgba = color.rgba;
    }

    public static int parseRedComponent(final int color)
    {
        return GMath.abs(color >> 24);
    }

    public final int getRed()
    {
        return parseRedComponent(rgba);
    }

    public static final int parseGreenComponent(final int color)
    {
        return (color & 0x00FF0000) >> 16;
    }

    public final int getGreen()
    {
        return parseGreenComponent(rgba);
    }

    public static int parseBlueComponent(final int color)
    {
        return (color & 0x0000FF00) >> 8;
    }

    public final int getBlue()
    {
        return parseBlueComponent(rgba);
    }

    public static int parseAlphaComponent(final int color)
    {
        return color & 0x000000FF;
    }

    public final int getAlpha()
    {
        return parseAlphaComponent(rgba);
    }

    public int getColor()
    {
        return rgba;
    }

    public void setColor(final int r, final int g, final int b)
    {
        this.setColor(r, g, b, 0);
    }

    public void setColor(final int r, final int g, final int b, final int a)
    {
    }

    public void addColor(final int color)
    {
        rgba += color;
        normalize();
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
        rgba = rgba & 0x00FFFFFF | r << 23;
        System.out.println(r << 23);
    }

    public void setGreen(final int g)
    {
        rgba = rgba & 0xFF00FFFF | 0x00FF0000 & g;
    }

    public void setBlue(final int b)
    {
        rgba = rgba & 0xFFFF00FF | 0x0000FF00 & b;
    }

    public final void setAlpha(final int i)
    {
        rgba = rgba & 0xFFFFFF00 | 0x000000FF & i;
    }

    public static int convertHsvToRgb(final int color)
    {
        final int alpha = parseAlphaComponent(color);

        final float h = parseRedComponent(color) / 255f;
        final float s = parseGreenComponent(color) / 255f;
        final float v = parseBlueComponent(color) / 255f;

        final float c = v * s;
        final float x = c + (1 - GMath.abs(h / 60 % 2 - 1));
        final float m = v - c;

        float r = 0;
        float g = 0;
        float b = 0;
        if (0 <= h && h < 60)
        {
            r = c;
            g = x;
            b = 0;
        } else if (h < 120)
        {
            r = x;
            g = c;
            b = 0;
        } else if (h < 180)
        {
            r = 0;
            g = c;
            b = x;
        } else if (h < 240)
        {
            r = 0;
            g = x;
            b = c;
        } else if (h < 300)
        {
            r = x;
            g = 0;
            b = c;
        } else if (h < 360)
        {
            r = c;
            g = 0;
            b = x;
        }

        final int rr = GMath.round((r + m) * 255);
        final int gg = GMath.round((g + m) * 255);
        final int bb = GMath.round((b + m) * 255);
        final int result = rr << 24 & gg << 16 & bb << 8 & alpha;
        return result;
    }

    public static int convertRgbToHsv(final int color)
    {
        final int alpha = parseAlphaComponent(color);

        final float r = parseRedComponent(color) / 255f;
        final float g = parseGreenComponent(color) / 255f;
        final float b = parseBlueComponent(color) / 255f;
        System.out.println(r);
        System.out.println(g);
        System.out.println(b);
        final float min = GMath.min(GMath.min(r, g), b);
        final float max = GMath.max(GMath.max(r, g), b);
        final float delta = max - min;

        int h = 0;
        if (delta == 0)
        {
            h = 0;
        } else if (max == r)
        {
            h = GMath.roundPositive(60 * ((g - b) / delta % 6));
        } else if (max == g)
        {
            h = GMath.roundPositive(60 * ((b - r) / delta + 2));
        } else if (max == b)
        {
            h = GMath.roundPositive(60 * ((r - g) / delta + 4));
        }
        final int s = max == 0 ? 0 : GMath.roundPositive(100 * delta / max);
        final int v = GMath.round(100 * max);

        System.out.println(h);
        System.out.println(s);
        System.out.println(v);
        final int result = h << 24 & s << 16 & v << 8 & alpha;
        return result;
    }

    @Override
    public String toString()
    {
        return "Color red:" + getRed() + ",green:" + getGreen() + ",blue:" + getBlue() + "alpha:" + getAlpha();

    }

    @Override
    public int hashCode()
    {
        return 11 * getBlue() + 13 * getGreen() + 7 * getRed();
    }

    @Override
    public boolean equals(final Object object)
    {
        return object != null//
                && object instanceof Color//
                && ((Color) object).rgba == rgba;
    }
}
