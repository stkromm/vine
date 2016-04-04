package vine.gui;

import java.util.ArrayList;
import java.util.List;

import vine.event.Event.EventType;
import vine.event.EventListener;
import vine.event.EventListener.EventHandler;
import vine.event.KeyEvent;
import vine.game.Layer;
import vine.game.screen.Screen;
import vine.graphics.GUIRenderer;

/**
 * @author Steffen
 *
 */
public class GameUserInterface implements Layer {
    private EventListener listener = new EventListener();
    private List<Widget> widgets = new ArrayList<>();
    private GUIRenderer renderer = new GUIRenderer();

    /**
     * 
     */
    public GameUserInterface() {
        final EventHandler handler = event -> {
            for (final Widget widget : this.widgets) {
                if (widget.selected) {
                    widget.onKeyEvent((KeyEvent) event);
                }
            }
            return false;
        };
        this.listener.addEventHandler(EventType.KEY, handler);
    }

    @Override
    public String getName() {
        return "vine.GUI";
    }

    @Override
    public void render(Screen screen) {
        renderer.renderGUI(widgets, screen);
    }

    @Override
    public EventListener getListener() {
        return this.listener;
    }

    /**
     * @param widget
     *            Widget that is added to the screen
     */
    public void addWidget(Widget widget) {
        this.widgets.add(widget);
    }
}
