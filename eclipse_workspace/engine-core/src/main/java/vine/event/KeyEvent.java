package vine.event;

import vine.input.InputAction;

/**
 * @author Steffen
 *
 */
public class KeyEvent implements Event
{
    /**
     * 
     */
    private final int         key;
    /**
     * 
     */
    private final int         scancode;
    /**
     * 
     */
    private final InputAction action;
    /**
     * 
     */
    private final int         mods;

    /**
     * @param key
     * @param scancode
     * @param action
     * @param mods
     */
    public KeyEvent(final int key, final int scancode, final InputAction action, final int mods)
    {
        super();
        this.key = key;
        this.scancode = scancode;
        this.action = action;
        this.mods = mods;
    }

    /**
     * @return
     */
    public final int getKey()
    {
        return this.key;
    }

    /**
     * @return
     */
    public final int getScancode()
    {
        return this.scancode;
    }

    /**
     * @return
     */
    public final InputAction getAction()
    {
        return this.action;
    }

    /**
     * @return
     */
    public final int getMods()
    {
        return this.mods;
    }

    @Override
    public final EventType getType()
    {
        return EventType.KEY;
    }

    @Override
    public final String toString()
    {
        return "KeyEvent:" + this.getAction().toString() + " Key:" + this.getKey() + " with Mods:" + this.getMods()
                + " and Scancode:" + this.scancode;
    }

}
