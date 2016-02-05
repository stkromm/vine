package vine.animation;

public class AnimationFrame {
    private final float[] uvs;
    private final float duration;

    public AnimationFrame(float[] uvs, float duration) {
        this.uvs = uvs;
        this.duration = duration;
    }

    public final float[] getUvs() {
        return uvs;
    }

    public final float getDuration() {
        return duration;
    }
}
