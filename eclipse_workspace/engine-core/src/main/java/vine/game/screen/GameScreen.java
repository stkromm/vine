package vine.game.screen;

import vine.math.matrix.Mat4f;
import vine.util.Log;
import vine.window.Window;

/**
 * @author Steffen
 *
 */
public class GameScreen implements Screen
{

    private final Window   window;
    private final Viewport viewport;
    private int            height;
    private int            width;
    private final float    aspect;
    private volatile Mat4f projection;

    /**
     * @param window
     *            The window, that contains this screen.
     * @param width
     *            The width of the displayed screen on the window
     * @param height
     *            The height of the displayed screen on the window
     */
    public GameScreen(final Window window, final int width, final int height)
    {
        super();
        this.aspect = (float) height / width;
        this.window = window;
        this.viewport = new Viewport();
        this.height = height;
        this.width = width;
        calculateViewport(window.getHeight(), window.getWidth(), (float) height / width);
        this.projection = GameScreen.calculateProjection(width, height);
    }

    private final static Mat4f calculateProjection(final float width, final float height)
    {
        return Mat4f.orthographic(-width / 2.f, width / 2.f, -height / 2.f, height / 2.f, -1.0f, 1.0f);
    }

    @Override
    public final void calculateViewport(final float windowHeight, final float windowWidth, final float aspectRatio)
    {
        final int totalOffset = Math.round(windowWidth - windowHeight / aspectRatio);
        this.viewport.setLeftOffset(totalOffset / 2);
        this.viewport.setRightOffset(totalOffset / 2);
        Log.lifecycle("Calculated screen side offset:" + this.viewport.getLeftOffset());
        this.viewport.setBottomOffset(0);
        this.viewport.setTopOffset(0);
        this.projection = GameScreen.calculateProjection(this.width, this.height);
    }

    @Override
    public final Viewport getViewport()
    {
        return this.viewport;
    }

    @Override
    public final int getWidth()
    {
        return this.width;
    }

    @Override
    public final int getHeight()
    {
        return this.height;
    }

    @Override
    public final float getUnitsPerPixel()
    {
        return (this.window.getWidth() - this.viewport.getLeftOffset() * 2.f) / this.width;
    }

    @Override
    public final float getAspect()
    {
        return this.aspect;
    }

    @Override
    public final Mat4f getProjection()
    {
        return this.projection;
    }

    @Override
    public final void setWidth(final int width)
    {
        this.width = width;
        calculateViewport(this.window.getHeight(), this.window.getWidth(), this.aspect);
        this.projection = GameScreen.calculateProjection(this.width, this.height);
    }

    @Override
    public final void setHeight(final int height)
    {
        this.height = height;
        calculateViewport(this.window.getHeight(), this.window.getWidth(), this.aspect);
        this.projection = GameScreen.calculateProjection(this.width, this.height);
    }

    @Override
    public final float worldToScreenCoord(final float x)
    {
        return x * getUnitsPerPixel();
    }
}
