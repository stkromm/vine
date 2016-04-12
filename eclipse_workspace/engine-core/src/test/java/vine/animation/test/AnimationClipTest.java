package vine.animation.test;

import org.junit.Assert;
import org.junit.Test;

import vine.animation.AnimationClip;
import vine.animation.AnimationFrame;
import vine.animation.AnimationState;
import vine.animation.AnimationStateManager;

public class AnimationClipTest {

    @Test
    public static void getFrameTest() {
        final AnimationFrame frame1 = new AnimationFrame(new float[5], 3);
        final AnimationFrame frame2 = new AnimationFrame(new float[5], 5);
        final AnimationFrame frame3 = new AnimationFrame(new float[5], 6);
        final AnimationFrame frame4 = new AnimationFrame(new float[5], 8);
        final AnimationClip clip = new AnimationClip(null, null, frame1, frame2, frame3, frame4);
        Assert.assertEquals(clip.getFrame(3.5f), frame2);
        Assert.assertEquals(clip.getFrame(0), frame1);
        Assert.assertEquals(clip.getFrame(8), frame4);
        Assert.assertEquals(clip.getFrame(-1), frame1);
    }

    @Test
    public static void testPlaybackSpeed() {
        final AnimationFrame frame1 = new AnimationFrame(new float[5], 3);
        final AnimationFrame frame2 = new AnimationFrame(new float[5], 5);
        final AnimationFrame frame3 = new AnimationFrame(new float[5], 6);
        final AnimationFrame frame4 = new AnimationFrame(new float[5], 8);
        final AnimationClip clip = new AnimationClip(null, frame1, frame2, frame3, frame4);
        final AnimationState state = new AnimationState(clip, "TEST", 2);
        final AnimationStateManager manager = new AnimationStateManager(new AnimationState[] { state });
        manager.changeState("TEST");
        Assert.assertEquals(manager.getCurrentState(), state);
        manager.tick(5);
        Assert.assertEquals(manager.getCurrentFrame(), frame1.getUvs());
        state.setPlaybackSpeed(1);
        Assert.assertEquals(manager.getCurrentFrame(), frame1.getUvs());
    }
}
