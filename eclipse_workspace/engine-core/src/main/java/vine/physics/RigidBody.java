package vine.physics;

import vine.game.scene.Component;
import vine.math.VineMath;
import vine.math.vector.MutableVec2f;

public class RigidBody extends Component implements PhysicsBody
{
    float        inversedMass          = 0.1f;
    float        linearDamping         = 1f;
    MutableVec2f position              = new MutableVec2f();
    MutableVec2f velocity              = new MutableVec2f();
    MutableVec2f acceleration          = new MutableVec2f();
    MutableVec2f linearForce           = new MutableVec2f();
    MutableVec2f linearImpuls          = new MutableVec2f();
    MutableVec2f forceAccumualtor      = new MutableVec2f();

    MutableVec2f lastFrameAcceleration = new MutableVec2f();

    public void integrate(final float delta)
    {
        // Calculate current acceleration
        this.lastFrameAcceleration.set(this.acceleration);
        this.forceAccumualtor.uniformScale(this.inversedMass);
        this.lastFrameAcceleration.add(this.acceleration);

        // Update linear velocity
        this.lastFrameAcceleration.uniformScale(delta);
        this.velocity.add(this.lastFrameAcceleration);
        this.velocity.uniformScale((float) VineMath.pow(this.linearDamping, delta));

        // Update linear position
        final float x = this.velocity.getX() * delta;
        final float y = this.velocity.getY() * delta;
        this.position.add(x, y);
    }

    @Override
    public void onPhysicsUpdate(final float delta)
    {
        integrate(delta);
        this.entity.setPosition(this.position.getX(), this.position.getY());
    }

    @Override
    public void onUpdate(final float delta)
    {
    }

    @Override
    public void onAttach()
    {
        this.entity.getScene().getWorld().getPhysics().addPhysicBody(this);
    }

    @Override
    public void onDetach()
    {
        this.entity.getScene().getWorld().getPhysics().removePhysicBody(this);
    }

    @Override
    public void onDeactivation()
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void onActivation()
    {
        // TODO Auto-generated method stub
    }
}
