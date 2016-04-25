package vine.application;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import vine.game.World;
import vine.graphics.GraphicsProvider;
import vine.graphics.RenderStack;
import vine.input.Input;
import vine.util.Log;
import vine.util.time.Stopwatch;
import vine.util.time.TimerManager;
import vine.window.Window;

/**
 * @author Steffen
 *
 */
public class Engine implements EngineLifecycle
{
    private static final long  MAX_UPDATE_DURATION = 16L * (long) 1e6;
    private static final long  MAX_FRAME_DURATION  = 33L * (long) 1e6;

    private final World        world;
    private final Input        input;

    private volatile boolean   running             = true;
    private volatile boolean   idle;

    private final RenderStack  renderStack;
    private final List<Thread> workThreads         = new ArrayList<>(2);
    private final Window       window;

    /**
     * @param application
     *            The application, that contains the game
     * @param window
     * @param input
     * @param game
     */
    public Engine(final Window window, final Input input, final World world, final RenderStack renderStack)
    {
        this.window = window;
        this.input = input;
        this.world = world;
        this.renderStack = renderStack;
        PerformanceMonitor.logGargabeCollector();
    }

    public Window getWindow()
    {
        return this.window;
    }

    public Input getInput()
    {
        return this.input;
    }

    @Override
    public final synchronized void stop()
    {
        this.running = false;
    }

    @Override
    public final synchronized void pause()
    {
        this.idle = true;
    }

    @Override
    public final synchronized void resume()
    {
        this.idle = false;
    }

    @Override
    public final synchronized void start()
    {
        this.workThreads.forEach(thread -> thread.start());
        while (this.running)
        {
            this.input.poll();
            if (this.window.requestedClose())
            {
                this.running = false;
            }
            Engine.waitTick(Engine.MAX_UPDATE_DURATION);
        }
    }

    @Override
    public synchronized void create()
    {
        this.world.changeLevel("default-level");
        GraphicsProvider.getGraphics().makeContext(0L);
        if (RuntimeInfo.getProcessorCoreCount() > 3)
        {
            this.workThreads.add(this.createLogicThread());
            this.workThreads.add(this.createRenderThread());
        } else
        {
            this.workThreads.add(this.createSingleThreadExecution());
        }

    }

    @Override
    public synchronized void destroy()
    {
        this.stop();
        this.workThreads.forEach(thread ->
        {
            try
            {
                thread.join();
            } catch (final InterruptedException e)
            {
                Log.exception("Interrupted worker thread by joining them at start end", e);
            }
        });
    }

    private final static void waitTick(final long sleepTime)
    {
        if (sleepTime > 1000)
        {
            try
            {
                TimeUnit.NANOSECONDS.sleep(sleepTime);
            } catch (final InterruptedException e)
            {
                Log.exception("Thread waiting for Tick end interrupted", e);
            }
        }
    }

    private final Thread createLogicThread()
    {
        return new Thread(() ->
        {
            Thread.currentThread().setName(RuntimeInfo.LOGIC_THREAD_NAME);
            final Stopwatch stopwatch = new Stopwatch(true);
            while (this.running)
            {
                if (!this.idle)
                {
                    final float delta = stopwatch.stop() / 1000000f;
                    TimerManager.get().tick(delta);
                    this.world.update(delta);
                }
                Engine.waitTick(Engine.MAX_UPDATE_DURATION - stopwatch.layover());
            }
        });
    }

    private final Thread createRenderThread()
    {
        return new Thread(() ->
        {
            final Stopwatch stopwatch = new Stopwatch(true);
            Thread.currentThread().setName(RuntimeInfo.RENDER_THREAD_NAME);
            this.renderStack.init();
            stopwatch.stop();
            while (this.running)
            {
                PerformanceMonitor.startFrame();
                if (!this.idle)
                {
                    this.renderStack.render();
                }
                PerformanceMonitor.endFrame();
                Engine.waitTick(Engine.MAX_FRAME_DURATION - stopwatch.stop());
            }
        });
    }

    private final Thread createSingleThreadExecution()
    {
        return new Thread(() ->
        {
            Thread.currentThread().setName(RuntimeInfo.RENDER_THREAD_NAME);
            this.renderStack.init();
            final Stopwatch stopwatch = new Stopwatch(true);
            while (this.running)
            {
                if (!this.idle)
                {
                    final long delta = stopwatch.stop();
                    TimerManager.get().tick(delta / (float) 1e6);
                    this.world.update(delta / (float) 1e6);
                    PerformanceMonitor.endFrame();
                    this.renderStack.render();
                }
                Engine.waitTick(Engine.MAX_UPDATE_DURATION - stopwatch.layover());
            }
        });
    }
}
