package vine.game.primitive;

import vine.math.HitData;
import vine.math.vector.MutableVec2f;
import vine.math.vector.Vec2f;

import vine.game.Transform;
import vine.game.scene.Component;
import vine.physics.Contact;

public class CirclePrimitive extends Component implements Primitive
{
    MutableVec2f radius = new MutableVec2f();

    @Override
    public Transform getTransform()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onUpdate(final float delta)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAttach()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDetach()
    {
        // TODO Auto-generated method stub

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

    @Override
    public boolean trace(final Primitive primitive)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void generateContact(final Primitive qq, final Contact contact)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void generateContactBox(final BoxPrimitive box, final Contact contact)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void generateContactCircle(final CirclePrimitive box, final Contact contact)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public Vec2f getExtends()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean lineTrace(final Vec2f origin, final Vec2f iDirection, final HitData result)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean boxTrace(final Vec2f origin, final Vec2f extend, final HitData result)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean circleTrace(final Vec2f origin, final float radius, final HitData result)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean trace(final Primitive primitive, final HitData result)
    {
        // TODO Auto-generated method stub
        return false;
    }
}
