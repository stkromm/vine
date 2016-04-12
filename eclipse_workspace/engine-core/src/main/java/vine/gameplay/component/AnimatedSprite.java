package vine.gameplay.component;

import vine.animation.AnimationStateManager;
import vine.game.Component;
import vine.graphics.Sprite;
import vine.graphics.Texture2D;
import vine.math.Vector2f;

/**
 * @author Steffen
 *
 */
public class AnimatedSprite extends Component implements Sprite {

    Texture2D texture;
    AnimationStateManager animation;
    private final Vector2f size = new Vector2f(0, 0);

    @Override
    public final void onUpdate(final float delta) {
        this.animation.tick(delta);
    }

    public void construct(final AnimationStateManager animation, final Texture2D texture, final float width,
            final float height) {
        this.texture = texture;
        this.animation = animation;
        this.size.setX(width);
        this.size.setY(height);
    }

    @Override
    public float[] getUVCoordinates() {
        return this.animation.getCurrentFrame();
    }

    @Override
    public Texture2D getTexture() {
        return this.texture;
    }

    @Override
    public Vector2f getSize() {
        return this.size;
    }

}
