package vine.physics;

import vine.game.scene.Component;
import vine.math.geometry.Intersection2D;
import vine.math.vector.MutableVec2f;
import vine.math.vector.Vec2f;

public class CollisionBox extends Component implements Collider
{
    PhysicsComponent physics;
    MutableVec2f     boundingBox = new MutableVec2f(32, 32);
    MutableVec2f     position    = new MutableVec2f(0, 0);

    @Override
    public PhysicsComponent getPhysics()
    {
        return this.physics;
    }

    @Override
    public boolean collideWith(final Collider collided)
    {
        this.position.set(
                getEntity().getXPosition() + this.worldOffset.getX(),
                getEntity().getYPosition() + this.worldOffset.getY());
        return collided.collideWithAABB(this.position, this.boundingBox);

    }

    @Override
    public void onUpdate(final float delta)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void onAttach()
    {
        this.physics = this.entity.getComponent(PhysicsComponent.class);
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
    public boolean collideWithAABB(final Vec2f origin, final Vec2f extend)
    {
        this.position.set(
                getEntity().getXPosition() + this.worldOffset.getX(),
                getEntity().getYPosition() + this.worldOffset.getY());
        return Intersection2D.doesAabbIntersectAabb(getEntity().getPosition(), this.boundingBox, origin, extend);
    }

    @Override
    public boolean collideWithOBB(final Vec2f origin, final Vec2f sideA, final Vec2f sideB)
    {
        this.position.set(
                getEntity().getXPosition() + this.worldOffset.getX(),
                getEntity().getYPosition() + this.worldOffset.getY());
        return false;
    }

    @Override
    public boolean collideWithCircle(final Vec2f center, final float radius)
    {
        this.position.set(
                getEntity().getXPosition() + this.worldOffset.getX(),
                getEntity().getYPosition() + this.worldOffset.getY());
        return Intersection2D.doesAabbIntersectCircle(getEntity().getPosition(), this.boundingBox, center, radius);
    }

    @Override
    public void onOverlap(final Collider other)
    {
        //
    }

    @Override
    public void onCollision(final Collider other)
    {
        //
    }

    @Override
    public Vec2f getExtend()
    {
        return this.boundingBox;
    }

    @Override
    public Vec2f getPosition()
    {
        this.position.set(
                getEntity().getXPosition() + this.worldOffset.getX(),
                getEntity().getYPosition() + this.worldOffset.getY());
        return this.position;
    }

    @Override
    public void onCollisionUpdate(final float f)
    {
        //
    }
}
