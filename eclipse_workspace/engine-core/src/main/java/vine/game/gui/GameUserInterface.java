package vine.game.gui;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import vine.event.EventListener;
import vine.game.Layer;
import vine.game.screen.Screen;
import vine.graphics.renderer.GUIRenderer;

/**
 * @author Steffen
 *
 */
public class GameUserInterface implements Layer
{
    private final EventListener               listener = new EventListener();
    private final List<WeakReference<Widget>> widgets  = new ArrayList<>();
    private final Screen                      screen;
    private final GUIRenderer                 renderer = new GUIRenderer();

    public GameUserInterface(Screen screen)
    {
        this.screen = screen;
    }

    @Override
    public String getName()
    {
        return "vine.GUI";
    }

    @Override
    public void render(Screen screen)
    {
        this.renderer.renderGUI(this.widgets, screen);
    }

    @Override
    public EventListener getListener()
    {
        return this.listener;
    }

    /**
     * @param widget
     *            Widget that is added to the screen
     */
    public <T extends Widget> void addWidget(WeakReference<T> widget)
    {
        this.widgets.add(new WeakReference<Widget>(widget.get()));
    }

    public Screen getScreen()
    {
        return this.screen;
    }
}
