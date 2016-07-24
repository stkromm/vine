package vine.physics;

import vine.math.geometry.shape.Aabb;
import vine.math.vector.Vec2f;

import java.util.List;

import vine.game.Transform;
import vine.game.primitive.Primitive;

public interface PhysicsBody
{
    void onPhysicsUpdate(final float delta);

    Transform getTransform();

    Aabb getAabb();

    CollisionResponse getCollisionResponse();

    List<Primitive> getPrimitives();

    Vec2f getVelocity();

    float getInvMass();

    void setVelocity(Vec2f velocity);

    Vec2f getPosition();

    void move(Vec2f assi);

    boolean isKinematic();

    boolean isAsleep();
}
