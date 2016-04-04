package vine.animation;

import java.util.HashMap;
import java.util.Map;

public class AnimationStateManager {
    private final Map<String, AnimationState> states;
    private AnimationState currentState;
    private float time;

    public AnimationStateManager(final AnimationState[] animation) {
        if (animation.length > 0) {
            this.currentState = animation[animation.length - 1];
        }

        this.states = new HashMap<>();
        for (final AnimationState state : animation) {
            this.states.put(state.getName(), state);
        }
    }

    public final void changeState(final String name) {
        if (this.states.containsKey(name)) {
            this.currentState = this.states.get(name);
        }
    }

    public final void tick(final float delta) {
        this.time += delta * this.currentState.getPlaybackSpeed();
        this.time = this.time % this.currentState.getClip().getDuration();
    }

    public final float[] getCurrentFrame() {
        return this.currentState.getClip().getFrame(this.time).getUvs();
    }

    public final AnimationState getCurrentState() {
        return this.currentState;
    }
}
