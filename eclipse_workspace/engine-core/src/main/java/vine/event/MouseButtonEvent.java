package vine.event;

import vine.input.InputAction;

/**
 * @author Steffen
 *
 */
public class MouseButtonEvent implements Event {
    private final int button;
    private final InputAction action;
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
    public MouseButtonEvent(final int button, final InputAction action, final int mods, final double x,
            final double y) {
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
        return this.button;
    }

    /**
     * @return The action type of the event
     */
    public final InputAction getAction() {
        return this.action;
    }

    /**
     * @return The mods of the event
     */
    public final int getMods() {
        return this.mods;
    }

    /**
     * @return The x cursor coordinate of the event
     */
    public final double getX() {
        return this.x;
    }

    /**
     * @return The y cursor coordinate of the event
     */
    public final double getY() {
        return this.y;
    }

    @Override
    public EventType getType() {
        return EventType.MOUSE_BUTTON;
    }

}
