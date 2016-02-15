package vine.event;

import java.util.ArrayDeque;
import java.util.Deque;

import vine.event.Event.EventType;
import vine.game.GameObject;

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

    public void registerHandler(final GameObject object, final EventType type) {
        for (final EventListener listener : layers) {
            if (listener.type == type) {
                listener.addEventHandler(object);
            }
        }
    }

    /**
     * @param object
     */
    public void unregisterHandler(final GameObject object) {
        for (final EventListener listener : layers) {
            listener.handler.remove(object);
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
