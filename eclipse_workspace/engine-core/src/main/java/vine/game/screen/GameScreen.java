package vine.game.screen;

import vine.math.Matrix4f;
import vine.window.Window;

public class GameScreen implements Screen {

    private final Window window;
    private final Viewport viewport;
    private int height;
    private int width;
    private float aspect;
    private volatile Matrix4f projection;

    /**
     * @param window
     * @param aspectRatio
     * @param width
     */
    public GameScreen(final Window window, final int width, final int height) {
        super();
        this.aspect = (float) height / width;
        this.window = window;
        this.viewport = new Viewport();
        this.height = height;
        this.width = width;
        calculateViewport();
        calculateProjection();

    }

    private final void calculateProjection() {
        this.projection = Matrix4f.orthographic(-width / 2.f, width / 2.f, -height / 2.f, height / 2.f, -1.0f, 1.0f);

    }

    private final void calculateViewport() {
        final float widthInAspectRatio = window.getHeight() / (aspect);
        final float totalOffset = window.getWidth() - widthInAspectRatio;
        viewport.setLeftOffset((int) (totalOffset / 2));
        viewport.setRightOffset((int) (totalOffset / 2));
        viewport.setBottomOffset(0);
        viewport.setTopOffset(0);
    }

    @Override
    public final Viewport getViewport() {
        calculateViewport();
        return viewport;
    }

    @Override
    public final int getWidth() {
        return width;
    }

    @Override
    public final int getHeight() {
        return height;
    }

    @Override
    public final float worldToScreenCoord(final float x) {
        return x * getUnitsPerPixel();
    }

    @Override
    public final float getUnitsPerPixel() {
        return ((float) window.getWidth() - viewport.getLeftOffset() * 2) / width;
    }

    @Override
    public final float getAspect() {
        return aspect;
    }

    @Override
    public final Matrix4f getOrthographicProjection() {
        return projection;
    }

    @Override
    public final void setWidth(final int width) {
        this.width = width;
        this.aspect = (float) height / width;
        calculateViewport();
        calculateProjection();
    }

    @Override
    public final void setHeight(final int height) {
        this.height = height;
        this.aspect = (float) height / width;
        calculateViewport();
        calculateProjection();
    }

}
