package geometry.shape.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import vine.math.geometry.shape.AABB;
import vine.math.vector.Vec2f;

public class AABBTest
{
    @Test
    public void testContainment()
    {
        final AABB aabb = new AABB(0, 0, 1, 1);
        assertTrue(aabb.contains(new Vec2f(1, 1)));
        assertTrue(!aabb.contains(new Vec2f(-1, 1)));
        assertTrue(!aabb.contains(-1, -1));
        assertTrue(aabb.contains(1, 0));
    }

    @Test
    public void testArea()
    {
        final AABB aabb = new AABB(0, 0, 2, 1);
        assertTrue(aabb.getArea() == 2);
    }

    @Test
    public void testCircumference()
    {
        final AABB aabb = new AABB(0, 0, 2, 1);
        assertTrue(aabb.getCircumference() == 6);
    }

    @Test
    public void testAccessors()
    {
        final AABB aabb = new AABB(0, 0, 2, 1);
        assertTrue(aabb.getX() == 0);
        assertTrue(aabb.getY() == 0);
        assertTrue(aabb.getWidth() == 2);
        assertTrue(aabb.getHeight() == 1);
    }

    /**
     * Null as origin vector should throw an exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreationFromNullOrigin()
    {
        AABB aabb = new AABB(new Vec2f(0, 0), null);
        aabb = new AABB(null, new Vec2f(0, 0));
        aabb.equals(null);
    }

    @Test
    public void testCreationWithNegativExtends()
    {
        final AABB aabb = new AABB(new Vec2f(0, 0), new Vec2f(-1, -1));
        assertTrue(aabb.getHeight() == 1);
        assertTrue(aabb.getWidth() == 1);
    }

    @Test
    public void testEquals()
    {
        final AABB aabb = new AABB(1, 1, 4, 4);
        assertTrue(!aabb.equals(null));
        assertTrue(aabb.equals(new AABB(1, 1, 4, 4)));
        assertTrue(!aabb.equals(new AABB(1, 1, 4, 3)));
        assertTrue(!aabb.equals(new AABB(1, 1, 3, 4)));
        assertTrue(!aabb.equals(new AABB(1, 2, 4, 3)));
        assertTrue(!aabb.equals(new AABB(2, 1, 4, 4)));
        assertTrue(!aabb.equals(new AABB(2, -1, -4, 3)));
    }

    @Test
    public void testHashcode()
    {
        final AABB aabb = new AABB(new Vec2f(0, 0), new Vec2f(-1, -1));
        assertTrue(new AABB(aabb).hashCode() == aabb.hashCode());
    }

    @Test
    public void testStringRepresentation()
    {
        final AABB aabb = new AABB(new Vec2f(0, 0), new Vec2f(-1, -1));
        assertTrue(new AABB(aabb).toString().equals(aabb.toString()));
    }
}
