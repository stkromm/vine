package vine.physics;

import java.util.ArrayList;
import java.util.List;

import vine.game.primitive.Primitive;
import vine.util.Log;
import vine.util.Pool;

public class CollisionEngine
{
    CollisionDetection detection = new CollisionDetection();
    ContactGeneration  contacts  = new ContactGeneration();

    public void resolveCollisions(final float delta)
    {
        // Broad and narrow
        if (detection.collisionPairsA.isEmpty())
        {
            return;
        }
        // Log.debug("Collision after broad phase:" +
        // detection.collisionPairsA.size());
        for (int i = detection.collisionPairsA.size() - 1; i >= 0; i--)
        {
            final PhysicsBody p = detection.collisionPairsA.get(i);
            final PhysicsBody q = detection.collisionPairsB.get(i);

            for (final Primitive pp : p.getPrimitives())
            {
                for (final Primitive qq : q.getPrimitives())
                {
                    if (pp.trace(qq))
                    {
                        contacts.generateContact(pp, p, qq, q);
                    }
                }
            }

        }
        if (!contacts.contacts.isEmpty())
        {
            Log.debug("Need to resolve contacts: %d", Integer.valueOf(contacts.contacts.size()));
            for (final Contact contact : contacts.contacts)
            {
                contact.resolve(delta);
            }
        }
    }

    public void addCollisionPair(final PhysicsBody p, final PhysicsBody q)
    {
        detection.collisionPairsA.add(p);
        detection.collisionPairsB.add(q);
    }

    public void prepareCollisionDetection()
    {
        detection.collisionPairsA.clear();
        detection.collisionPairsB.clear();
        for (final Contact contact : contacts.contacts)
        {
            contacts.contactPool.give(contact);
        }
        contacts.contacts.clear();
    }

    private static class CollisionDetection
    {
        final List<PhysicsBody> collisionPairsA = new ArrayList<>();
        final List<PhysicsBody> collisionPairsB = new ArrayList<>();

        public CollisionDetection()
        {
        }
    }

    private static class ContactGeneration
    {
        final Pool<Contact> contactPool = new Pool<>(Contact.class, 5000);
        final List<Contact> contacts    = new ArrayList<>();

        public ContactGeneration()
        {
        }

        public void generateContact(final Primitive pp, final PhysicsBody p, final Primitive qq, final PhysicsBody q)
        {
            final Contact contact = contactPool.take();
            contact.setCollided(q);
            contact.setCollider(p);
            pp.generateContact(qq, contact);
            contacts.add(contact);
        }
    }
}
