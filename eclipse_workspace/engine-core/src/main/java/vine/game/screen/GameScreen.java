package vine.game.screen;

import vine.math.Matrix4f;
import vine.window.Window;

/**
 * @author Steffen
 *
 */
public class GameScreen implements Screen {

    private final Window window;
    private final Viewport viewport;
    private int height;
    private int width;
    private float aspect;
    private volatile Matrix4f projection;

    /**
     * @param window
     *            The window, that contains this screen.
     * @param width
     *            The width of the displayed screen on the window
     * @param height
     *            The height of the displayed screen on the window
     */
    public GameScreen(final Window window, final int width, final int height) {
        super();
        this.aspect = (float) height / width;
        this.window = window;
        this.viewport = new Viewport();
        this.height = height;
        this.width = width;
        GameScreen.calculateViewport(this.viewport, window.getHeight(), window.getWidth(), (float) height / width);
        this.projection = GameScreen.calculateProjection(width, height);

    }

    private final static Matrix4f calculateProjection(final float width, final float height) {
        return Matrix4f.orthographic(-width / 2.f, width / 2.f, -height / 2.f, height / 2.f, -1.0f, 1.0f);
    }

    private final static void calculateViewport(final Viewport viewport, final float windowHeight,
            final float windowWidth, final float aspectRatio) {
        final int totalOffset = Math.round(windowWidth - windowHeight / aspectRatio);
        viewport.setLeftOffset(totalOffset / 2);
        viewport.setRightOffset(totalOffset / 2);
        viewport.setBottomOffset(0);
        viewport.setTopOffset(0);
    }

    @Override
    public final Viewport getViewport() {
        return this.viewport;
    }

    @Override
    public final int getWidth() {
        return this.width;
    }

    @Override
    public final int getHeight() {
        return this.height;
    }

    @Override
    public final float getUnitsPerPixel() {
        return (this.window.getWidth() - this.viewport.getLeftOffset() * 2.f) / this.width;
    }

    @Override
    public final float getAspect() {
        return this.aspect;
    }

    @Override
    public final Matrix4f getOrthographicProjection() {
        return this.projection;
    }

    @Override
    public final void setWidth(final int width) {
        this.width = width;
        this.aspect = (float) this.height / width;
        GameScreen.calculateViewport(this.viewport, this.window.getHeight(), this.window.getWidth(), this.aspect);
        this.projection = GameScreen.calculateProjection(this.width, this.height);
    }

    @Override
    public final void setHeight(final int height) {
        this.height = height;
        this.aspect = (float) height / this.width;
        GameScreen.calculateViewport(this.viewport, this.window.getHeight(), this.window.getWidth(), this.aspect);
        this.projection = GameScreen.calculateProjection(this.width, this.height);
    }

    @Override
    public final float worldToScreenCoord(final float x) {
        return x * this.getUnitsPerPixel();
    }
}
