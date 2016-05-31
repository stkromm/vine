package vine.physics;

import java.util.ArrayList;
import java.util.List;

import vine.game.Transform;
import vine.game.primitive.BoxPrimitive;
import vine.game.primitive.Primitive;
import vine.game.scene.Component;
import vine.math.GMath;
import vine.math.geometry.shape.AABB;
import vine.math.geometry.shape.MutableAABB;
import vine.math.vector.MutableVec2f;
import vine.math.vector.Vec2f;

public class RigidBody extends Component implements PhysicsBody
{
    Transform       transform;
    MutableAABB     aabb                  = new MutableAABB(0, 0, 32, 64);
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
            aabb.setHeight(b.getExtends().getY());
            aabb.setWidth(b.getExtends().getX());
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
        transform.translate(delta * velocity.getX(), delta * velocity.getY());
        // this.position.addScaled(delta, this.velocity);

        impulsAcc.set(0, 0);
        forceAccumualtor.set(0, 0);
        if (GMath.isNearlyZero(velocity.getX()) && GMath.isNearlyZero(velocity.getY()))
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
    public AABB getAABB()
    {
        return aabb;
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
    public void move(final MutableVec2f assi)
    {
        transform.translate(assi.getX(), assi.getY());
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
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isAsleep()
    {
        // TODO Auto-generated method stub
        return false;
    }
}
