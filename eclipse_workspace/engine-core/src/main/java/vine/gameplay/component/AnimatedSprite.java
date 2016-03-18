package vine.gameplay.component;

import vine.animation.AnimationState;
import vine.animation.AnimationStateManager;
import vine.game.Component;
import vine.graphics.Sprite;
import vine.graphics.Texture2D;

/**
 * @author Steffen
 *
 */
public class AnimatedSprite extends Component implements Sprite {

    Texture2D texture;
    AnimationStateManager animation;

    @Override
    public final void update(final float delta) {
        animation.tick(delta);
    }

    public void construct(final AnimationState animation) {
        this.animation = new AnimationStateManager(new AnimationState[] { animation });
    }

    @Override
    public float[] getUVCoordinates() {
        return animation.getCurrentFrame();
    }

    @Override
    public Texture2D getTexture() {
        return animation.getCurrentState().getClip().getTexture();
    }

}
