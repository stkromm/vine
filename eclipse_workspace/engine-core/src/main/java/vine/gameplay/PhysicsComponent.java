package vine.gameplay;

import java.lang.ref.WeakReference;

import vine.game.scene.Component;
import vine.game.scene.GameEntity;
import vine.game.tilemap.UniformTileMap;
import vine.graphics.renderer.SpriteBatch;
import vine.math.Vec2f;
import vine.physics.Intersection2D;

public class PhysicsComponent extends Component
{
    private WeakReference<GameEntity> lastCollidedEntity = new WeakReference<>(this.getEntity());
    /**
     * Does this entity get blocked by dynamic entities.
     */
    private final boolean             blockDynamic       = true;
    /**
     * Does this entity get blocked by static objects.
     */
    private final boolean             blockStatic        = true;

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
        return Intersection2D.doesAabbIntersectAabb(
                this.getEntity().getPosition(),
                this.getEntity().getBoundingBoxExtends(),
                e.getPosition(),
                e.getBoundingBoxExtends())//
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
            final float xSpeed = (1 - 0.5f * delta) * this.entity.getXSpeed();
            final float ySpeed = (1 - 0.5f * delta) * this.entity.getYSpeed();
            this.entity.setSpeed(xSpeed, ySpeed);
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
            if (Vec2f.length(x, y) >= this.entity.getBoundingBoxExtends().length())
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
                if (this.lastCollidedEntity.get() == null || this.lastCollidedEntity.get().isDestroyed())
                {
                    this.lastCollidedEntity = new WeakReference<>(this.entity);
                } else if (this.intersect(this.lastCollidedEntity.get()))
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
                        this.lastCollidedEntity = new WeakReference<>(e);
                        this.entity.addPosition(-x, -y);
                        return false;
                    }
                }
            }
            this.entity.setCurrentChunk();
            return true;
        }
        return false;
    }

    @Override
    public void onUpdatePhysics(final float delta)
    {
        // Physics
        this.accelerate(delta);
        this.move(this.getEntity().getXSpeed() * delta, this.getEntity().getYSpeed() * delta);
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

    @Override
    public void onDeactivation()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onActivation()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRender(SpriteBatch batcher)
    {
        // TODO Auto-generated method stub

    }
}
