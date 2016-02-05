package vine.gameplay.component;

import vine.animation.AnimationState;
import vine.animation.AnimationStateManager;
import vine.graphics.Sprite;
import vine.graphics.Texture2D;

public class AnimatedSprite extends Component implements Sprite {

    float[] vertices;
    Texture2D texture;
    AnimationStateManager animation;

    public AnimatedSprite() {
    }

    @Override
    public void update(float delta) {
        animation.tick(delta);
    }

    public void construct(final int worldWidth, final int worldHeight, AnimationState animation) {
        vertices = new float[] { 0, 0, 0, 0, worldHeight, 0, worldWidth, worldHeight, 0, worldWidth, 0, 0, };
        this.animation = new AnimationStateManager(new AnimationState[] { animation });
    }

    @Override
    public float[] getVertices() {
        return vertices.clone();
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
    public int[] getIndices() {
        return new int[] { 0, 1, 2, 2, 3, 0 };
    }

}
