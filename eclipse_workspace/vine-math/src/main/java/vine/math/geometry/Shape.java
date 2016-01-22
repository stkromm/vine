package vine.math.geometry;

import vine.math.geometry.ray.Intersection;
import vine.math.geometry.ray.Ray;

/**
 * @author steffen
 *
 */
public interface Shape {
    /**
     * Returns true, if the given point is contained in the convex shape.
     * 
     * @param x
     * @param y
     * @return
     */
    boolean contains(float x, float y);

    /**
     * Returns a intersection object that contains information of the
     * intersection of the ray and the shape.
     * 
     * @param ray
     * @return
     */
    Intersection intersect(Ray ray);
}
