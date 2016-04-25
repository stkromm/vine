package vine.physics;

import vine.math.Vec2f;

public final class Intersection2D
{
    private Intersection2D()
    {

    }

    public static boolean doesAabbIntersectAabb(
            final Vec2f positionA,
            final Vec2f extendsA,
            final Vec2f positionB,
            final Vec2f extendsB)
    {
        return Intersection2D.doesAabbIntersectAabb(
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
            float pos1X,
            float pos1Y,
            float ext1X,
            float ext1Y,
            float pos2X,
            float pos2Y,
            float ext2X,
            float ext2Y)
    {
        return pos1X <= pos2X + ext2X && //
                pos2X <= pos1X + ext1X && //
                pos1Y <= pos2Y + ext2Y && //
                pos2Y <= pos1Y + ext1Y;
    }

    public static boolean doesRayIntersectAabb(
            final Vec2f origin,
            final Vec2f direction,
            final Vec2f position,
            final Vec2f extend)
    {
        return Float.MAX_VALUE != Intersection2D.whereDoesRayIntersectAabb(origin, direction, position, extend);
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
        final float b = Vec2f.dot(direction.getX() * 2, direction.getY() * 2, c1, c2);
        final float c = Vec2f.dot(c1, c2, c1, c2) - radius * radius;
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
        return Intersection2D.whereDoesRayIntersectAabb(
                origin.getX(),
                origin.getY(),
                inversedDirX,
                inversedDirY,
                position.getX(),
                position.getY(),
                extend.getX(),
                extend.getY());
    }

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

        final float tmin = Math.max(Math.min(t1, t2), Math.min(t3, t4));
        final float tmax = Math.min(Math.max(t1, t2), Math.max(t3, t4));
        if (tmax < 0 || tmin > tmax)
        {
            // return tmax
            return Float.MAX_VALUE;
        }
        return tmin;
    }

}
