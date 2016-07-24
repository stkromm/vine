package vine.physics;

import vine.math.GMath;
import vine.math.geometry.shape.Aabb;
import vine.math.geometry.shape.MutableAabb;
import vine.math.vector.MutableVec2f;
import vine.math.vector.Vec2f;

import java.util.ArrayList;
import java.util.List;

import vine.game.Transform;
import vine.game.primitive.BoxPrimitive;
import vine.game.primitive.Primitive;
import vine.game.scene.Component;

public class RigidBody extends Component implements PhysicsBody
{
    Transform       transform;
    MutableAabb     Aabb                  = new MutableAabb(0, 0, 32, 64);
    List<Primitive> primitives            = new ArrayList<Primitive>();
    float           inversedMass          = 1f;
    float           linearDamping         = 0.99f;
    MutableVec2f    velocity              = new MutableVec2f();
    MutableVec2f    acceleration          = new MutableVec2f();
    MutableVec2f    linearForce           = new MutableVec2f();
    MutableVec2f    linearImpuls          = new MutableVec2f();
    MutableVec2f    forceAccumualtor      = new MutableVec2f();
    MutableVec2f    impulsAcc             = new MutableVec2f();
    MutableVec2f    lastFrameAcceleration = new MutableVec2f();
    boolean         ignoreMass            = false;
    boolean         asleep                = false;

    MutableVec2f    tickMove              = new MutableVec2f(0, 0);

    public void setTransform(final Transform transform)
    {
        this.transform = transform;
    }

    public void addPrimitive(final Primitive primitive)
    {
        primitives.add(primitive);
        if (primitive instanceof BoxPrimitive)
        {
            final BoxPrimitive b = (BoxPrimitive) primitive;
            Aabb.setHeight(b.getExtends().getY());
            Aabb.setWidth(b.getExtends().getX());
        }
    }

    public void integrate(final float delta)
    {
        if (asleep)
        {
            return;
        }

        // Calculate current acceleration
        lastFrameAcceleration.set(acceleration);
        if (ignoreMass)
        {
            lastFrameAcceleration.add(forceAccumualtor);
        } else
        {
            lastFrameAcceleration.addScaled(inversedMass, forceAccumualtor);
        }
        // Update linear velocity
        lastFrameAcceleration.uniformScale(delta);
        velocity.add(lastFrameAcceleration);
        velocity.uniformScale(GMath.fastPow(linearDamping, delta));
        if (ignoreMass)
        {
            velocity.add(impulsAcc);
        } else
        {
            velocity.addScaled(inversedMass, impulsAcc);
        }
        // Update linear position
        transform.translate(tickMove.getX() + delta * velocity.getX(), tickMove.getY() + delta * velocity.getY());
        tickMove.set(0, 0);
        // this.position.addScaled(delta, this.velocity);

        impulsAcc.set(0, 0);
        forceAccumualtor.set(0, 0);
        if (velocity.isNearlyZero() && tickMove.isNearlyZero())
        {
            asleep = true;
        }
    }

    public void addForce(final float x, final float y)
    {
        forceAccumualtor.add(x, y);
        asleep = false;
    }

    public void addImpuls(final float x, final float y)
    {
        impulsAcc.add(x, y);
        asleep = false;
    }

    @Override
    public void onPhysicsUpdate(final float delta)
    {
        integrate(delta / 1000.f);
    }

    @Override
    public void onAttach()
    {
        transform = entity.getTransform();
        entity.getScene().getWorld().getPhysics().addPhysicBody(this);
    }

    @Override
    public void onDetach()
    {
        entity.getScene().getWorld().getPhysics().removePhysicBody(this);
    }

    @Override
    public void onUpdate(final float delta)
    {//
    }

    @Override
    public void onDeactivation()
    {//
    }

    @Override
    public void onActivation()
    {//
    }

    public float getSpeedY()
    {
        return velocity.getY();
    }

    public float getSpeedX()
    {
        return velocity.getX();
    }

    @Override
    public Transform getTransform()
    {
        return transform;
    }

    @Override
    public Aabb getAabb()
    {
        return Aabb;
    }

    @Override
    public CollisionResponse getCollisionResponse()
    {
        return CollisionResponse.IGNORE;
    }

    @Override
    public List<Primitive> getPrimitives()
    {
        return primitives;
    }

    @Override
    public Vec2f getVelocity()
    {
        return velocity;
    }

    @Override
    public float getInvMass()
    {
        return inversedMass;
    }

    @Override
    public void setVelocity(final Vec2f velocity)
    {
        this.velocity.set(velocity);
        asleep = false;
    }

    @Override
    public Vec2f getPosition()
    {
        return transform.getWorldPosition();
    }

    @Override
    public void move(final Vec2f assi)
    {
        tickMove.add(assi);
    }

    public void ignoreMass()
    {
        ignoreMass = true;
    }

    public void setInvMass(final float i)
    {
        inversedMass = i;
    }

    public void setDamping(final float v)
    {
        linearDamping = v;
    }

    @Override
    public boolean isKinematic()
    {
        return false;
    }

    @Override
    public boolean isAsleep()
    {
        return asleep;
    }
}
