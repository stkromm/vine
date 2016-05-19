package vine.math;

import org.junit.Test;

public class RandomTest
{
    @Test
    public void testPow()
    {
        long time = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++)
        {
            Math.pow(i, 3);
        }
        System.out.println(System.currentTimeMillis() - time);
        time = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++)
        {
            VineMath.pow(i, 3);
        }
        System.out.println(System.currentTimeMillis() - time);
    }
}
