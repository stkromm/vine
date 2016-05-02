package vine.application;

import vine.math.geometry.Intersection2D;
import vine.util.Log;

public final class BenchmarkIntersection
{

    public static void main(final String... args)
    {
        final long startTime = System.nanoTime();
        float a, b, c, d, e, f, g, h;
        a = b = c = d = e = f = g = h = (float) Math.random();
        for (int i = 1000000; i > 0; i--)
        {
            Intersection2D.whereDoesRayIntersectAabb(a, b, c, d, e, f, g, h);
        }
        final float time = (System.nanoTime() - startTime) / 1000000f;
        Log.benchmark("1million ray aabb intersection took: " + time + " millis");
    }

}
