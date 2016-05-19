package vine.physics;

import java.util.HashSet;
import java.util.Set;

public class Physics
{
    private final Set<PhysicsBody> physicsBodies = new HashSet<>();

    public Physics()
    {
        // TODO Auto-generated constructor stub
    }

    public void update(final float delta)
    {
        for (final PhysicsBody p : this.physicsBodies)
        {
            p.onPhysicsUpdate(delta);
        }
    }

    public boolean addPhysicBody(final PhysicsBody physicBody)
    {
        return this.physicsBodies.add(physicBody);
    }

    public boolean removePhysicBody(final PhysicsBody physicBody)
    {
        return this.physicsBodies.remove(physicBody);
    }
}
