package geometry.shape.test;

import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import vine.math.VineMath;
import vine.math.geometry.shape.Circle;
import vine.math.vector.Vec2f;

public class CircleTest
{
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testAccessorcs()
    {
        final Circle cirlce = new Circle(new Vec2f(), 5);
        assertTrue(cirlce.getRadius() == 5);
        assertTrue(cirlce.getX() == 0);
        assertTrue(cirlce.getY() == 0);
    }

    @Test
    public void testPerimeter()
    {
        final Circle cirlce = new Circle(0, 0, 5);
        assertTrue(cirlce.getCircumference() == VineMath.PIF * 10);
    }

    @Test
    public void testArea()
    {
        Circle cirlce = new Circle(0, 0, 5);
        assertTrue(cirlce.getArea() == VineMath.PIF * 25);
        cirlce = new Circle(0, 0, 0);
        assertTrue(cirlce.getArea() == 0);
    }

    /**
     * Copying null should throw an exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreationFromNullCircle()
    {
        final Circle c = new Circle(null);
        c.equals(null);
    }

    /**
     * Null as origin vector should throw an exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreationFromNullOrigin()
    {
        final Circle c = new Circle(null, 0);
        c.equals(null);
    }

    @Test
    public void testContainment()
    {
        final Circle circle = new Circle(0, 1, 5);
        assertTrue(circle.contains(3, 3));
        assertTrue(circle.contains(0, 1));
        assertTrue(!circle.contains(0, 6.000001f));
        assertTrue(!circle.contains(new Vec2f(0, 6.000001f)));
        assertTrue(circle.contains(new Vec2f(3, 3)));
    }

    @Test
    public void testObjectMethods()
    {
        final Circle circle = new Circle(0, 1, 5);
        final Circle circle2 = new Circle(circle);
        assertTrue(circle2.toString().equals(circle.toString()));
        assertTrue(circle2.hashCode() == circle.hashCode());
        assertTrue(circle.equals(circle2));
        assertTrue(circle.equals(circle));
        assertTrue(!circle.equals(new Vec2f()));
        assertTrue(!circle.equals(null));
        assertTrue(!circle.equals(new Circle(1, 1, 5)));
        assertTrue(!circle.equals(new Circle(0, 0, 5)));
        assertTrue(!circle.equals(new Circle(0, 1, 4)));
        assertTrue(!circle.equals(new Circle(0, 0, 4)));
        assertTrue(!circle.equals(new Circle(3, 1, 3)));
    }
}
