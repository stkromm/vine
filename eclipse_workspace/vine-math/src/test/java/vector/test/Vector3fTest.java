package vector.test;

import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

import vine.math.vector.MutableVec3f;
import vine.math.vector.Vec3f;

/**
 * @author Steffen
 *
 */
public class Vector3fTest
{
    /**
     *
     */
    @Test
    public void testAdd()
    {
        final MutableVec3f vector = new MutableVec3f(1.f, 2.f, 3.f);
        vector.add(new Vec3f(2, 2.f, 2.f));
        assertTrue(vector.equals(new Vec3f(3, 4, 5)));
        vector.add(-5.f, 1.f, -5.f);
        assertTrue(vector.equals(new Vec3f(-2, 5, 0)));
        vector.add(null);
        assertTrue(vector.equals(new Vec3f(-2, 5, 0)));
        assertTrue(!vector.equals(null));
        assertTrue(!vector.equals(new Vec3f(-1, 5, 0)));
        assertTrue(!vector.equals(new Vec3f(-1, 6, 0)));
        assertTrue(!vector.equals(new Vec3f(-2, 6, 0)));
        assertTrue(!vector.equals(new Vec3f(-2, 6, -2)));
        assertTrue(!vector.equals(new Vec3f(-2, 6, 11)));
        assertTrue(!vector.equals(new Vec3f(-2, 5, 5)));
    }

    /**
     *
     */
    @Test
    public void testPropertyAccess()
    {
        final MutableVec3f vector = new MutableVec3f(0, 0, 0);
        assertTrue(vector.getX() == 0);
        vector.setX(1);
        assertTrue(vector.getX() == 1);
        vector.setY(1);
        assertTrue(vector.getY() == 1);
        vector.setZ(5);
        assertTrue(vector.getZ() == 5);
        final Vec3f vector2 = new Vec3f(vector);
        assertTrue(vector2.getX() == 1);
        assertTrue(vector2.getY() == 1);
        assertTrue(vector2.getZ() == 5);
    }

    /**
     *
     */
    @Test
    public void testNormalize()
    {
        final MutableVec3f vector = new MutableVec3f(2, 2, 1);
        vector.normalize();
        System.out.println(Math.abs(vector.getX()));
        assertTrue(Math.abs(Math.abs(vector.getX()) - 2.f / 3.f) <= Vec3f.EPSILON);
        assertTrue(Math.abs(Math.abs(vector.getY()) - 2.f / 3.f) <= Vec3f.EPSILON);
        assertTrue(Math.abs(Math.abs(vector.getZ()) - 1.f / 3.f) <= Vec3f.EPSILON);
        System.out.println(Math.abs(vector.length()));
        assertTrue(Math.abs(Math.abs(vector.length()) - 1) <= Vec3f.EPSILON);
        final MutableVec3f nullVector = new MutableVec3f(0, 0, 0);
        nullVector.normalize();
        assertTrue(nullVector.length() == 0);
        final Random rn = new Random();
        for (int i = 0; i <= 100000; i++)
        {
            final MutableVec3f vec = new MutableVec3f(rn.nextFloat() * 10000,
                    rn.nextFloat() * System.currentTimeMillis() - 12345, System.nanoTime());
            vec.normalize();
            assertTrue(Math.abs(Math.abs(vec.length()) - 1) <= Vec3f.EPSILON);
        }
    }

    /**
     *
     */
    @Test
    public void testDotProduct()
    {
        final Vec3f vector = new Vec3f(1, 0, 0);
        assertTrue(vector.dot(vector) == 1.f);
        assertTrue(vector.dot(null) == 0);
        assertTrue(vector.squaredLength() == vector.dot(vector));
    }

    /**
     *
     */
    @Test
    public void testCrossProduct()
    {
        final MutableVec3f vector = new MutableVec3f(1, 0, 0);
        final Vec3f vector2 = new Vec3f(0, 1, 0);
        assertTrue(vector.cross(vector2).equals(new Vec3f(0, 0, 1)));
        assertTrue(vector.cross(null).equals(new Vec3f(0, 0, 0)));
        vector.crossInThis(vector2);
        assertTrue(vector.equals(new Vec3f(0, 0, 1)));
    }

    /**
     *
     */
    @Test
    public void testGetElement()
    {
        final Vec3f vector = new Vec3f(1, 2.f, 5);
        assertTrue(vector.getY() == 2.f);
        assertTrue(vector.getZ() == 5.f);
        assertTrue(vector.getX() == 1.f);
    }

    /**
     *
     */
    @Test
    public void testScaling()
    {
        final Vec3f vector = new Vec3f(1, 2, 1);
        vector.scale(0);
        assertTrue(vector.equals(new Vec3f(0, 0, 0)));
    }

    /**
     *
     */
    @Test
    public void testAngleBetween()
    {
        Vec3f vector = new Vec3f(3.f, 4.f, 0);
        Vec3f vector2 = new Vec3f(-8.f, 6.f, 0);
        assertTrue(Math.toDegrees(Math.acos(vector.getAngle(vector2))) == 90.f);
        assertTrue(vector.getAngle(null) == 0);
        vector2 = new Vec3f(0, 0, 0);
        assertTrue(vector.getAngle(vector2) == 0);
        vector = new Vec3f(0, 0, 0);
        assertTrue(vector2.getAngle(vector) == 0);
    }

    @Test
    public void testHashcode()
    {
        final Vec3f vector = new Vec3f(3.f, 4.f, 0);
        final Vec3f vec2 = new Vec3f(vector);
        assertTrue(vector.hashCode() == vec2.hashCode());
        assertTrue(vector.toString().equals(vec2.toString()));
    }

    /**
     *
     */
    @Test
    public void testLength()
    {
        final Vec3f vector = new Vec3f(0.f, 3.f, 0.f);
        assertTrue(vector.length() == 3.f);
        assertTrue(vector.squaredLength() == 9);
    }
}
