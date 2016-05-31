package vine.physics;

import java.util.List;

import vine.game.Transform;
import vine.game.primitive.Primitive;
import vine.math.geometry.shape.AABB;
import vine.math.vector.MutableVec2f;
import vine.math.vector.Vec2f;

public interface PhysicsBody
{
    void onPhysicsUpdate(final float delta);

    Transform getTransform();

    AABB getAABB();

    CollisionResponse getCollisionResponse();

    List<Primitive> getPrimitives();

    Vec2f getVelocity();

    float getInvMass();

    void setVelocity(Vec2f velocity);

    Vec2f getPosition();

    void move(MutableVec2f assi);

    boolean isKinematic();

    boolean isAsleep();
}
