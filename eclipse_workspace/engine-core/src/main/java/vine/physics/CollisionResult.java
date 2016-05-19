package vine.physics;

import vine.math.vector.MutableVec2f;

public class CollisionResult
{
    private final MutableVec2f normal = new MutableVec2f();
    private Collider           collider;
    private Collider           collided;
}
