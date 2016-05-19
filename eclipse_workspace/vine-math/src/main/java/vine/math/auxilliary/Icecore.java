package vine.math.auxilliary;

import vine.math.VineMath;

///////////////////////////////////////
// Icecore's atan2 ( http://www.java-gaming.org/topics/extremely-fast-atan2/36467/msg/346145/view.html#msg346145 )
///////////////////////////////////////

public final class Icecore
{

    private static final int   SIZE_ACC    = 100000;
    private static final int   SIZE_AR     = SIZE_ACC + 1;
    private static final float PI_H        = VineMath.PIF / 2;

    private static final float ATAN2_S[]   = new float[SIZE_AR];
    private static final float ATAN2_PM[]  = new float[SIZE_AR];
    private static final float ATAN2_MP[]  = new float[SIZE_AR];
    private static final float ATAN2_MM[]  = new float[SIZE_AR];

    private static final float ATAN2_R[]   = new float[SIZE_AR];
    private static final float ATAN2_RPM[] = new float[SIZE_AR];
    private static final float ATAN2_RMP[] = new float[SIZE_AR];
    private static final float ATAN2_RMM[] = new float[SIZE_AR];

    static
    {
        for (int i = 0; i <= SIZE_ACC; i++)
        {
            final double d = (double) i / SIZE_ACC;
            final double y = d;
            final float v = (float) Math.atan2(y, 1);
            ATAN2_S[i] = v;
            ATAN2_PM[i] = VineMath.PIF - v;
            ATAN2_MP[i] = -v;
            ATAN2_MM[i] = -VineMath.PIF + v;

            ATAN2_R[i] = PI_H - v;
            ATAN2_RPM[i] = PI_H + v;
            ATAN2_RMP[i] = -PI_H + v;
            ATAN2_RMM[i] = -PI_H - v;
        }
    }

    private Icecore()
    {
        // Utility class
    }

    public static float atan2(final float y, final float x)
    {
        if (y < 0)
        {
            if (x < 0)
            {
                if (y < x)
                {
                    return ATAN2_RMM[(int) (x / y * SIZE_ACC)];
                } else
                {
                    return ATAN2_MM[(int) (y / x * SIZE_ACC)];
                }
            } else
            {
                if (y > x)
                {
                    return ATAN2_RMP[(int) (x / -y * SIZE_ACC)];
                } else
                {
                    return ATAN2_MP[(int) (-y / x * SIZE_ACC)];
                }
            }
        } else
        {
            if (x < 0)
            {
                if (y > -x)
                {
                    return ATAN2_RPM[(int) (-x / y * SIZE_ACC)];
                } else
                {
                    return ATAN2_PM[(int) (y / -x * SIZE_ACC)];
                }
            } else
            {
                if (y > x)
                {
                    return ATAN2_R[(int) (x / y * SIZE_ACC)];
                } else
                {
                    return ATAN2_S[(int) (y / x * SIZE_ACC)];
                }
            }
        }
    }
}