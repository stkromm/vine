package vine.event;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import vine.event.Event.EventType;
import vine.game.GameObject;

/**
 * Provides a unique rendering method with post processing, global screnn
 * maniuplation for a set of gameobjects. Offers no semantic ordering of
 * gameobjects and is not relevant for physics or audio.
 * 
 * @author Steffen
 * 
 */
public class EventListener {
    EventType type;
    /**
     * 
     */
    public final Set<GameObject> handler;

    /**
     * @param key
     * 
     */
    public EventListener(final EventType key) {
        handler = new HashSet<>();
        type = key;
    }

    /**
     * @param handler
     *            Adds the handler to the eventlayer.
     */
    public void addEventHandler(final GameObject handler) {
        this.handler.add(handler);
    }

    /**
     * @param event
     *            The event that will be processed by this layer
     * @return True, if the event was consumed
     */
    public boolean onEvent(final Event event) {
        if (event.getType() == type) {
            final Optional<GameObject> opt = handler.stream().filter(h -> h.onKeyEvent((KeyEvent) event)).findFirst();
            return opt.isPresent();
        }
        return false;
    }

}
