package vine.graphics;

import java.util.ArrayDeque;
import java.util.Deque;

import vine.game.Layer;
import vine.game.screen.Screen;
import vine.input.Input;
import vine.window.Window;

public class RenderStack
{
    private final Deque<Layer> layers = new ArrayDeque<>();
    private boolean            resizeViewport;
    private final Graphics     graphics;
    private final Screen       screen;
    private final Window       window;
    private final Input        input;

    public RenderStack(final Layer[] layers, final Window window, final Screen screen, final Input input)
    {
        this.graphics = GraphicsProvider.getGraphics();
        this.screen = screen;
        this.input = input;
        this.window = window;
        for (final Layer layer : layers)
        {
            this.layers.push(layer);
        }
    }

    public final void init()
    {
        GraphicsProvider.getGraphics().makeContext(this.window.getContext());
        GraphicsProvider.getGraphics().init();
        this.window.setWindowContextCallback(context ->
        {
            GraphicsProvider.getGraphics().makeContext(context);
            GraphicsProvider.getGraphics().init();
            this.input.listenToWindow(context);
        });
        this.window.setSizeCallback((w, h) ->
        {
            this.window.setWindowSize(w, h);
            this.screen.calculateViewport(h, w, this.screen.getAspect());
            this.resizeViewport = true;
        });
    }

    public final void render()
    {
        if (this.resizeViewport)
        {
            GraphicsProvider.getGraphics().setViewport(this.screen.getViewport().getLeftOffset(),
                    this.screen.getViewport().getTopOffset(),
                    this.window.getWidth() - 2 * this.screen.getViewport().getRightOffset(),
                    this.window.getHeight() - this.screen.getViewport().getBottomOffset());
            this.resizeViewport = false;
        }
        this.graphics.clearBuffer();
        for (final Layer layer : this.layers)
        {
            layer.render(this.screen);
        }
        this.graphics.swapBuffer();
    }
}
