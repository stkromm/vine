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
     * @param action
     * @param mods
     * @param x
     * @param y
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
     * @return
     */
    public final int getButton() {
        return button;
    }

    /**
     * @return
     */
    public final int getAction() {
        return action;
    }

    /**
     * @return
     */
    public final int getMods() {
        return mods;
    }

    /**
     * @return
     */
    public final double getX() {
        return x;
    }

    /**
     * @return
     */
    public final double getY() {
        return y;
    }

    @Override
    public EventType getType() {
        return EventType.MOUSE_BUTTON;
    }

}
