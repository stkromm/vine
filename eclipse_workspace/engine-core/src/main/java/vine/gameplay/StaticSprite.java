package vine.gameplay;

import vine.game.scene.Component;
import vine.graphics.Image;
import vine.graphics.Renderable;
import vine.graphics.Sprite;
import vine.graphics.renderer.SpriteBatch;
import vine.math.vector.MutableVec2f;
import vine.math.vector.Vec2f;

/**
 * @author Steffen
 *
 */
public class StaticSprite extends Component implements Sprite, Renderable
{
    private final MutableVec2f size = new MutableVec2f(0, 0);
    private final Image        texture;
    private final float[]      textureUVs;

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
    public Vec2f getSize()
    {
        return this.size;
    }

    @Override
    public void onRender(final SpriteBatch batcher)
    {
        batcher.submit(
                getTexture(),
                getUVCoordinates(),
                this.entity.getXPosition() + this.worldOffset.getX(),
                this.entity.getYPosition() + this.worldOffset.getY(),
                getSize().getX(),
                getSize().getY(),
                this.entity.getZPosition(),
                this.entity.getColor().getColor());
    }

    @Override
    public void onUpdate(final float delta)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAttach()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDetach()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDeactivation()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onActivation()
    {
        // TODO Auto-generated method stub

    }
}
