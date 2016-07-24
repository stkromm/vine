package vine.graphics;

import vine.math.vector.Vec2f;

import vine.game.Transform;
import vine.graphics.renderer.SpriteBatch;

public interface Renderable
{
    public void onRender(SpriteBatch batch);

    Vec2f getSize();

    Transform getTransform();

    boolean isVisible();
}
