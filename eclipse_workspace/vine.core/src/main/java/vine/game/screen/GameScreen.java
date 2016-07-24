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
        aspect = (float) height / width;
        this.window = window;
        viewport = new Viewport();
        this.height = height;
        this.width = width;
        calculateViewport(window.getHeight(), window.getWidth(), (float) height / width);
        projection = GameScreen.calculateProjection(width, height);
    }

    private final static Mat4f calculateProjection(final float width, final float height)
    {
        return Mat4f.orthographic(-width / 2.f, width / 2.f, -height / 2.f, height / 2.f, -1.0f, 1.0f);
    }

    @Override
    public final void calculateViewport(final float windowHeight, final float windowWidth, final float aspectRatio)
    {
        final int totalOffset = Math.round(windowWidth - windowHeight / aspectRatio);
        viewport.setLeftOffset(totalOffset / 2);
        viewport.setRightOffset(totalOffset / 2);
        Log.lifecycle("Calculated screen side offset:" + viewport.getLeftOffset());
        viewport.setBottomOffset(0);
        viewport.setTopOffset(0);
        projection = GameScreen.calculateProjection(width, height);
    }

    @Override
    public final Viewport getViewport()
    {
        return viewport;
    }

    @Override
    public final int getWidth()
    {
        return width;
    }

    @Override
    public final int getHeight()
    {
        return height;
    }

    @Override
    public final float getUnitsPerPixel()
    {
        return (window.getWidth() - viewport.getLeftOffset() * 2.f) / width;
    }

    @Override
    public final float getAspect()
    {
        return aspect;
    }

    @Override
    public final Mat4f getProjection()
    {
        return projection;
    }

    @Override
    public final void setWidth(final int width)
    {
        this.width = width;
        calculateViewport(window.getHeight(), window.getWidth(), aspect);
        projection = GameScreen.calculateProjection(this.width, height);
    }

    @Override
    public final void setHeight(final int height)
    {
        this.height = height;
        calculateViewport(window.getHeight(), window.getWidth(), aspect);
        projection = GameScreen.calculateProjection(width, this.height);
    }

    @Override
    public final float worldToScreenCoord(final float x)
    {
        return x * getUnitsPerPixel();
    }

    @Override
    public Window getWindow()
    {
        return window;
    }
}
