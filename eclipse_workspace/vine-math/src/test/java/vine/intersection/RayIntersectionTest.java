package vine.intersection;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import vine.math.Vector2f;
import vine.math.geometry.Circle;
import vine.math.geometry.ray.Intersection;
import vine.math.geometry.ray.Ray;

/**
 * @author Steffen
 *
 */
public class RayIntersectionTest {

    /**
     * 
     */
    @Test
    public void testCircleRayIntersection() {
        Ray ray = new Ray(new Vector2f(0.1f, 0.1f), new Vector2f(0, 1));
        Circle circle = new Circle(1, 1, 1);
        Intersection intersec = circle.intersect(ray);
        assertTrue(intersec.isHit());
        ray = new Ray(new Vector2f(0, -2), new Vector2f(1, 1));
        intersec = circle.intersect(ray);
        assertTrue(!intersec.isHit());
        ray = new Ray(new Vector2f(-1, -1f), new Vector2f(1.5f, 1));
        intersec = circle.intersect(ray);
        assertTrue(intersec.isHit());
        ray = new Ray(new Vector2f(2, 2), new Vector2f(0, 1));
        intersec = circle.intersect(ray);
        assertTrue(intersec.isHit());
    }
}
