package vine.game.screen;

import vine.math.Matrix4f;

/**
 * @author Steffen
 *
 */
public interface Screen {

    /**
     * @return The width of the screen in game units.
     */
    int getWidth();

    /**
     * @return The height of the screen in game units.
     */
    int getHeight();

    /**
     * @param x
     * @return
     */
    float worldXCoordToScreenXCoord(float x);

    /**
     * @param y
     * @return
     */
    float worldYCoordToScreenYCoord(float y);

    /**
     * @return The number of game units that fit into one window pixel.
     */
    float getUnitsPerPixel();

    Viewport getViewport();

    float getAspect();

    Matrix4f getOrthographicProjection();
}