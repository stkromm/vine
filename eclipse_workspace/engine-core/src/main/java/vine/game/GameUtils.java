package vine.game;

import vine.game.GameObject.ReferenceManager;

/**
 * @author Steffen
 *
 */
public final class GameUtils {

    private GameUtils() {
        // Utility Class
    }

    /**
     * @param name
     *            The name of a potential gameObject
     * @return True, if a gameObject can be instantiate with the given name
     */
    public static boolean isValidGameObjectName(final String name) {
        return name != null && !name.contains(GameObject.ReferenceManager.ID_QUALIFIER)
                && !ReferenceManager.OBJECTS.containsKey(name);
    }

}
