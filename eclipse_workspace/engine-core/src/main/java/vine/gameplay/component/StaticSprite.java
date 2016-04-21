package vine.gameplay.component;

import vine.game.scene.Component;
import vine.graphics.Image;
import vine.graphics.Sprite;
import vine.math.Vector2f;

/**
 * @author Steffen
 *
 */
public class StaticSprite extends Component implements Sprite
{
    private final Vector2f size = new Vector2f(0, 0);
    private final Image    texture;
    private final float[]  textureUVs;

    /**
     * @return
     */
    @Override
    public final float[] getUVCoordinates()
    {
        return this.textureUVs;
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
    public StaticSprite(final Image texture, final int texX, final int texY, final int texWidth, final int texHeight,
            final float width, final float height)
    {
        super();
        this.texture = texture;
        this.textureUVs = texture.getPackedUVSquad(texX, texY, texWidth, texHeight);
        this.size.setX(width);
        this.size.setY(height);
    }

    /**
     * @return
     */
    @Override
    public Image getTexture()
    {
        return this.texture;
    }

    @Override
    public Vector2f getSize()
    {
        return this.size;
    }

}
