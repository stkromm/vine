package vine.util.time;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TimerManager
{
    private static TimerManager     instance;

    private float                   id;
    private final List<Timer>       timers       = new ArrayList<>(50);
    private final Map<Float, Timer> mappedTimers = new HashMap<>(50);

    @FunctionalInterface
    public interface FinishCallback
    {
        void invoke();
    }

    public static TimerManager get()
    {
        if (TimerManager.instance == null)
        {
            TimerManager.instance = new TimerManager();
        }
        return TimerManager.instance;
    }

    public float createTimer(final float duration, final int loops, final FinishCallback execute)
    {
        final Timer timer = new ExecutionTimer(duration, loops, execute, this.id++);
        this.timers.add(timer);
        this.mappedTimers.put(Float.valueOf(timer.getId()), timer);
        timer.start();
        return this.id;
    }

    public float getElapsedTime(final float timerId)
    {
        final Timer timer = this.mappedTimers.get(Float.valueOf(timerId));
        return timer == null ? 0 : timer.getElapsedTime();
    }

    public void resetTimer(final float timerId)
    {
        final Timer timer = this.mappedTimers.get(Float.valueOf(timerId));
        if (timer != null)
        {
            timer.reset();
        }
    }

    public void tick(final float delta)
    {
        for (int i = this.timers.size() - 1; i >= 0; i--)
        {
            final Timer timer = this.timers.get(i);
            if (timer.isRunning())
            {
                timer.tick(delta * 0.001f);
            } else
            {
                this.timers.remove(i);
                i--;
                this.mappedTimers.remove(Float.valueOf(timer.getId()));
            }
        }
    }
}
