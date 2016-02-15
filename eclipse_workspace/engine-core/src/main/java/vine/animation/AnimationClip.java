package vine.animation;

import vine.graphics.Texture2D;

public class AnimationClip {
    private final AnimationFrame[] frames;
    private final Texture2D texture;
    private final float duration;

    public AnimationClip(final Texture2D texture, final AnimationFrame... frames) {
        super();
        this.frames = frames;
        this.texture = texture;
        duration = frames[frames.length - 1].getDuration();
    }

    /**
     * @param time
     * @return
     */
    public final AnimationFrame getFrame(final float time) {
        int frameId = 0;
        while (frames[frameId].getDuration() < time) {
            ++frameId;
        }
        return frames[frameId];
    }

    /**
     * @return
     */
    public final float getDuration() {
        return duration;
    }

    /**
     * @return
     */
    public final Texture2D getTexture() {
        return texture;
    }
}
