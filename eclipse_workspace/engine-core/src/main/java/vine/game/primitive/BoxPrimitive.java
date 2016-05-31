package vine.game.primitive;

import vine.game.Transform;
import vine.game.scene.Component;
import vine.game.scene.TraceResult;
import vine.math.GMath;
import vine.math.Intersection;
import vine.math.vector.MutableVec2f;
import vine.math.vector.Vec2f;
import vine.physics.Contact;

public class BoxPrimitive extends Component implements Primitive
{
    Vec2f     boundingBox = new Vec2f(32, 64);
    Transform transform   = new Transform();

    @Override
    public void onUpdate(final float delta)
    {//
    }

    @Override
    public void onAttach()
    {
        transform.setParent(entity.getTransform());
    }

    @Override
    public void onDetach()
    {
        transform.setParent(null);
    }

    @Override
    public void onDeactivation()
    {//
    }

    @Override
    public void onActivation()
    {//
    }

    @Override
    public Transform getTransform()
    {
        return transform;
    }

    @Override
    public boolean lineTrace(final Vec2f origin, final Vec2f iDirection, final TraceResult result)
    {
        return Intersection.doesAABBIntersectRay(origin, iDirection, transform.getWorldPosition(), boundingBox);
    }

    @Override
    public boolean boxTrace(final Vec2f origin, final Vec2f extend, final TraceResult result)
    {
        return Intersection.doesAABBIntersectAABB(origin, extend, transform.getWorldPosition(), boundingBox);
    }

    @Override
    public boolean circleTrace(final Vec2f origin, final float radius, final TraceResult result)
    {
        return Intersection.doesAABBIntersectCircle(transform.getWorldPosition(), boundingBox, origin, radius);
    }

    @Override
    public boolean trace(final Primitive primitive)
    {
        return primitive.boxTrace(transform.getWorldPosition(), boundingBox, null);
    }

    @Override
    public void generateContact(final Primitive primitive, final Contact contact)
    {
        primitive.generateContactBox(this, contact);
    }

    MutableVec2f tmp1 = new MutableVec2f();

    @Override
    public void generateContactBox(final BoxPrimitive box, final Contact contact)
    {
        tmp1.set(transform.getWorldPosition());
        tmp1.sub(box.transform.getWorldPosition());

        final float xOverlap = box.boundingBox.getX() / 2 + boundingBox.getX() / 2 - GMath.abs(tmp1.getX());
        final float yOverlap = box.boundingBox.getY() / 2 + boundingBox.getY() / 2 - GMath.abs(tmp1.getY());
        if (yOverlap < xOverlap)
        {
            if (tmp1.getY() < 0)
            {
                contact.setContactNormal(0, -1);
            }
            {
                contact.setContactNormal(0, 1);
            }
            contact.setPenetration(yOverlap);

        } else
        {
            if (tmp1.getX() < 0)
            {
                contact.setContactNormal(-1, 0);
            } else
            {
                contact.setContactNormal(1, 0);
            }
            contact.setPenetration(xOverlap);
        }
        // Log.debug(
        // "Normal " + contact.getContactNormal() + " ,Penetration " +
        // contact.getPenetration() + " ,x overlap"
        // + xOverlap + " ,y overlap" + yOverlap);
    }

    @Override
    public void generateContactCircle(final CirclePrimitive box, final Contact contact)
    {
        // TODO
    }

    @Override
    public Vec2f getExtends()
    {
        return boundingBox;
    }
}
