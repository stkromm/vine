package vine.gameplay.component;

import vine.animation.AnimationStateManager;
import vine.game.scene.Component;
import vine.graphics.Image;
import vine.graphics.Sprite;
import vine.math.Vector2f;

/**
 * @author Steffen
 *
 */
public class AnimatedSprite extends Component implements Sprite
{

    Image                  texture;
    AnimationStateManager  animation;
    private final Vector2f size = new Vector2f(0, 0);

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
    public Vector2f getSize()
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
}
