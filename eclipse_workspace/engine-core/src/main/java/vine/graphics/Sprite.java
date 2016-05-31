package vine.graphics;

import vine.math.vector.Vec2f;

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

    RgbaImage getTexture();

    Vec2f getSize();

}