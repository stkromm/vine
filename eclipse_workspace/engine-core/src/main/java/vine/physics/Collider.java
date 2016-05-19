package vine.physics;

import vine.math.vector.Vec2f;

public interface Collider
{
    boolean collideWith(Collider collided);

    boolean collideWithAABB(Vec2f origin, Vec2f extend);

    boolean collideWithOBB(Vec2f origin, Vec2f sideA, Vec2f sideB);

    boolean collideWithCircle(Vec2f center, float radius);

    void onOverlap(Collider other);

    void onCollision(Collider other);

    Vec2f getExtend();

    Vec2f getPosition();

    PhysicsComponent getPhysics();

    void onCollisionUpdate(float f);
}
