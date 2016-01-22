package vine.settings;

/**
 * Contains all settings, that configure the behavior of the running game.
 * 
 * @author stkromm
 *
 */
public class GameSettings {
    private static float maxFrameDuration = 1;

    private GameSettings() {

    }

    /**
     * @return Returns the maximum possible frame-duration of the running game.
     */
    public static float getMaxFrameDuration() {
        return maxFrameDuration;
    }

    /**
     * @return The start level asset name
     */
    public static String getStartLevelName() {
        return "test";
    }
}