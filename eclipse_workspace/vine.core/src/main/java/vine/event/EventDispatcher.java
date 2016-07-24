package vine.event;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

/**
 * @author Steffen
 *
 */
public final class EventDispatcher
{
    /**
     * Stores all event layers of the game.
     */
    private final Deque<EventListener> layers;

    /**
     * 
     */
    public EventDispatcher()
    {
        this.layers = new ArrayDeque<>();
    }

    /**
     * @param layer
     *            The event layer, that will be used to receive events.
     */
    public void registerListener(final EventListener layer)
    {
        this.layers.addLast(layer);
    }

    /**
     * @param event
     *            An event, that will be assigned to each handler of each event
     *            layer, until a handler in a layer consumes the event.
     */
    public void dispatch(final Event event)
    {
        final Iterator<EventListener> it = this.layers.descendingIterator();
        while (it.hasNext())
        {
            if (it.next().onEvent(event))
            {
                return;
            }
        }
    }
}
