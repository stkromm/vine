package vine.gameplay.component;

import vine.graphics.Texture;

/**
 * @author Steffen
 *
 */
public class Sprite extends Component { // NOSONAR

    private Texture texture;
    private float worldWidth = 32;
    private float worldHeight = 64;
    private float[] vertices;
    private float[] textureUVs;

    /**
     * @return The vertices array of the sprite.
     */
    public final float[] getVertices() {
        return vertices;
    }

    public final float[] getUVCoordinates() {
        return textureUVs;
    }

    public void construct(int worldWidth, int worldHeight, Texture texture, int texX, int texY, int texWidth,
            int texHeight) {
        this.worldHeight = worldHeight;
        this.worldWidth = worldWidth;
        vertices = new float[] { 0, 0, 0, 0, worldHeight, 0, worldWidth, worldHeight, 0, worldWidth, 0, 0, };
        this.texture = texture;
        int textureWidth = texture.getWidth();
        int textureHeight = texture.getHeight();
        float uvX = texX / (float) textureWidth;
        float uvY = texY / (float) textureHeight;
        float uvWidth = texWidth / (float) textureWidth;
        float uvHeight = texHeight / (float) textureHeight;
        textureUVs = new float[] { uvX, uvY + uvHeight, uvX, uvY, uvX + uvWidth, uvY, uvX + uvWidth, uvY + uvHeight };
    }

    public Texture getTexture() {
        return texture;
    }
}
