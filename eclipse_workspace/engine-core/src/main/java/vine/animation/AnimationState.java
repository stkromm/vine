package vine.animation;

public class AnimationState {
    private final String name;
    private final AnimationClip clip;
    private float playbackSpeed;

    public AnimationState(AnimationClip clip, String name, float playbackSpeed) {
        this.name = name;
        this.clip = clip;
        this.playbackSpeed = playbackSpeed;
    }

    public final String getName() {
        return name;
    }

    public final AnimationClip getClip() {
        return clip;
    }

    public final float getPlaybackSpeed() {
        return playbackSpeed;
    }

    public final void setPlaybackSpeed(final float playbackSpeed) {
        this.playbackSpeed = playbackSpeed;
    }
}
