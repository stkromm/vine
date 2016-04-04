package vine.gameplay.component;

import vine.game.Component;
import vine.graphics.Sprite;
import vine.graphics.Texture2D;
import vine.math.Vector3f;

/**
 * @author Steffen
 *
 */
public class StaticSprite extends Component implements Sprite { // NOSONAR
    private final Vector3f size = new Vector3f(0, 0, 0);
    private Texture2D texture;
    private float[] textureUVs;

    /**
     * @return
     */
    @Override
    public final float[] getUVCoordinates() {
        return this.textureUVs.clone();
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
            final int texHeight, final float width, final float height) {
        this.texture = texture;
        this.textureUVs = texture.getUVSquad(texX, texY, texWidth, texHeight);
        this.size.setX(width);
        this.size.setY(height);
    }

    /**
     * @return
     */
    public Texture2D getTexture() {
        return this.texture;
    }

    @Override
    public Vector3f getSize() {
        return this.size;
    }
}
