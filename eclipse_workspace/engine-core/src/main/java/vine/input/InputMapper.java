package vine.input;

import vine.event.EventDispatcher;
import vine.event.KeyEvent;
import vine.event.MouseButtonEvent;

/**
 * @author Steffen
 *
 */
public class InputMapper {
    private static final boolean[] keys = new boolean[65536];

    private InputMapper() {

    }

    /**
     * Sets the key, with the given keycode, if valid, to the given value, that
     * tells if the button is pressed.
     * 
     * @param keycode
     *            Maps to the current key state.
     * @param value
     *            Signals, that the key with the given keycode is pressed.
     */
    private static void setKeyPressed(int keycode, boolean value) {
        keys[keycode] = value;
    }

    /**
     * @return Returns the absolute number of possible different keys.
     */
    private static int getNumberOfKeys() {
        return keys.length;
    }

    /**
     * Returns true, if the key is currently pressed.
     * 
     * @param keycode
     *            Keycode, of the key, that press-state should be returned.
     * @return Returns true, if the key, that corresponds to the keycode, is
     *         pressed.
     */
    public static boolean isKeyPressed(int keycode) {
        if (keys.length <= keycode || keycode < 0) {
            return false;
        } else {
            return keys[keycode];
        }
    }

    public static void assignInput(Input input) {
        input.setKeyCallback((win, key, scancode, action, mods) -> {
            if (getNumberOfKeys() > key && key >= 0) {
                setKeyPressed(key, input.isReleaseAction(action));
            }
            EventDispatcher.dispatch(new KeyEvent(key, scancode, action, mods));
        });
        input.setMouseButtonCallback((win, key, action, mods) -> {
            if (getNumberOfKeys() > key && key >= 0) {
                setKeyPressed(key, input.isReleaseAction(action));
            }
            EventDispatcher.dispatch(new MouseButtonEvent(key, action, mods, input.getCursorX(), input.getCursorY()));
        });
        input.setCursorPositionCallback((w, x, y) -> {
        });
    }

}