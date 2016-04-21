package vine.graphics;

import vine.math.Vector2f;

/**
 * @author Steffen
 *
 */
public interface Sprite
{
    int NUM_OF_VERTICES = 12;
    int NUM_OF_UVS      = 8;
    int NUM_OF_INDICES  = 6;

    float[] getUVCoordinates();

    Image getTexture();

    Vector2f getSize();

}