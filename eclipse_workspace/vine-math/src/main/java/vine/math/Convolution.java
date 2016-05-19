package vine.math;

public final class Convolution
{
    public static final float[] SOBEL                = null;
    public static final float[] SHARPEN              = null;
    public static final float[] EMBOSSING_RIGHT_UP   = null;
    public static final float[] EMBOSSING_RIGHT_DOWN = null;
    public static final float[] LOWPASS              = null;
    public static final float[] HIGHPASS             = null;

    private Convolution()
    {
        // Utiltiy class
    }

    public static void convolute(
            final float[] values,
            final int valuesWidth,
            final float[] filter,
            final int filterWidth,
            final float[] dest)
    {
        final int valuesHeight = values.length / valuesWidth;
        final int filterHeight = filter.length / filterWidth;

        for (int a = valuesWidth - 1; a >= 0; a--)
        {
            for (int b = valuesHeight - 1; b >= 0; b--)
            {
                float tmp = 0;
                for (int i = filterWidth - 1; i >= 0; i--)
                {
                    for (int j = filterHeight - 1; j >= 0; j--)
                    {
                        final int x = VineMath.clamp(a - filterWidth / 2 + i, 0, valuesWidth);
                        final int y = VineMath.clamp(b - filterHeight / 2 + j, 0, valuesHeight);
                        tmp += values[a + valuesWidth * b] * filter[x + y * filterWidth];
                    }
                }
                dest[a + valuesWidth * b] = tmp;
            }
        }

    }
}
