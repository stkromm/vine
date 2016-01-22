package vine.intersection;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import vine.math.geometry.Circle;
import vine.math.geometry.Rectangle;

/**
 * @author Steffen
 *
 */
public class ShapeContainTest {
    /**
     * 
     */
    @Test
    public void testCircleContainsPoint() {
        final Circle circle = new Circle(0, 0, 1);
        assertTrue(circle.contains(0.1f, 0.5f));
        assertTrue(!circle.contains(1.1f, 0.5f));
        Rectangle rectangle = new Rectangle(0, 0, 1, 1);
        assertTrue(rectangle.contains(1.f, 0.5f));
    }
}
