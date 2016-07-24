package vine.gameplay;

import vine.math.vector.MutableVec2f;
import vine.math.vector.Vec2f;

import vine.game.Transform;
import vine.game.scene.Component;
import vine.graphics.Renderable;
import vine.graphics.RgbaTexture;
import vine.graphics.Sprite;
import vine.graphics.renderer.SpriteBatch;

/**
 * @author Steffen
 *
 */
public class StaticSprite extends Component implements Sprite, Renderable
{
    private final RgbaTexture texture;
    private final float[]     textureUVs;
    MutableVec2f              size = new MutableVec2f();

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
    public StaticSprite(final RgbaTexture texture, final int texX, final int texY, final int texWidth,
            final int texHeight, final int width, final int height)
    {
        super();
        this.texture = texture;
        textureUVs = texture.getUvQuad(texX, texY, texWidth, texHeight);
        size.set(width, height);
    }

    /**
     * @return
     */
    @Override
    public RgbaTexture getTexture()
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

    @Override
    public Transform getTransform()
    {
        return entity.getTransform();
    }

    @Override
    public boolean isVisible()
    {
        return true;
    }
}
