package vine.physics;

import vine.math.GMath;
import vine.math.vector.MutableVec2f;

public class Contact
{
    public Contact()
    {
    }

    private final MutableVec2f assi          = new MutableVec2f();
    PhysicsBody                collider;
    PhysicsBody                collided;
    MutableVec2f               contactNormal = new MutableVec2f();
    float                      penetration;
    float                      friction      = 1;
    float                      restituition  = 1;

    public void setCollider(final PhysicsBody p)
    {
        collider = p;

    }

    public void setCollided(final PhysicsBody q)
    {
        collided = q;
    }

    public MutableVec2f getContactNormal()
    {
        return contactNormal;
    }

    public void setContactNormal(final float x, final float y)
    {
        contactNormal.set(x, y);
    }

    public float getPenetration()
    {
        return penetration;
    }

    public void setPenetration(final float penetration)
    {
        this.penetration = penetration;
    }

    public float getFriction()
    {
        return friction;
    }

    public void setFriction(final float friction)
    {
        this.friction = friction;
    }

    public float getRestituition()
    {
        return restituition;
    }

    public void setRestituition(final float restituition)
    {
        this.restituition = restituition;
    }

    public PhysicsBody getCollider()
    {
        return collider;
    }

    public PhysicsBody getCollided()
    {
        return collided;
    }

    public void resolve(final float delta)
    {
        final float totalInvMass = collider.getInvMass() + collided.getInvMass();
        float tmpX;
        float tmpY;
        if (GMath.abs(penetration) > 0.001f)
        {
            if (GMath.isNearlyZero(totalInvMass))
            {
                final float colliderSpeed = collider.getVelocity().length();
                assi.set(contactNormal);
                if (GMath.isNearlyZero(colliderSpeed))
                {
                    assi.uniformScale(penetration);
                    collided.move(assi);
                } else
                {
                    float totalSpeed = collided.getVelocity().length();
                    if (GMath.isNearlyZero(totalSpeed))
                    {
                        assi.uniformScale(penetration);
                        collider.move(assi);
                    } else
                    {
                        totalSpeed += colliderSpeed;

                        assi.uniformScale(penetration);
                        tmpX = assi.getX();
                        tmpY = assi.getY();

                        assi.uniformScale(-colliderSpeed / totalSpeed);
                        collider.move(assi);

                        assi.set(tmpX, tmpY);
                        assi.uniformScale((totalSpeed - colliderSpeed) / totalSpeed);
                        collided.move(assi);
                    }
                }

            } else
            {
                assi.set(contactNormal);
                assi.uniformScale(penetration / totalInvMass);
                tmpX = assi.getX();
                tmpY = assi.getY();

                assi.uniformScale(-collider.getInvMass());
                collider.move(assi);

                assi.set(tmpX, tmpY);
                assi.uniformScale(collided.getInvMass());
                collided.move(assi);
            }
        }
        assi.set(collided.getVelocity());
        assi.sub(collider.getVelocity());
        final float separationSpeed = assi.dot(contactNormal);
        if (separationSpeed > 0 || GMath.isNearlyZero(totalInvMass))
        {
            return;
        }

        final float newSepVelocity = -separationSpeed * restituition;
        final float deltaVelocity = newSepVelocity - separationSpeed;

        final float impulse = deltaVelocity / totalInvMass;

        assi.set(contactNormal);
        assi.uniformScale(impulse);

        tmpX = assi.getX();
        tmpY = assi.getY();

        if (collider.isKinematic())
        {
            assi.add(collider.getVelocity());
            assi.uniformScale(-collider.getInvMass());
            collider.setVelocity(assi);
        }

        if (collided.isKinematic())
        {
            assi.set(tmpX, tmpY);
            assi.add(collided.getVelocity());
            assi.uniformScale(collided.getInvMass());
            collided.setVelocity(assi);
        }
    }
}
