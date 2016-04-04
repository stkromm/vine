package vine.animation.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import vine.animation.AnimationClip;
import vine.animation.AnimationFrame;
import vine.animation.AnimationState;
import vine.animation.AnimationStateManager;
import vine.graphics.SpriteRenderer;

public class AnimationClipTest {

    @Test
    public void getFrameTest() {
        AnimationFrame frame1 = new AnimationFrame(new float[5], 3);
        AnimationFrame frame2 = new AnimationFrame(new float[5], 5);
        AnimationFrame frame3 = new AnimationFrame(new float[5], 6);
        AnimationFrame frame4 = new AnimationFrame(new float[5], 8);
        AnimationClip clip = new AnimationClip(null, frame1, frame2, frame3, frame4);
        assertEquals(clip.getFrame(3.5f), frame2);
        assertEquals(clip.getFrame(0), frame1);
        assertEquals(clip.getFrame(8), frame4);
        assertEquals(clip.getFrame(-1), frame1);
        assertEquals(null, clip.getTexture());
    }

    @Test
    public void testPlaybackSpeed() {
        AnimationFrame frame1 = new AnimationFrame(new float[5], 3);
        AnimationFrame frame2 = new AnimationFrame(new float[5], 5);
        AnimationFrame frame3 = new AnimationFrame(new float[5], 6);
        AnimationFrame frame4 = new AnimationFrame(new float[5], 8);
        AnimationClip clip = new AnimationClip(null, frame1, frame2, frame3, frame4);
        AnimationState state = new AnimationState(clip, "TEST", 2);
        AnimationStateManager manager = new AnimationStateManager(new AnimationState[] { state });
        manager.changeState("TEST");
        assertEquals(manager.getCurrentState(), state);
        manager.tick(5);
        assertEquals(manager.getCurrentFrame(), frame1.getUvs());
        state.setPlaybackSpeed(1);
        assertEquals(manager.getCurrentFrame(), frame1.getUvs());
    }
}
