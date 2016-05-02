package vine.gameplay;

import vine.animation.AnimationStateManager;
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
public class AnimatedSprite extends Component implements Sprite, Renderable
{

    Image                      texture;
    AnimationStateManager      animation;
    private final MutableVec2f size = new MutableVec2f(0, 0);

    public AnimatedSprite(final AnimationStateManager animation, final Image texture, final float width,
            final float height)
    {
        super();
        this.texture = texture;
        this.animation = animation;
        this.size.setX(width);
        this.size.setY(height);
    }

    public AnimationStateManager getAnimationManager()
    {
        return this.animation;
    }

    @Override
    public float[] getUVCoordinates()
    {
        return this.animation.getCurrentFrame();
    }

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
    public void onDeactivation()
    {
        this.animation.stop();
    }

    @Override
    public void onActivation()
    {
        this.animation.start();
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
        //
    }

    @Override
    public void onDetach()
    {
        // TODO Auto-generated method stub

    }
}
