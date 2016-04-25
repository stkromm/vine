package vine.application;

import vine.physics.Intersection2D;

public class BenchmarkIntersection
{

    public static void main(String[] args)
    {
        final long startTime = System.nanoTime();
        float a, b, c, d, e, f, g, h;
        a = b = c = d = e = f = g = h = (float) Math.random();
        for (int i = 1000000; i > 0; i--)
        {
            Intersection2D.whereDoesRayIntersectAabb(a, b, c, d, e, f, g, h);
        }
        final float time = (System.nanoTime() - startTime) / 1000000f;
        System.out.println("1million ray aabb intersection took: " + time + " millis");
    }

}
