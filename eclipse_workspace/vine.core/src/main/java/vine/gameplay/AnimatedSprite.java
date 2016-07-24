package vine.gameplay;

import vine.math.vector.MutableVec2f;
import vine.math.vector.Vec2f;

import vine.animation.AnimationStateManager;
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
public class AnimatedSprite extends Component implements Sprite, Renderable
{

    RgbaTexture                texture;
    AnimationStateManager      animation;
    private final MutableVec2f size      = new MutableVec2f(0, 0);
    private final Transform    transform = new Transform();

    public AnimatedSprite(final AnimationStateManager animation, final RgbaTexture texture, final float width,
            final float height)
    {
        super();
        this.texture = texture;
        this.animation = animation;
        size.setX(width);
        size.setY(height);
        transform.translate(8, 0);
    }

    public AnimationStateManager getAnimationManager()
    {
        return animation;
    }

    @Override
    public float[] getUVCoordinates()
    {
        return animation.getCurrentFrame();
    }

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
    public void onDeactivation()
    {
        animation.stop();
    }

    @Override
    public void onActivation()
    {
        animation.start();
    }

    @Override
    public void onRender(final SpriteBatch batcher)
    {
        batcher.submit(
                getTexture(),
                getUVCoordinates(),
                transform.getWorldPosition().getX(),
                transform.getWorldPosition().getY(),
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
        //
        transform.setParent(entity.getTransform());
    }

    @Override
    public void onDetach()
    {
        transform.setParent(null);
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
