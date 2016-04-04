package vine.gameplay.component;

import vine.animation.AnimationState;
import vine.animation.AnimationStateManager;
import vine.game.Component;
import vine.graphics.Sprite;
import vine.graphics.Texture2D;
import vine.math.Vector3f;

/**
 * @author Steffen
 *
 */
public class AnimatedSprite extends Component implements Sprite {

    Texture2D texture;
    AnimationStateManager animation;
    private final Vector3f size = new Vector3f(0, 0, 0);

    @Override
    public final void update(final float delta) {
        animation.tick(delta);
    }

    public void construct(final AnimationState animation, final float width, final float height) {
        this.animation = new AnimationStateManager(new AnimationState[] { animation });
        size.setX(width);
        size.setY(height);
    }

    @Override
    public float[] getUVCoordinates() {
        return animation.getCurrentFrame();
    }

    @Override
    public Texture2D getTexture() {
        return animation.getCurrentState().getClip().getTexture();
    }

    @Override
    public Vector3f getSize() {
        return size;
    }

}
