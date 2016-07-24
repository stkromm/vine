package vine.device.input;

/**
 * @author Steffen
 *
 */
public final class InputMapper
{
    /**
     * 
     */
    private static final boolean[] KEYS = new boolean[65536];

    private InputMapper()
    {

    }

    /**
     * Sets the key, with the given keycode, if valid, to the given value, that
     * tells if the button is pressed.
     * 
     * @param keycode
     *            Maps to the current key state.
     * @param action
     *            Signals, that the key with the given keycode is pressed.
     */
    public static void setKeyPressed(final int keycode, final InputAction action)
    {
        KEYS[keycode] = InputAction.isReleaseAction(action);
    }

    /**
     * @return Returns the absolute number of possible different keys.
     */
    public static int getNumberOfKeys()
    {
        return KEYS.length;
    }

    /**
     * Returns true, if the key is currently pressed.
     * 
     * @param keycode
     *            Keycode, of the key, that press-state should be returned.
     * @return Returns true, if the key, that corresponds to the keycode, is
     *         pressed.
     */
    public static boolean isKeyPressed(final int keycode)
    {
        if (keycode >= 0 && KEYS.length > keycode)
        {
            return KEYS[keycode];
        } else
        {
            return false;
        }
    }

}
