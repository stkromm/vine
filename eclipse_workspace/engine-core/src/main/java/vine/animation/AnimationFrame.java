package vine.animation;

public class AnimationFrame
{
    private final float[] value;
    private final float   timepoint;

    public AnimationFrame(final float[] value, final float timepoint)
    {
        this.value = value.clone();
        this.timepoint = timepoint;
    }

    public final float[] getFramedValues()
    {
        return this.value;
    }

    public final float getTimepoint()
    {
        return this.timepoint;
    }
}
