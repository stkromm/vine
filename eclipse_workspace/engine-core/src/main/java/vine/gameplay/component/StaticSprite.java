package vine.gameplay.component;

import vine.game.Component;
import vine.graphics.Sprite;
import vine.graphics.Texture2D;

/**
 * @author Steffen
 *
 */
public class StaticSprite extends Component implements Sprite { // NOSONAR

    private Texture2D texture;
    private float[] textureUVs;

    /**
     * @return
     */
    @Override
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
    public void construct(final Texture2D texture, final int texX, final int texY, final int texWidth,
            final int texHeight) {
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
