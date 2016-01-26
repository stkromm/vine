package vine.event;

/**
 * @author Steffen
 * 
 */
public interface Event {
    /**
     * Used to identify different types of events.
     * 
     * @author Steffen
     *
     */
    enum EventType {
        /**
         * 
         */
        MOUSE_BUTTON,
        /**
        * 
        */
        KEY,
        /**
        * 
        */
        SCROLL,
        /**
        * 
        */
        MOUSE_POSITION,
        /**
        * 
        */
        SENSOR
    }

    /**
     * @return The type of the event
     * @see EventType
     */
    EventType getType();
}
