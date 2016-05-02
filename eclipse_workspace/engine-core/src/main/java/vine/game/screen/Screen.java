package vine.game.screen;

import vine.math.matrix.Mat4f;

/**
 * @author Steffen
 *
 */
public interface Screen
{

    /**
     * @return The width of the screen in game units.
     */
    int getWidth();

    /**
     * @return The height of the screen in game units.
     */
    int getHeight();

    /**
     * @return
     */
    Viewport getViewport();

    /**
     * @return
     */
    float getAspect();

    /**
     * @return
     */
    Mat4f getProjection();

    /**
     * @param width
     */
    void setWidth(int width);

    /**
     * @param coord
     * @return
     */
    float worldToScreenCoord(float coord);

    /**
     * @return The number of game units that fit into one window pixel.
     */
    float getUnitsPerPixel();

    /**
     * @param height
     */
    void setHeight(int height);

    void calculateViewport(final float windowHeight, final float windowWidth, final float aspectRatio);
}
