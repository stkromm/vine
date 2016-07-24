package vine.game;

import vine.game.screen.Screen;

/**
 * @author Steffen
 *
 */
public interface Layer
{
    /**
     * @return The name of the layer
     */
    String getName();

    /**
     * @param screen
     *            The screen to render on.
     */
    void render(Screen screen);
}
