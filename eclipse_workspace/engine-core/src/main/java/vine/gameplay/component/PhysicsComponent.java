package vine.gameplay.component;

import vine.game.scene.Component;
import vine.game.scene.GameEntity;
import vine.game.tilemap.UniformTileMap;
import vine.math.Vector2f;
import vine.physics.BoxCollision;

public class PhysicsComponent extends Component
{
    private GameEntity     lastCollidedEntity = this.getEntity();
    private final Vector2f moveCache          = new Vector2f(0, 0);
    /**
     * Does this entity get blocked by dynamic entities.
     */
    private final boolean  blockDynamic       = false;
    /**
     * Does this entity get blocked by static objects.
     */
    private final boolean  blockStatic        = true;

    public final boolean blocksStatic()
    {
        return this.blockStatic;
    }

    public final boolean blockDynamic()
    {
        return this.blockDynamic;
    }

    public final boolean intersect(final GameEntity e)
    {
        return BoxCollision.collide(this.getEntity().getPosition(), this.getEntity().getBoundingBoxExtends(),
                e.getPosition(), e.getBoundingBoxExtends())//
                && e != this.getEntity();
    }

    private final boolean intersect(final UniformTileMap map)
    {
        final int startX = Math.round(this.entity.getXPosition()) / map.getTileWidth();
        final int startY = Math.round(this.entity.getYPosition()) / map.getTileHeight();
        final int width = Math.round(this.entity.getXPosition() + this.entity.getXExtends()) / map.getTileWidth()
                - startX;
        final int height = Math.round(this.entity.getYPosition() + this.entity.getYExtends()) / map.getTileHeight()
                - startY;
        for (int i = startX; i <= startX + width; i++)
        {
            for (int j = startY; j <= startY + height; j++)
            {
                if (map.blocksDynamic(i, j))
                {
                    return true;
                }
            }
        }
        return false;

    }

    private final void accelerate(final float delta)
    {
        if (this.entity.getAccelerationX() != 0 || this.entity.getAccelerationY() != 0)
        {
            this.entity.addSpeed(this.entity.getAccelerationX() * delta, this.entity.getAccelerationY() * delta);
            this.entity.getSpeed().scale(1 - 0.5f * delta);
            this.entity.addAcceleration(-this.entity.getAccelerationX() * 0.8f, -this.entity.getAccelerationY() * 0.8f);
        }
    }

    /**
     * @param x
     *            x distance to move
     * @param y
     *            y distance to move
     * @return true, if the entity has moved the given distance
     */
    private final boolean move(final float x, final float y)
    {
        if (Math.abs(x) + Math.abs(y) > 0.0001f)
        {
            this.moveCache.setX(x);
            this.moveCache.setY(y);
            if (this.moveCache.length() >= this.entity.getBoundingBoxExtends().length())
            {
                return this.move(x / 2, y / 2) && this.move(x / 2, y / 2);
            }

            this.entity.addPosition(x, y);
            if (this.blocksStatic() && this.intersect(this.entity.getScene().getMap()))
            {
                this.entity.addPosition(-x, -y);
                boolean result = false;
                if (y != 0)
                {
                    result = this.move(x / 2, 0);
                }
                if (x != 0)
                {
                    return result || this.move(0, y / 2);
                }
                return false;
            }
            if (this.blockDynamic())
            {
                if (this.lastCollidedEntity == null || this.lastCollidedEntity.isDestroyed())
                {
                    this.lastCollidedEntity = this.entity;
                } else if (this.intersect(this.lastCollidedEntity))
                {
                    this.entity.addPosition(-x, -y);
                    boolean result = false;
                    if (y != 0)
                    {
                        result = this.move(x / 2, 0);
                    }
                    if (x != 0)
                    {
                        return result || this.move(0, y / 2);
                    }
                }
                for (final GameEntity e : this.entity.getChunk().getEntities())
                {
                    if (this.intersect(e))
                    {
                        this.lastCollidedEntity = e;
                        this.entity.addPosition(-x, -y);
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void updatePhysics(final float delta)
    {
        // Physics
        this.accelerate(delta);
        this.move(this.getEntity().getXSpeed() * delta, this.getEntity().getYSpeed() * delta);
    }

}
