package vine.gameplay.scene;

/**
 * Signals, that the creation of a scene object failed due to a missing scene
 * asset, normally.
 * 
 * @author Steffen
 *
 */
public class SceneCreationException extends Exception {
    /**
     * Auto generated serial id.
     */
    private static final long serialVersionUID = -3339224831584061401L;

    /**
     * @param message
     *            The error message.
     */
    protected SceneCreationException(final String message) {
        super(message);
    }
}
