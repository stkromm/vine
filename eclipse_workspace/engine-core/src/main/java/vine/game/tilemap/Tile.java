package vine.game.tilemap;

import vine.game.GameObject;
import vine.game.scene.GameEntity;
import vine.graphics.Color;
import vine.graphics.Sprite;

public class Tile extends GameObject
{
    Sprite  sprite;
    boolean blockDynamic = false;

    public Tile(final Sprite sprite, final Color color, final boolean collisionEnabled, final float z)
    {
        this.sprite = sprite;
        this.blockDynamic = collisionEnabled;
    }

    public boolean blocksDynamic()
    {
        return this.blockDynamic;
    }

    public int getColor()
    {
        return 0;
    }

    public Sprite getSprite()
    {
        return this.sprite;
    }

    protected void onCollision(final GameEntity entity)
    {
        //
    }

    protected void onEnter(final GameEntity entity)
    {
        //
    }

    protected void onLeave(final GameEntity entity)
    {
        //
    }
}
