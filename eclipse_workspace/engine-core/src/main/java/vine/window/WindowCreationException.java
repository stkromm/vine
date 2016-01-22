package vine.window;

/**
 * Gets created, when the game window creation fails.
 * 
 * @author Steffen
 *
 */
public class WindowCreationException extends Exception {

    /**
     * Default serial id.
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param message
     *            error message
     */
    public WindowCreationException(String message) {
        super(message);
    }

}
