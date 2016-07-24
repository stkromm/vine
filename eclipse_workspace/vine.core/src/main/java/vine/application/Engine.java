package vine.application;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import vine.device.input.Input;
import vine.game.World;
import vine.graphics.GraphicsProvider;
import vine.graphics.RenderStack;
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
    private static final long  MAX_UPDATE_DURATION  = 16L * (long) 1e6;
    private static final long  MAX_PHYSICS_DURATION = (long) (1000f / 3000f * 1e6);
    private static final long  MAX_FRAME_DURATION   = (long) (1000f / 3000f * 1e6);

    private final World        world;
    private final Input        input;

    private volatile boolean   running              = true;
    private volatile boolean   idle;

    private final RenderStack  renderStack;
    private final List<Thread> workThreads          = new ArrayList<>(2);
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
        return window;
    }

    public Input getInput()
    {
        return input;
    }

    @Override
    public final synchronized void stop()
    {
        running = false;
    }

    @Override
    public final synchronized void pause()
    {
        idle = true;
    }

    @Override
    public final synchronized void resume()
    {
        idle = false;
    }

    @Override
    public final synchronized void start()
    {
        workThreads.forEach(thread -> thread.start());
        while (running)
        {
            input.poll();
            if (window.requestedClose())
            {
                running = false;
            }
            Engine.waitTick(Engine.MAX_UPDATE_DURATION);
        }
    }

    @Override
    public synchronized void create()
    {
        world.changeLevel("default-level");
        GraphicsProvider.getGraphics().makeContext(0L);
        if (RuntimeInfo.getProcessorCoreCount() > 2)
        {
            workThreads.add(createLogicThread());
            workThreads.add(createRenderThread());
            workThreads.add(createPhysicThread());
        } else
        {
            workThreads.add(createSingleThreadExecution());
        }

    }

    @Override
    public synchronized void destroy()
    {
        stop();
        workThreads.forEach(thread ->
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
        try
        {
            TimeUnit.NANOSECONDS.sleep(sleepTime);
        } catch (final InterruptedException e)
        {
            Log.exception("Thread waiting for Tick end interrupted", e);
        }
    }

    private final Thread createLogicThread()
    {
        return new Thread(() ->
        {
            Thread.currentThread().setName(RuntimeInfo.LOGIC_THREAD_NAME);
            final Stopwatch stopwatch = new Stopwatch(true);
            while (running)
            {
                if (!idle)
                {
                    final float delta = stopwatch.stop() / 1000000f;
                    TimerManager.get().tick(delta);
                    world.update(delta);
                }
                Engine.waitTick(Engine.MAX_UPDATE_DURATION - stopwatch.layover());
            }
        });
    }

    private final Thread createPhysicThread()
    {
        return new Thread(() ->
        {
            Thread.currentThread().setName(RuntimeInfo.PHYSIC_THREAD_NAME);
            final Stopwatch stopwatch = new Stopwatch(true);
            while (running)
            {
                if (!idle)
                {
                    final float delta = stopwatch.stop() / 1000000f;
                    world.simulatePhysics(delta);
                }
                Engine.waitTick(Engine.MAX_PHYSICS_DURATION - stopwatch.layover());
            }
        });
    }

    private final Thread createRenderThread()
    {
        return new Thread(() ->
        {
            final Stopwatch stopwatch = new Stopwatch(true);
            Thread.currentThread().setName(RuntimeInfo.RENDER_THREAD_NAME);
            renderStack.init();
            while (running)
            {
                PerformanceMonitor.startFrame();
                stopwatch.stop();
                if (!idle)
                {
                    renderStack.render();
                }
                // Engine.waitTick(Engine.MAX_FRAME_DURATION -
                // stopwatch.layover());
                PerformanceMonitor.endFrame();
            }
        });
    }

    private final Thread createSingleThreadExecution()
    {
        return new Thread(() ->
        {
            Thread.currentThread().setName(RuntimeInfo.RENDER_THREAD_NAME);
            renderStack.init();
            final Stopwatch stopwatch = new Stopwatch(true);
            while (running)
            {
                PerformanceMonitor.startFrame();
                if (!idle)
                {
                    final long delta = stopwatch.stop();
                    TimerManager.get().tick(delta / (float) 1e6);
                    world.update(delta / (float) 1e6);
                    renderStack.render();
                }
                Engine.waitTick(Engine.MAX_UPDATE_DURATION - stopwatch.layover());
                PerformanceMonitor.endFrame();
            }
        });
    }
}
