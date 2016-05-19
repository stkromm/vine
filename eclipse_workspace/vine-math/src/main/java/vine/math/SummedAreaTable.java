package vine.math;

public final class SummedAreaTable
{
    private SummedAreaTable()
    {
        // Utility
    }

    public static void convertToSummedAreaTable(final float[] values, final int width)
    {
        final float[] result = values;
        final int height = values.length / width;
        for (int i = 1; i < width * height; i++)
        {
            final int x = i % width;
            if (x > 0)
            {
                result[i] += result[i - 1];
            }
        }
        for (int j = 1; j < width * height; j++)
        {
            final int y = j / width;
            if (y > 0)
            {
                result[j] += result[j - width];
            }
        }
    }
}
