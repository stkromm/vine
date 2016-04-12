package vine.graphics;

import vine.math.Vector2f;

/**
 * @author Steffen
 *
 */
public interface Sprite {
    static final int NUM_OF_VERTICES = 12;
    static final int NUM_OF_UVS = 8;
    static final int NUM_OF_INDICES = 6;

    float[] getUVCoordinates();

    Texture2D getTexture();

    Vector2f getSize();
}
