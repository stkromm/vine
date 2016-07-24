package vine.animation;

import java.util.HashMap;
import java.util.Map;

public class AnimationStateManager
{
    private final Map<String, AnimationState> states;
    private AnimationState                    currentState;
    private final long                        startTime = System.currentTimeMillis();
    private long                              stoppedTime;

    public AnimationStateManager(final AnimationState[] animation)
    {
        if (animation.length > 0)
        {
            this.currentState = animation[animation.length - 1];
        }
        this.states = new HashMap<>();
        for (final AnimationState state : animation)
        {
            this.states.put(state.getName(), state);
        }
    }

    public final void start()
    {
        this.stoppedTime = 0;
    }

    public final void stop()
    {
        this.stoppedTime = System.currentTimeMillis();
    }

    public final void changeState(final String name)
    {
        if (!this.currentState.getName().equals(name) && this.states.containsKey(name))
        {
            this.currentState = this.states.get(name);
        }
    }

    public final float[] getCurrentFrame()
    {
        return this.currentState.getClip().getFrame((this.getCurrentTime() - this.startTime)
                * this.currentState.getPlaybackSpeed() % this.currentState.getClip().getDuration()).getFramedValues();
    }

    private long getCurrentTime()
    {
        if (this.stoppedTime > 0)
        {
            return this.stoppedTime;
        } else
        {
            return System.currentTimeMillis();
        }
    }

    public final AnimationState getCurrentState()
    {
        return this.currentState;
    }
}
