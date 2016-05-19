package vine.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import vine.math.geometry.shape.ShapeUtil;

public class UtilTest
{

    @Test
    public void testRepeat()
    {
        assertEquals(VineMath.repeat(8, 2, 6), 2);
        assertEquals(VineMath.repeat(-8, -6, -2), -4);

        final float[] testArray = new float[] { 1, 1, 1, 0, 4, 6, 11, 0, 0 };
        SummedAreaTable.convertToSummedAreaTable(testArray, 3);
        System.out.println(Arrays.toString(testArray));
    }

    @Test
    public void testPolygonArea()
    {
        final float[] polygon = new float[] { 0, 0, 1, 0, 1, 1, 0, 1 };
        assertTrue(ShapeUtil.polygonArea(polygon, polygon.length) - 1 <= 0.00000001);
        assertTrue(ShapeUtil.polygonArea(polygon, polygon.length - 2) - 0.5f <= 0.00000001);
    }

    @Test
    public void testPolygonPerimeter()
    {
        final float[] polygon = new float[] { 0, 0, 1, 0, 1, 1, 0, 1 };
        assertTrue(ShapeUtil.polygonPerimeter(polygon, polygon.length) - 4 <= 0.00000001);
    }
}
