package vine.graphics;

public enum DrawPrimitive
{
    POINT(1), TRIANGLE(3), TRIANGLE_STRIP(3), TRIANGLE_FAN(3), LINE(2), LINE_STRIP(2), LINE_LOOP(2);

    public final int NUM_VERTICES;

    DrawPrimitive(final int v)
    {
        NUM_VERTICES = v;
    }
}
