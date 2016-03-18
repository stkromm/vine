package vine.application;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vine.game.Game;
import vine.graphics.GraphicsProvider;
import vine.input.Input;
import vine.window.Window;

/**
 * @author Steffen
 *
 */
public class GameLifecycle implements ActivityLifecycle {
    /**
     * 
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GameLifecycle.class);
    private final long MAX_FRAME_DURATION = 16L * (long) 1e6;
    /**
     * 
     */
    private volatile boolean running = true;
    private volatile boolean idle = false;
    private final CyclicBarrier barrier;
    private final List<Thread> workThreads = new ArrayList<>(2);
    private static Game game;
    private final Window window;
    private final Input input;

    public static Game getRunningGame() {
        return game;
    }

    /**
     * @param application
     *            The application, that contains the game
     * @param window
     * @param input
     */
    public GameLifecycle(final Window window, final Input input, final Game game) {
        this.window = window;
        this.input = input;
        GameLifecycle.game = game;
        game.changeLevel("default-level");
        if (Runtime.getRuntime().availableProcessors() > 2) {
            barrier = new CyclicBarrier(2);
        } else {
            barrier = new CyclicBarrier(1);
        }
    }

    @Override
    public synchronized void stop() {
        running = false;
    }

    @Override
    public synchronized void pause() {
        idle = true;
    }

    @Override
    public synchronized void resume() {
        idle = false;
    }

    /**
     * Executes the game loop.
     */
    @Override
    public synchronized void start() {
        workThreads.forEach(thread -> thread.start());
        while (running) {
            input.pollEvents();
            if (window.requestedClose()) {
                running = false;
            }
            waitTick(MAX_FRAME_DURATION);
        }
    }

    private final static void waitBarrier(final CyclicBarrier barrier) {
        try {
            barrier.await();
        } catch (InterruptedException e) {
            LOGGER.info("Interrupted CyclicBarrier in the Gameloop", e);
        } catch (BrokenBarrierException e) {
            LOGGER.info("Broke CyclicBarrier in the Gameloop", e);
        }
    }

    /**
     * @param sleepTime
     *            The time, the thread should sleep. It is not guaranteed the
     *            thread will sleep the given time.
     */
    private final static void waitTick(final long sleepTime) {
        if (sleepTime > 1000) {
            try {
                TimeUnit.NANOSECONDS.sleep(sleepTime);
            } catch (InterruptedException e) {
                LOGGER.info("Thread waiting for Tick end interrupted", e);
            }
        }
    }

    @Override
    public synchronized void create() {
        if (Runtime.getRuntime().availableProcessors() > 2) {
            Thread logic = new Thread(() -> {
                Thread.currentThread().setName("logic");
                long curTime = System.nanoTime() - 16;
                while (running) {
                    if (!idle) {
                        game.update((System.nanoTime() - curTime) / (float) 1e6);
                        curTime = System.nanoTime();
                    }
                    waitTick(MAX_FRAME_DURATION - System.nanoTime() +
                     curTime);
                    waitBarrier(barrier);
                }
            });
            workThreads.add(logic);
            GraphicsProvider.getGraphics().makeContext(0L);
            Thread rendering = new Thread(() -> {
                Thread.currentThread().setName("render");
                GraphicsProvider.getGraphics().makeContext(window.getContext());
                GraphicsProvider.getGraphics().init();
                while (running) {
                    if (!idle) {
                        StatMonitor.newFrame();
                        game.render();
                    }
                    waitBarrier(barrier);
                }
            });
            workThreads.add(rendering);
        } else {
            GraphicsProvider.getGraphics().makeContext(0L);
            Thread rendering = new Thread(() -> {
                Thread.currentThread().setName("render");
                GraphicsProvider.getGraphics().makeContext(window.getContext());
                GraphicsProvider.getGraphics().init();
                long curTime = System.nanoTime() - 16;
                while (running) {
                    if (!idle) {
                        game.update((System.nanoTime() - curTime) / (float) 1e6);
                        curTime = System.nanoTime();
                        StatMonitor.newFrame();
                        game.render();
                    }
                    waitTick(MAX_FRAME_DURATION - System.nanoTime() + curTime);
                    waitBarrier(barrier);
                }
            });
            workThreads.add(rendering);
        }
    }

    @Override
    public synchronized void destroy() {
        stop();
        workThreads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                LOGGER.debug("Interrupted worker thread by joining them at start end", e);
            }
        });
    }
}
