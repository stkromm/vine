package vine.game.tilemap;

import vine.math.geometry.shape.MutableAabb;
import vine.math.vector.Vec2f;

import java.util.ArrayList;
import java.util.List;

import vine.game.primitive.Primitive;
import vine.game.scene.GameEntity;
import vine.physics.CollisionResponse;
import vine.physics.PhysicsBody;

public class TileMapSceneProxy extends GameEntity implements PhysicsBody
{
    private final MutableAabb Aabb           = new MutableAabb(0, 0, 0, 0);
    private final Vec2f       placeholderVec = new Vec2f();
    private UniformTileMap    map;
    List<Primitive>           primitives     = new ArrayList<>(1);

    public void construct(final UniformTileMap map)
    {
        this.map = map;
        Aabb.setHeight(map.getHeight() * map.getTileHeight());
        Aabb.setWidth(map.getWidth() * map.getTileWidth());
        final Primitive p = new TileMapPrimitive(map, Aabb);
        primitives.add(p);
    }

    public UniformTileMap getMap()
    {
        return map;
    }

    @Override
    public void onPhysicsUpdate(final float delta)
    { // TileMap objects are immoveable
    }

    @Override
    public vine.math.geometry.shape.Aabb getAabb()
    {
        return Aabb;
    }

    @Override
    public CollisionResponse getCollisionResponse()
    {
        return CollisionResponse.BLOCK;
    }

    @Override
    public Vec2f getVelocity()
    {
        return placeholderVec;
    }

    @Override
    public float getInvMass()
    {
        return 0;
    }

    @Override
    public void setVelocity(final Vec2f velocity)
    { // TileMap objects are immoveable
    }

    @Override
    public void move(final Vec2f assi)
    {
        // TileMap objects are immoveable
    }

    @Override
    public boolean isKinematic()
    {
        return false;
    }

    @Override
    public boolean isAsleep()
    {
        return true;
    }
}
