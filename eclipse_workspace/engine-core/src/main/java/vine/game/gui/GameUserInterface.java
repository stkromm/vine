package vine.game.gui;

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
    private final EventListener listener = new EventListener();
    private final List<Widget> widgets = new ArrayList<>();
    private final GUIRenderer renderer = new GUIRenderer();

    public GameUserInterface() {
        final EventHandler keyHandler = event -> {
            for (final Widget widget : this.widgets) {
                if (widget.isSelected()) {
                    widget.onKeyEvent((KeyEvent) event);
                }
            }
            return false;
        };
        this.listener.addEventHandler(EventType.KEY, keyHandler);
        final EventHandler mouseMoveHandler = event -> {
            return false;
        };
        this.listener.addEventHandler(EventType.MOUSE_MOVE, mouseMoveHandler);
    }

    @Override
    public String getName() {
        return "vine.GUI";
    }

    @Override
    public void render(Screen screen) {
        this.renderer.renderGUI(this.widgets, screen);
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
