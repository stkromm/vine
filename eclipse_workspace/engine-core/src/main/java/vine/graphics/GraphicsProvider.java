package vine.graphics;

public class GraphicsProvider {

    private static Graphics graphics;

    public static final Graphics getGraphics() {
        return graphics;

    }

    public static final void setGraphics(Graphics graphics) {
        GraphicsProvider.graphics = graphics;
    }
}
