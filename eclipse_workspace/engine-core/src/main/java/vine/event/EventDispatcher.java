package vine.event;

import java.util.ArrayDeque;
import java.util.Deque;

import vine.event.Event.EventType;
import vine.event.EventListener.EventHandler;

/**
 * @author Steffen
 *
 */
public final class EventDispatcher {
    /**
     * Stores all event layers of the game.
     */
    private final Deque<EventListener> layers;

    /**
     * 
     */
    public EventDispatcher() {
        layers = new ArrayDeque<>();
    }

    /**
     * @param layer
     *            The event layer, that will be used to receive events.
     */
    public void registerListener(final EventListener layer) {
        layers.addLast(layer);
    }

    /**
     * @param name
     * @param handler
     * @param type
     */
    public void registerHandler(final String name, final EventHandler handler, final EventType type) {
        for (final EventListener listener : layers) {
            if (listener.type == type) {
                listener.addEventHandler(name, handler);
            }
        }
    }

    /**
     * @param name
     */
    public void unregisterHandler(final String name) {
        for (final EventListener listener : layers) {
            listener.handler.remove(name);
        }
    }

    /**
     * @param event
     *            An event, that will be assigned to each handler of each event
     *            layer, until a handler in a layer consumes the event.
     */
    public void dispatch(final Event event) {
        layers.stream().filter(e -> e.onEvent(event)).findFirst();
    }
}
