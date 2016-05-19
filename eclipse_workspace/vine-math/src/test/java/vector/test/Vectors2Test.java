package vector.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import vine.math.vector.Vec2Util;

public class Vectors2Test
{
    @Test
    public void testDotProduct()
    {
        assertTrue(Vec2Util.dot(0.0, 1.0, 1.0, 0.0) == 0.0);
        assertTrue(Vec2Util.dot(0.0f, 1.0f, 1.0f, 0.0f) == 0.0f);
        assertTrue(Vec2Util.dot(-1, 1, -1, -1) == 0.f);
        assertTrue(Math.abs(Vec2Util.dot(0.4, 1.2, 0.2, 0.15) - 13 / 50f) <= Vec2Util.VEC2_EPSILON);
    }

    @Test
    public void testCrossProduct()
    {
        assertTrue(Vec2Util.pseudoCross(1.0, 1.0, 1.0, 1.0) == 0);
        assertTrue(Vec2Util.pseudoCross(-1.0, 1.0, 1.0, 1.0) == -2);
        assertTrue(Vec2Util.pseudoCross(1.0, 1.0, -1.0, 1.0) == 2);
        assertTrue(Vec2Util.pseudoCross(1.0f, 1.0f, 1.0f, 1.0f) == 0);
        assertTrue(Vec2Util.pseudoCross(-1.0f, 1.0f, 1.0f, 1.0f) == -2);
        assertTrue(Vec2Util.pseudoCross(1.0f, 1.0f, -1.0f, 1.0f) == 2);
    }

    @Test
    public void testAngleCalculation()
    {
        assertTrue(Math.abs(Vec2Util.getAngle(1, 1, 0, 1) - Math.PI / 4) <= Vec2Util.VEC2_EPSILON);
        assertTrue(Math.abs(Vec2Util.getAngle(0, 1, 1, 1) + Math.PI / 4) <= Vec2Util.VEC2_EPSILON);
        assertTrue(Math.abs(Vec2Util.getAngle(0, 1, 0, -1) - Math.PI) <= Vec2Util.VEC2_EPSILON);
        assertTrue(Math.abs(Vec2Util.getAngle(0, -1, 0, 1) - Math.PI) <= Vec2Util.VEC2_EPSILON);
        assertTrue(Math.abs(Vec2Util.getAngle(1, 0, -1, 0) - Math.PI) <= Vec2Util.VEC2_EPSILON);
        assertTrue(Math.abs(Vec2Util.getAngle(-1, 0, 1, 0) - Math.PI) <= Vec2Util.VEC2_EPSILON);
    }

    @Test
    public void testLengthCalculation()
    {
        assertTrue(Vec2Util.length(0, 0) == 0);
        assertTrue(Vec2Util.length(1, 0) == 1);
        assertTrue(Vec2Util.length(1, 1) == Math.sqrt(2));
        assertTrue(Vec2Util.squaredLength(1.f, 1) == 2);
        assertTrue(Vec2Util.squaredLength(1., 1.0) == 2);
        assertTrue(Vec2Util.length(0, -1) == 1);
    }
}
