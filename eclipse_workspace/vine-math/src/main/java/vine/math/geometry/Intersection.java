package vine.math.geometry;

import vine.math.VineMath;
import vine.math.geometry.shape.AABB;
import vine.math.vector.Vec2f;
import vine.math.vector.Vec2Util;

public final class Intersection
{
    private Intersection()
    {

    }

    public static boolean doesAabbIntersectAabb(final AABB aabb1, final AABB aabb2)
    {
        return doesAabbIntersectAabb(
                aabb1.getX(),
                aabb1.getY(),
                aabb1.getWidth(),
                aabb1.getHeight(),
                aabb2.getX(),
                aabb2.getY(),
                aabb2.getWidth(),
                aabb2.getHeight());
    }

    public static boolean doesAabbIntersectAabb(
            final Vec2f positionA,
            final Vec2f extendsA,
            final Vec2f positionB,
            final Vec2f extendsB)
    {
        return Intersection.doesAabbIntersectAabb(
                positionA.getX(),
                positionA.getY(),
                extendsA.getX(),
                extendsA.getY(),
                positionB.getX(),
                positionB.getY(),
                extendsB.getX(),
                extendsB.getY());
    }

    public static boolean doesAabbIntersectAabb(
            final float posAX,
            final float posAY,
            final float extAX,
            final float extAY,
            final float posBX,
            final float posBY,
            final float extBX,
            final float extBY)
    {
        return posAX <= posBX + extBX && //
                posBX <= posAX + extAX && //
                posAY <= posBY + extBY && //
                posBY <= posAY + extAY;
    }

    public static boolean doesCircleIntersectCircle(
            final Vec2f centerA,
            final float radiusA,
            final Vec2f centerB,
            final float radiusB)
    {
        final float radiusSum = radiusA + radiusB;
        return radiusSum * radiusSum >= centerA.squaredDistance(centerB);
    }

    public static boolean doesRayIntersectRay(
            final Vec2f origin1,
            final Vec2f direction1,
            final Vec2f origin2,
            final Vec2f direction2)
    {
        // TODO True if equal (same line)
        return Vec2Util.getSlope(direction1) != Vec2Util.getSlope(direction2);
    }

    public static boolean doesRayIntersectAabb(
            final Vec2f origin,
            final Vec2f direction,
            final Vec2f position,
            final Vec2f extend)
    {
        return Float.MAX_VALUE != Intersection.whereDoesRayIntersectAabb(origin, direction, position, extend);
    }

    public static boolean doesRayIntersectCircle(
            final Vec2f origin,
            final Vec2f direction,
            final Vec2f point,
            final float radius)
    {
        final float a = direction.dot(direction);
        final float c1 = origin.getX() - point.getX();
        final float c2 = origin.getY() - point.getY();
        final float b = Vec2Util.dot(direction.getX() * 2, direction.getY() * 2, c1, c2);
        final float c = Vec2Util.dot(c1, c2, c1, c2) - radius * radius;
        final float d = b * b - 4 * a * c;

        return d >= 0;
    }

    public static boolean doesAabbIntersectCircle(
            final Vec2f position,
            final Vec2f extend,
            final Vec2f point,
            final float radius)
    {
        final boolean xIsWithin = point.getX() <= position.getX() && point.getX() + radius >= position.getX()
                || point.getX() >= position.getX() + extend.getX()
                        && point.getX() - radius <= position.getX() + extend.getX();
        return xIsWithin;
    }

    public static float whereDoesRayIntersectAabb(
            final Vec2f origin,
            final Vec2f direction,
            final Vec2f position,
            final Vec2f extend)
    {

        final float inversedDirX = 1 / direction.getX();
        final float inversedDirY = 1 / direction.getY();
        return Intersection.whereDoesRayIntersectAabb(
                origin.getX(),
                origin.getY(),
                inversedDirX,
                inversedDirY,
                position.getX(),
                position.getY(),
                extend.getX(),
                extend.getY());
    }

    /**
     * Calculates the length of the ray from the origin to the intersection.
     * <p>
     * The given direction has to be normalized or the behaviour is undefined.
     * </p>
     * If there is no intersection the yield result is Float.MAX_VALUE
     */
    public static float whereDoesRayIntersectAabb(
            final float originX,
            final float originY,
            final float iDirecX,
            final float iDirecY,
            final float positionX,
            final float positionY,
            final float extendX,
            final float extendY)
    {
        final float t1 = (positionX - originX) * iDirecX;
        final float t2 = (positionX + extendX - originX) * iDirecX;
        final float t3 = (positionY - originY) * iDirecY;
        final float t4 = (positionY + extendY - originY) * iDirecY;

        final float tmin = VineMath.max(VineMath.min(t1, t2), VineMath.min(t3, t4));
        final float tmax = VineMath.min(VineMath.max(t1, t2), VineMath.max(t3, t4));
        return tmax < 0 || tmin > tmax ? Float.MAX_VALUE : tmin;
    }

}
