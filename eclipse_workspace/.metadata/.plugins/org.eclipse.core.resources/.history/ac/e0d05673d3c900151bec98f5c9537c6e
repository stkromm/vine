package vine.animation;

import vine.graphics.Texture2D;

public class AnimationClip {
    private final AnimationFrame[] frames;
    private final Texture2D texture;
    private final float duration;

    public AnimationClip(Texture2D texture, AnimationFrame... frames) {
        super();
        this.frames = frames;
        this.texture = texture;
        duration = frames[frames.length - 1].getDuration();
    }

    public final AnimationFrame getFrame(final float time) {
        int frameId = 0;
        while (frames[frameId].getDuration() < time) {
            ++frameId;
        }
        return frames[frameId];
    }

    public final float getDuration() {
        return duration;
    }

    public final Texture2D getTexture() {
        return texture;
    }
}
