package vine.event;

/**
 * @author Steffen
 *
 */
public class MouseButtonEvent implements Event {
    private final int button;
    private final int action;
    private final int mods;
    private final double x;
    private final double y;

    /**
     * @param button
     *            The id of the button
     * @param action
     *            The action of the button (pressen, released)
     * @param mods
     *            The mod with which the button event was generated
     * @param x
     *            The x coordinate of the cursor, when the event was generated
     * @param y
     *            The y coordinate of the cursor, when the event was generated
     */
    public MouseButtonEvent(final int button, final int action, final int mods, final double x, final double y) {
        super();
        this.button = button;
        this.action = action;
        this.mods = mods;
        this.x = x;
        this.y = y;
    }

    /**
     * @return The mouse-button id of the event
     */
    public final int getButton() {
        return button;
    }

    /**
     * @return The action type of the event
     */
    public final int getAction() {
        return action;
    }

    /**
     * @return The mods of the event
     */
    public final int getMods() {
        return mods;
    }

    /**
     * @return The x cursor coordinate of the event
     */
    public final double getX() {
        return x;
    }

    /**
     * @return The y cursor coordinate of the event
     */
    public final double getY() {
        return y;
    }

    @Override
    public EventType getType() {
        return EventType.MOUSE_BUTTON;
    }

}
