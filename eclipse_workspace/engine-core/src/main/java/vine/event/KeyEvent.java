package vine.event;

import vine.input.InputAction;

/**
 * @author Steffen
 *
 */
public class KeyEvent implements Event {
    /**
     * 
     */
    private final int key;
    /**
     * 
     */
    private final int scancode;
    /**
     * 
     */
    private final InputAction action;
    /**
     * 
     */
    private final int mods;

    /**
     * @param key
     * @param scancode
     * @param action
     * @param mods
     */
    public KeyEvent(final int key, final int scancode, final InputAction action, final int mods) {
        super();
        this.key = key;
        this.scancode = scancode;
        this.action = action;
        this.mods = mods;
    }

    /**
     * @return
     */
    public final int getKey() {
        return key;
    }

    /**
     * @return
     */
    public final int getScancode() {
        return scancode;
    }

    /**
     * @return
     */
    public final InputAction getAction() {
        return action;
    }

    /**
     * @return
     */
    public final int getMods() {
        return mods;
    }

    @Override
    public final EventType getType() {
        return EventType.KEY;
    }

}
