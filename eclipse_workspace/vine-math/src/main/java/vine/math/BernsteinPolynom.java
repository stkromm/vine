package vine.math;

public final class BernsteinPolynom
{

    final double coeffi;
    final int    grad;
    final int    i;

    public BernsteinPolynom(final int grad, final int i)
    {
        this.grad = grad;
        this.i = i;
        coeffi = VineMath.binominalCoefficient(grad, i);
    }

    public float evaluate(final float u)
    {
        float uPowI = u;
        for (int a = grad - i; a > 0; a--)
        {
            uPowI *= u;
        }
        final float oneMinusU = 1 - u;
        float uPowNI = oneMinusU;
        for (int a = i; a > 0; a--)
        {
            uPowNI *= oneMinusU;
        }

        return (float) (coeffi * uPowI * uPowNI);
    }
}
