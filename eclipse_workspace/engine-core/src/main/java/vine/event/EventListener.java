package vine.event;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import vine.event.Event.EventType;

/**
 * @author Steffen
 * 
 */
public class EventListener {
    /**
     * 
     */
    EventType type;
    /**
     * 
     */
    public final Map<String, EventHandler> handler;

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
    public EventListener(final EventType key) {
        handler = new HashMap<>();
        type = key;
    }

    /**
     * @param name
     * @param handler
     *            Adds the handler to the eventlayer.
     */
    public void addEventHandler(final String name, final EventHandler handler) {
        this.handler.put(name, handler);
    }

    /**
     * @param event
     *            The event that will be processed by this layer
     * @return True, if the event was consumed
     */
    public boolean onEvent(final Event event) {
        if (event.getType() == type) {
            final Optional<EventHandler> opt = handler.values().stream().filter(h -> h.handle(event)).findFirst();
            return opt.isPresent();
        }
        return false;
    }

}
