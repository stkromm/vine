package vine.graphics;

public final class GraphicsProvider
{
    private static Graphics graphics;

    private GraphicsProvider()
    {

    }

    public static Graphics getGraphics()
    {
        return GraphicsProvider.graphics;

    }

    public static void setGraphics(final Graphics graphics)
    {
        if (graphics == null)
        {
            throw new IllegalArgumentException("Can't pass null as graphics");
        }
        GraphicsProvider.graphics = graphics;
    }
}
