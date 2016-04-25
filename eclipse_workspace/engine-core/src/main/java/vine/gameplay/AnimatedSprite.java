package vine.gameplay;

import vine.animation.AnimationStateManager;
import vine.game.scene.Component;
import vine.graphics.Image;
import vine.graphics.Sprite;
import vine.graphics.renderer.SpriteBatch;
import vine.math.MutableVec2f;
import vine.math.Vec2f;

/**
 * @author Steffen
 *
 */
public class AnimatedSprite extends Component implements Sprite
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
    public void onRender(SpriteBatch batcher)
    {
        batcher.submit(
                this.getTexture(),
                this.getUVCoordinates(),
                this.entity.getXPosition() + this.worldOffset.getX(),
                this.entity.getYPosition() + this.worldOffset.getY(),
                this.getSize().getX(),
                this.getSize().getY(),
                this.entity.getZPosition(),
                this.entity.getColor().getColor());
    }

    @Override
    public void onUpdatePhysics(float delta)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUpdate(float delta)
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
}
