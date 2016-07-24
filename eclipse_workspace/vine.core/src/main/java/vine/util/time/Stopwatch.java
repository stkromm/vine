package vine.util.time;

public class Stopwatch
{
    long lastStartedTime;

    public Stopwatch(final boolean autostarted)
    {
        if (autostarted)
        {
            this.start();
        }
    }

    public Stopwatch(final long startTime)
    {
        this.lastStartedTime = startTime;
    }

    /**
     * Starts or resets a running stopwatch. The stopwatch then measures passed
     * time from the moment this method was called.
     */
    public final void start()
    {
        this.lastStartedTime = System.nanoTime();
    }

    /**
     * 
     * The time passed since the last start in nanoseconds. The stopwatch
     * doesn't stop and will used the time it was last started to measure the
     * passed time.
     */
    public long layover()
    {
        return System.nanoTime() - this.lastStartedTime;
    }

    /**
     * 
     * Stops the stopwatch and returns the time that has passed since it was
     * last started in nanoseconds. Resets the stopwatch and sets it to the
     * current time.
     */
    public long stop()
    {
        final long stoppedTime = System.nanoTime() - this.lastStartedTime;
        this.lastStartedTime = System.nanoTime();
        return stoppedTime;
    }
}
