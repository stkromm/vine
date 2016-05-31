package vine.game.primitive;

import vine.game.Transform;
import vine.game.scene.TraceResult;
import vine.math.vector.Vec2f;
import vine.physics.Contact;

/**
 * Implemented by components, that represent geometric data.
 * 
 * Design role model: Primitive Component of Unreal Engine 4
 * 
 * @author Steffen Kromm, first created on 20.05.2016
 *
 */
public interface Primitive
{
    Vec2f getExtends();

    Transform getTransform();

    boolean lineTrace(Vec2f origin, Vec2f iDirection, TraceResult result);

    boolean boxTrace(Vec2f origin, Vec2f extend, TraceResult result);

    boolean circleTrace(Vec2f origin, float radius, TraceResult result);

    boolean trace(Primitive primitive);

    void generateContact(Primitive qq, Contact contact);

    void generateContactBox(BoxPrimitive box, Contact contact);

    void generateContactCircle(CirclePrimitive box, Contact contact);
}
