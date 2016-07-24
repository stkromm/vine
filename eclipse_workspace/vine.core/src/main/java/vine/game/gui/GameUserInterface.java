package vine.game.gui;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import vine.game.Layer;
import vine.game.screen.Screen;
import vine.graphics.renderer.GUIRenderer;

/**
 * @author Steffen
 *
 */
public class GameUserInterface implements Layer
{
    private final List<WeakReference<Widget>> widgets  = new ArrayList<>();
    private final Screen                      screen;
    private final GUIRenderer                 renderer = new GUIRenderer();

    public GameUserInterface(final Screen screen)
    {
        this.screen = screen;
    }

    @Override
    public String getName()
    {
        return "vine.GUI";
    }

    @Override
    public void render(final Screen screen)
    {
        renderer.renderGUI(widgets, screen);
    }

    /**
     * @param widget
     *            Widget that is added to the screen
     */
    public <T extends Widget> void addWidget(final WeakReference<T> widget)
    {
        widgets.add(new WeakReference<Widget>(widget.get()));
    }

    public Screen getScreen()
    {
        return screen;
    }
}
