package vine.util.time;

import vine.util.time.TimerManager.FinishCallback;

public class ExecutionTimer implements Timer
{
    private boolean              running;
    private final float          id;
    private final float          duration;
    private int                  loops;
    private final FinishCallback execute;
    private float                elapsedTime;

    public ExecutionTimer(final float duration, final int loops, final FinishCallback execute, final float id)
    {
        this.duration = duration;
        this.loops = loops;
        this.execute = execute;
        this.id = id;
    }

    @Override
    public void start()
    {
        this.running = true;
    }

    @Override
    public void reset()
    {
        this.elapsedTime = 0;
    }

    @Override
    public void stop()
    {
        this.running = false;
        this.elapsedTime = 0;
    }

    @Override
    public float getElapsedTime()
    {
        return this.elapsedTime;
    }

    @Override
    public float getDuration()
    {
        return this.duration;
    }

    @Override
    public void tick(final float passedTime)
    {
        this.elapsedTime += passedTime;
        if (this.elapsedTime >= this.duration)
        {
            this.execute.invoke();
            if (this.loops > 0)
            {
                this.loops--;
            }
        }
        if (this.loops > 0 || this.loops == -1)
        {
            this.elapsedTime %= this.duration;
        } else
        {
            this.running = false;
        }
    }

    @Override
    public boolean isRunning()
    {
        return this.running;
    }

    @Override
    public float getId()
    {
        return this.id;
    }
}
