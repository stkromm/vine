package vine.animation;

public class AnimationFrame {
    private final float[] value;
    private final float duration;

    public AnimationFrame(final float[] value, final float duration) {
        this.value = value;
        this.duration = duration;
    }

    public final float[] getUvs() {
        return this.value;
    }

    public final float getDuration() {
        return this.duration;
    }
}
