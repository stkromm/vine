package vine.time;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TimerManager {
    @FunctionalInterface
    public interface FinishCallback {
        public void invoke();
    }

    float id = 0;
    private final List<Timer> timers = new ArrayList<>(50);
    private final Map<Float, Timer> mappedTimers = new HashMap<>(50);

    private static TimerManager instance;

    public final static TimerManager get() {
        if (TimerManager.instance == null) {
            TimerManager.instance = new TimerManager();
        }
        return TimerManager.instance;
    }

    public final float createTimer(final float duration, final int loops, final FinishCallback execute) {
        final Timer timer = new SecondTimer(duration, loops, execute, this.id++);
        this.timers.add(timer);
        this.mappedTimers.put(Float.valueOf(timer.getId()), timer);
        timer.start();
        return this.id;
    }

    public final float getElapsedTime(final float timerId) {
        final Timer timer = this.mappedTimers.get(Float.valueOf(timerId));
        return timer != null ? timer.getElapsedTime() : 0;
    }

    public final void resetTimer(final float timerId) {
        final Timer timer = this.mappedTimers.get(Float.valueOf(timerId));
        if (timer != null) {
            timer.reset();
        }
    }

    public final void tick(final float delta) {
        for (int i = this.timers.size() - 1; i >= 0; i--) {
            final Timer timer = this.timers.get(i);
            if (timer.isRunning()) {
                timer.tick(delta / 1000f);
            } else {
                this.timers.remove(i);
                i--;
                this.mappedTimers.remove(Float.valueOf(timer.getId()));
            }
        }
    }
}
