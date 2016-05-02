package vine.physics;

import java.lang.ref.WeakReference;

import vine.game.scene.Component;
import vine.game.scene.GameEntity;
import vine.game.tilemap.UniformTileMap;
import vine.math.geometry.Intersection2D;
import vine.math.vector.MutableVec2f;
import vine.math.vector.Vec2f;

public class PhysicsComponent extends Component implements PhysicsBody
{
    private WeakReference<GameEntity> lastCollidedEntity = new WeakReference<>(getEntity());
    // Physics
    // private float weight;
    // private float friction;
    private final float               maxSpeed           = 64;
    /**
     * The speed of this entity. Unit is World-Units / second.
     */
    private final MutableVec2f        speed              = new MutableVec2f(0, 0);
    /**
     * The acceleration of this entity. Unit is Speed / second.
     */
    private final MutableVec2f        acceleration       = new MutableVec2f(0, 0);
    /**
     * Does this entity get blocked by dynamic entities.
     */
    private final boolean             blockDynamic       = true;
    /**
     * Does this entity get blocked by static objects.
     */
    private final boolean             blockStatic        = true;

    public final float getSpeedX()
    {
        return this.speed.getX();
    }

    public final float getSpeedY()
    {
        return this.speed.getY();
    }

    public final Vec2f getSpeed()
    {
        return this.speed;
    }

    public final void setSpeed(final float x, final float y)
    {
        this.speed.setX(x);
        this.speed.setY(y);
        capSpeed();
    }

    public final void addSpeed(final float x, final float y)
    {
        this.speed.add(x, y);
        capSpeed();
    }

    public final boolean intersect(final GameEntity e)
    {
        if (e == this.entity)
        {
            return false;
        }
        if (Intersection2D.doesAabbIntersectAabb(
                this.entity.getPosition(),
                this.entity.getBoundingBoxExtends(),
                e.getPosition(),
                e.getBoundingBoxExtends()))
        {
            for (final Collider collider : this.entity.getCollisionComponents())
            {
                for (final Collider collided : e.getCollisionComponents())
                {
                    if (collider.collideWith(collided))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
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

    private final void capSpeed()
    {
        if (this.speed.getX() > this.maxSpeed)
        {
            this.speed.setX(this.maxSpeed);
        } else if (this.speed.getX() < -this.maxSpeed)
        {
            this.speed.setX(-this.maxSpeed);
        }
        if (this.speed.getY() > this.maxSpeed)
        {
            this.speed.setY(this.maxSpeed);
        } else if (this.speed.getY() < -this.maxSpeed)
        {
            this.speed.setY(-this.maxSpeed);
        }
    }

    protected final Vec2f getAcceleration()
    {
        return this.acceleration;
    }

    public final float getAccelerationX()
    {
        return this.acceleration.getX();
    }

    public final float getAccelerationY()
    {
        return this.acceleration.getY();
    }

    public final void addAcceleration(final float x, final float y)
    {
        this.acceleration.setX(getAccelerationX() + x);
        this.acceleration.setY(getAccelerationY() + y);
    }

    public final void setAcceleration(final float x, final float y)
    {
        this.acceleration.setX(x);
        this.acceleration.setY(y);
    }

    public final boolean blocksStatic()
    {
        return this.blockStatic;
    }

    public final boolean blockDynamic()
    {
        return this.blockDynamic;
    }

    private final void accelerate(final float delta)
    {
        if (getAccelerationX() != 0 || getAccelerationY() != 0)
        {
            addSpeed(getAccelerationX() * delta, getAccelerationY() * delta);
            final float xSpeed = (1 - 0.5f * delta) * getSpeedX();
            final float ySpeed = (1 - 0.5f * delta) * getSpeedY();
            setSpeed(xSpeed, ySpeed);
            addAcceleration(-getAccelerationX() * 0.8f, -getAccelerationY() * 0.8f);
        }
    }

    /**
     * @param x
     *            x distance to move
     * @param y
     *            y distance to move
     * @return true, if the entity has moved the given distance
     */
    private final void move(final float x, final float y)
    {
        if (Math.abs(x) + Math.abs(y) > 0.0001f)
        {
            this.entity.addPosition(x, y);
            this.entity.setCurrentChunk();
        }
    }

    @Override
    public void onPhysicsUpdate(final float delta)
    {
        accelerate(delta);
        move(getSpeedX() * delta, getSpeedY() * delta);
        //
        calculateCollision();
    }

    private void calculateCollision()
    {
        if (blocksStatic() && this.intersect(this.entity.getScene().getMap()))
        {
        }
        if (blockDynamic())
        {
            if (this.lastCollidedEntity.get() == null || this.lastCollidedEntity.get().isDestroyed())
            {
                this.lastCollidedEntity = new WeakReference<>(this.entity);
            } else if (this.intersect(this.lastCollidedEntity.get()))
            {
            }
            final int cx = Math.round(this.entity.getBoundingBoxExtends().getX())
                    / this.entity.getChunk().getTileWidth();
            final int cy = Math.round(this.entity.getBoundingBoxExtends().getY())
                    / this.entity.getChunk().getTileHeight();
            for (int i = cx + 1; i >= 0; i--)
            {
                for (int j = cy + 1; j >= 0; j--)
                {
                    for (final GameEntity entity : this.entity.getChunk().getSubdivisonChunk(
                            this.entity.getXPosition() + i * this.entity.getChunk().getTileWidth(),
                            this.entity.getYPosition() + j * this.entity.getChunk().getTileHeight()))
                    {
                        if (this.intersect(entity))
                        {
                            this.lastCollidedEntity = new WeakReference<>(entity);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onUpdate(final float delta)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAttach()
    {
        this.entity.getScene().getWorld().getPhysics().addPhysicBody(this);
    }

    @Override
    public void onDetach()
    {
        this.entity.getScene().getWorld().getPhysics().removePhysicBody(this);
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
}