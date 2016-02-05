package vine.gameplay.component;

import vine.graphics.Sprite;
import vine.graphics.Texture2D;

/**
 * @author Steffen
 *
 */
public class StaticSprite extends Component implements Sprite { // NOSONAR

    private Texture2D texture;
    private float[] vertices;
    private float[] textureUVs;

    /**
     * @return The vertices array of the sprite.
     */
    @Override
    public final float[] getVertices() {
        return vertices.clone();
    }

    /**
     * @return
     */
    @Override
    public final float[] getUVCoordinates() {
        return textureUVs.clone();
    }

    @Override
    public int[] getIndices() {
        return new int[] { 0, 1, 2, 2, 3, 0 };
    }

    /**
     * @param worldWidth
     * @param worldHeight
     * @param texture
     * @param texX
     * @param texY
     * @param texWidth
     * @param texHeight
     */
    public void construct(final int worldWidth, final int worldHeight, final Texture2D texture, final int texX,
            final int texY, final int texWidth, final int texHeight) {
        vertices = new float[] { 0, 0, 0, 0, worldHeight, 0, worldWidth, worldHeight, 0, worldWidth, 0, 0, };
        this.texture = texture;
        textureUVs = texture.getUVSquad(texX, texY, texWidth, texHeight);
    }

    /**
     * @return
     */
    public Texture2D getTexture() {
        return texture;
    }
}
