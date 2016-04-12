package vine.animation;

public class AnimationClip {
    private final AnimationFrame[] frames;
    private final float duration;

    public AnimationClip(final AnimationFrame... frames) {
        super();
        this.frames = frames;
        this.duration = frames[frames.length - 1].getDuration();
    }

    /**
     * @param time
     * @return
     */
    public final AnimationFrame getFrame(final float time) {
        int frameId = 0;
        while (frameId < this.frames.length - 1 && this.frames[frameId].getDuration() < time) {
            ++frameId;
        }
        return this.frames[frameId];
    }

    /**
     * @return
     */
    public final float getDuration() {
        return this.duration;
    }

}
