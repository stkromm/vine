package vine.event;

import vine.event.EventDispatcher.EventHandler;

/**
 * @author Steffen
 *
 */
public interface EventLayer {
    /**
     * @param handler
     *            Adds the handler to the eventlayer.
     */
    void addEventHandler(final EventHandler handler);

    /**
     * @param event
     *            The event that will be processed by this layer
     * @return True, if the event was consumed
     */
    boolean onEvent(final Event event);
}
