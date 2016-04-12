package vine.application;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vine.game.World;
import vine.graphics.GraphicsProvider;
import vine.input.Input;
import vine.time.Stopwatch;
import vine.time.TimerManager;
import vine.window.Window;

/**
 * @author Steffen
 *
 */
public class Engine implements EngineLifecycle {
    /**
     * 
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Engine.class);
    private static final long MAX_FRAME_DURATION = 16L * (long) 1e6;

    private static World game;
    private static Input input;

    private volatile boolean running = true;
    private volatile boolean idle = false;
    private boolean resizeViewport;

    private final CyclicBarrier barrier;
    private final List<Thread> workThreads = new ArrayList<>(2);
    private final Window window;

    /**
     * @param application
     *            The application, that contains the game
     * @param window
     * @param input
     * @param game
     */
    public Engine(final Window window, final Input input, final World game) {
        this.window = window;
        Engine.input = input;
        Engine.game = game;
        game.changeLevel("default-level");
        if (Runtime.getRuntime().availableProcessors() > 3) {
            this.barrier = new CyclicBarrier(3);
        } else {
            this.barrier = new CyclicBarrier(2);
        }
    }

    @Override
    public final synchronized void stop() {
        this.running = false;
    }

    @Override
    public final synchronized void pause() {
        this.idle = true;
    }

    @Override
    public final synchronized void resume() {
        this.idle = false;
    }

    @Override
    public final synchronized void start() {
        this.workThreads.forEach(thread -> thread.start());
        while (this.running) {
            Engine.input.poll();
            if (this.window.requestedClose()) {
                this.running = false;
            }
            // GamePlayer.waitTick(GamePlayer.MAX_FRAME_DURATION);
            Engine.waitBarrier(this.barrier);
        }
    }

    @Override
    public synchronized void create() {
        GraphicsProvider.getGraphics().makeContext(0L);
        if (RuntimeInfo.getProcessorCoreCount() > 3) {
            this.workThreads.add(this.createLogicThread());
            this.workThreads.add(this.createRenderThread());
        } else {
            this.workThreads.add(this.createSingleThreadExecution());
        }
    }

    @Override
    public synchronized void destroy() {
        this.stop();
        this.workThreads.forEach(thread -> {
            try {
                thread.join();
            } catch (final InterruptedException e) {
                Engine.LOGGER.debug("Interrupted worker thread by joining them at start end", e);
            }
        });
    }

    private final static void waitBarrier(final CyclicBarrier barrier) {
        try {
            barrier.await();
        } catch (final InterruptedException e) {
            Engine.LOGGER.info("Interrupted CyclicBarrier in the Gameloop", e);
        } catch (final BrokenBarrierException e) {
            Engine.LOGGER.info("Broke CyclicBarrier in the Gameloop", e);
        }
    }

    private final static void waitTick(final long sleepTime) {
        if (sleepTime > 1000) {
            try {
                TimeUnit.NANOSECONDS.sleep(sleepTime);
            } catch (final InterruptedException e) {
                Engine.LOGGER.info("Thread waiting for Tick end interrupted", e);
            }
        }
    }

    private final Thread createLogicThread() {
        return new Thread(() -> {
            Thread.currentThread().setName("logic");
            final Stopwatch stopwatch = new Stopwatch(true);
            while (this.running) {
                if (!this.idle) {
                    final float delta = stopwatch.stop() / 1000000f;
                    TimerManager.get().tick(delta);
                    Engine.game.update(delta);
                }
                // Engine.waitTick(Engine.MAX_FRAME_DURATION -
                // stopwatch.layover());
                Engine.waitBarrier(this.barrier);
            }
        });
    }

    private final Thread createRenderThread() {
        return new Thread(() -> {
            Thread.currentThread().setName("render");
            GraphicsProvider.getGraphics().makeContext(this.window.getContext());
            GraphicsProvider.getGraphics().init();
            this.window.setWindowContextCallback(context -> {
                GraphicsProvider.getGraphics().makeContext(context);
                GraphicsProvider.getGraphics().init();
                Engine.input.listenToWindow(context);
            });
            this.window.setSizeCallback((w, h) -> {
                this.window.setWindowSize(w, h);
                this.resizeViewport = true;
            });
            while (this.running) {
                if (this.resizeViewport) {
                    GraphicsProvider.getGraphics().setViewport(Engine.game.getScreen().getViewport().getLeftOffset(),
                            Engine.game.getScreen().getViewport().getTopOffset(),
                            this.window.getWidth() - Engine.game.getScreen().getViewport().getRightOffset(),
                            this.window.getHeight() - Engine.game.getScreen().getViewport().getBottomOffset());
                }
                if (!this.idle) {
                    PerformanceMonitor.captureFrame();
                    Engine.game.render();
                }
                Engine.waitBarrier(this.barrier);
            }
        });
    }

    private final Thread createSingleThreadExecution() {
        return new Thread(() -> {
            Thread.currentThread().setName("render");
            GraphicsProvider.getGraphics().makeContext(this.window.getContext());
            GraphicsProvider.getGraphics().init();
            this.window.setSizeCallback((w, h) -> {
                this.window.setWindowSize(w, h);
            });
            this.window.setWindowContextCallback(context -> {
                GraphicsProvider.getGraphics().makeContext(context);
                GraphicsProvider.getGraphics().init();
                Engine.input.listenToWindow(context);
            });
            final Stopwatch stopwatch = new Stopwatch(true);
            while (this.running) {
                if (this.resizeViewport) {
                    GraphicsProvider.getGraphics().setViewport(Engine.game.getScreen().getViewport().getLeftOffset(),
                            Engine.game.getScreen().getViewport().getTopOffset(),
                            this.window.getWidth() - Engine.game.getScreen().getViewport().getRightOffset(),
                            this.window.getHeight() - Engine.game.getScreen().getViewport().getBottomOffset());
                }
                if (!this.idle) {
                    final long delta = stopwatch.stop();
                    TimerManager.get().tick(delta / (float) 1e6);
                    Engine.game.update(delta / (float) 1e6);
                    PerformanceMonitor.captureFrame();
                    Engine.game.render();
                }
                Engine.waitTick(Engine.MAX_FRAME_DURATION - stopwatch.layover());
                Engine.waitBarrier(this.barrier);
            }
        });
    }
}
