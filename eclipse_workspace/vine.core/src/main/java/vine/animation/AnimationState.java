package vine.animation;

public class AnimationState
{
    private final String        name;
    private final AnimationClip clip;
    private float               playbackSpeed;

    public AnimationState(final AnimationClip clip, final String name, final float playbackSpeed)
    {
        this.name = name;
        this.clip = clip;
        this.playbackSpeed = playbackSpeed;
    }

    public final String getName()
    {
        return this.name;
    }

    public final AnimationClip getClip()
    {
        return this.clip;
    }

    public final float getPlaybackSpeed()
    {
        return this.playbackSpeed;
    }

    public final void setPlaybackSpeed(final float playbackSpeed)
    {
        this.playbackSpeed = playbackSpeed;
    }
}
