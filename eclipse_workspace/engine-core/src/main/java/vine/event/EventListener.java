package vine.event;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import vine.event.Event.EventType;

/**
 * @author Steffen
 * 
 */
public class EventListener {
    /**
     * 
     */
    final Map<EventType, Deque<EventHandler>> handler;

    /**
     * @author Steffen
     *
     */
    public interface EventHandler {
        /**
         * @param event
         * @return
         */
        boolean handle(Event event);
    }

    /**
     * @param key
     * 
     */
    public EventListener() {
        this.handler = new HashMap<>();
        for (final EventType type : EventType.values()) {
            this.handler.put(type, new ArrayDeque<EventHandler>());
        }
    }

    /**
     * @param name
     * @param handler
     *            Adds the handler to the eventlayer.
     */
    public final void addEventHandler(final EventType type, final EventHandler handler) {
        this.handler.get(type).add(handler);
    }

    /**
     * @param event
     *            The event that will be processed by this layer
     * @return True, if the event was consumed
     */
    public final boolean onEvent(final Event event) {
        final Iterator<EventHandler> it = this.handler.get(event.getType()).descendingIterator();
        while (it.hasNext()) {
            if (it.next().handle(event)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param handler
     */
    public void remove(final EventHandler handler) {
        this.handler.remove(handler);
    }

}
