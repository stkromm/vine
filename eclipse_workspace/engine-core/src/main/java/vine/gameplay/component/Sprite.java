package vine.gameplay.component;

import vine.graphics.Texture;

/**
 * @author Steffen
 *
 */
public class Sprite extends Component { // NOSONAR

    private Texture texture;
    private float[] vertices;
    private float[] textureUVs;

    /**
     * @return The vertices array of the sprite.
     */
    public final float[] getVertices() {
        return vertices.clone();
    }

    /**
     * @return
     */
    public final float[] getUVCoordinates() {
        return textureUVs.clone();
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
    public void construct(final int worldWidth, final int worldHeight, final Texture texture, final int texX,
            final int texY, final int texWidth, final int texHeight) {
        vertices = new float[] { 0, 0, 0, 0, worldHeight, 0, worldWidth, worldHeight, 0, worldWidth, 0, 0, };
        this.texture = texture;
        final int textureWidth = texture.getWidth();
        final int textureHeight = texture.getHeight();
        final float uvX = texX / (float) textureWidth;
        final float uvY = texY / (float) textureHeight;
        final float uvWidth = texWidth / (float) textureWidth;
        final float uvHeight = texHeight / (float) textureHeight;
        textureUVs = new float[] { uvX, uvY + uvHeight, uvX, uvY, uvX + uvWidth, uvY, uvX + uvWidth, uvY + uvHeight };
    }

    /**
     * @return
     */
    public Texture getTexture() {
        return texture;
    }
}
