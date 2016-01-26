package vine.event;

import java.util.ArrayDeque;
import java.util.Deque;

import vine.gameplay.scene.Layer;

/**
 * @author Steffen
 *
 */
public final class EventDispatcher {
    /**
     * Stores all event layers of the game.
     */
    private final Deque<Layer> layers;

    /**
     * 
     */
    public EventDispatcher() {
        layers = new ArrayDeque<>();
    }

    /**
     * Used to provide functionality that will be executed on events.
     * 
     * @author Steffen
     *
     */
    @FunctionalInterface
    public interface EventHandler {
        /**
         * @param event
         *            The event, the handler should process
         * @return True, if the event is consumed by the handler and will not be
         *         further propagated to other handlers.
         */
        boolean onEvent(final Event event);
    }

    /**
     * @param layer
     *            The event layer, that will be used to receive events.
     */
    public void registerListener(final Layer layer) {
        layers.addLast(layer);
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
