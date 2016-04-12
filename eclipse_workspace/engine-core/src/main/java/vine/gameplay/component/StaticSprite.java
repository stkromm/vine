package vine.gameplay.component;

import vine.game.Component;
import vine.graphics.Sprite;
import vine.graphics.Texture2D;
import vine.math.Vector2f;

/**
 * @author Steffen
 *
 */
public class StaticSprite extends Component implements Sprite {
    private final Vector2f size = new Vector2f(0, 0);
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
    @Override
    public Texture2D getTexture() {
        return this.texture;
    }

    @Override
    public Vector2f getSize() {
        return this.size;
    }
}
