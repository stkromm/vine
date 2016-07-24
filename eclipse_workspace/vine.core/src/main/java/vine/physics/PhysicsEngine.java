package vine.physics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PhysicsEngine
{
    private final CollisionEngine                 collisions   = new CollisionEngine();
    private final Comparator<? super PhysicsBody> sorter       = (
            o1,
            o2) -> o1.getTransform().getWorldPosition().getX() > o2.getTransform().getWorldPosition().getX() ? 1 : -1;
    private final List<PhysicsBody>               physicBodies = new ArrayList<>();

    public PhysicsEngine()
    {
    }

    public void update(final float delta)
    {
        // Integration , velocity and position resolution
        for (final PhysicsBody p : physicBodies)
        {
            p.onPhysicsUpdate(delta);
        }

        Collections.sort(physicBodies, sorter);
        collisions.prepareCollisionDetection();
        final int size = physicBodies.size();
        for (int i = 0; i < size - 1; i++)
        {
            final PhysicsBody p = physicBodies.get(i);
            final float posX = p.getTransform().getWorldPosition().getX() + p.getAabb().getWidth();
            final float posY = p.getTransform().getWorldPosition().getY();
            final float height = p.getAabb().getHeight();
            int a = i + 1;
            PhysicsBody q = physicBodies.get(a);
            while (a < size && q.getTransform().getWorldPosition().getX() <= posX)
            {
                q = physicBodies.get(a);
                final float qy = q.getTransform().getWorldPosition().getY();
                final boolean yOverlap = posY < qy ? qy <= posY + height : posY - qy < q.getAabb().getHeight();
                if (yOverlap)
                {
                    collisions.addCollisionPair(p, q);
                }
                a++;
            }
        }
        collisions.resolveCollisions(delta);
    }

    public boolean addPhysicBody(final PhysicsBody physicBody)
    {
        return physicBodies.add(physicBody);
    }

    public boolean removePhysicBody(final PhysicsBody physicBody)
    {
        return physicBodies.remove(physicBody);
    }
}
