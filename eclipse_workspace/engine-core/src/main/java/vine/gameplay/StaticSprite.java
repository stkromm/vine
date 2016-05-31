package vine.gameplay;

import vine.game.ITransform;
import vine.game.Transform;
import vine.game.scene.Component;
import vine.graphics.RgbaImage;
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
    ITransform                 transform = new Transform();
    private final MutableVec2f size      = new MutableVec2f(0, 0);
    private final RgbaImage        texture;
    private final float[]      textureUVs;

    /**
     * @return
     */
    @Override
    public final float[] getUVCoordinates()
    {
        return textureUVs;
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
    public StaticSprite(final RgbaImage texture, final int texX, final int texY, final int texWidth, final int texHeight,
            final float width, final float height)
    {
        super();
        this.texture = texture;
        textureUVs = texture.getPackedUVSquad(texX, texY, texWidth, texHeight);
        size.setX(width);
        size.setY(height);
    }

    /**
     * @return
     */
    @Override
    public RgbaImage getTexture()
    {
        return texture;
    }

    @Override
    public Vec2f getSize()
    {
        return size;
    }

    @Override
    public void onRender(final SpriteBatch batcher)
    {
        batcher.submit(
                getTexture(),
                getUVCoordinates(),
                entity.getXPosition(),
                entity.getYPosition(),
                getSize().getX(),
                getSize().getY(),
                entity.getZPosition(),
                entity.getColor().getColor());
    }

    @Override
    public void onUpdate(final float delta)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAttach()
    {
        transform.setParent(entity.getTransform());
    }

    @Override
    public void onDetach()
    {
        super.detach();
        transform.setParent(null);
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
