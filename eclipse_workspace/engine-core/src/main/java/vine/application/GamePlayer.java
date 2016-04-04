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
public class GamePlayer implements ActivityLifecycle {
    /**
     * 
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GamePlayer.class);
    private final long MAX_FRAME_DURATION = 16L * (long) 1e6;

    private static Game game;
    /**
     * 
     */
    private volatile boolean running = true;
    private volatile boolean idle = false;
    private volatile boolean resizeViewport;

    private final CyclicBarrier barrier;
    private final List<Thread> workThreads = new ArrayList<>(2);
    private final Window window;
    private final Input input;

    /**
     * @return
     */
    public static Game getRunningGame() {
        return game;
    }

    /**
     * @param application
     *            The application, that contains the game
     * @param window
     * @param input
     * @param game
     */
    public GamePlayer(final Window window, final Input input, final Game game) {
        this.window = window;
        this.input = input;
        GamePlayer.game = game;
        game.changeLevel("default-level");
        if (Runtime.getRuntime().availableProcessors() > 2) {
            this.barrier = new CyclicBarrier(2);
        } else {
            this.barrier = new CyclicBarrier(1);
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

    /**
     * Executes the game loop.
     */
    @Override
    public final synchronized void start() {
        this.workThreads.forEach(thread -> thread.start());
        while (this.running) {
            this.input.pollEvents();
            if (this.window.requestedClose()) {
                this.running = false;
            }
            waitTick(this.MAX_FRAME_DURATION);
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
                long now = System.nanoTime();
                while (this.running) {
                    if (!this.idle) {
                        now = System.nanoTime();
                        game.update((System.nanoTime() - curTime) / (float) 1e6);
                        curTime = now;
                    }
                    // waitTick(MAX_FRAME_DURATION - System.nanoTime() +
                    // curTime);
                    waitBarrier(this.barrier);
                }
            });
            this.workThreads.add(logic);
            GraphicsProvider.getGraphics().makeContext(0L);
            Thread rendering = new Thread(() -> {
                Thread.currentThread().setName("render");
                GraphicsProvider.getGraphics().makeContext(this.window.getContext());
                GraphicsProvider.getGraphics().init();

                this.window.setWindowContextCallback(context -> {
                    GraphicsProvider.getGraphics().makeContext(context);
                    GraphicsProvider.getGraphics().init();
                    this.input.listenToWindow(context);
                });
                this.window.setSizeCallback((w, h) -> {
                    this.window.setWindowSize(w, h);
                    this.resizeViewport = true;
                });
                while (this.running) {
                    if (this.resizeViewport) {
                        GraphicsProvider.getGraphics().setViewport(game.getScreen().getViewport().getLeftOffset(),
                                game.getScreen().getViewport().getTopOffset(),
                                this.window.getWidth() - game.getScreen().getViewport().getRightOffset(),
                                this.window.getHeight() - game.getScreen().getViewport().getBottomOffset());
                    }
                    if (!this.idle) {
                        StatMonitor.newFrame();
                        game.render();
                    }
                    waitBarrier(this.barrier);
                }
            });
            this.workThreads.add(rendering);
        } else {
            GraphicsProvider.getGraphics().makeContext(0L);
            Thread rendering = new Thread(() -> {
                Thread.currentThread().setName("render");
                GraphicsProvider.getGraphics().makeContext(this.window.getContext());
                GraphicsProvider.getGraphics().init();
                this.window.setSizeCallback((w, h) -> {
                    this.window.setWindowSize(w, h);
                });
                this.window.setWindowContextCallback(context -> {
                    GraphicsProvider.getGraphics().makeContext(context);
                    GraphicsProvider.getGraphics().init();
                    this.input.listenToWindow(context);
                });
                long curTime = System.nanoTime() - 16;
                while (this.running) {
                    if (this.resizeViewport) {
                        GraphicsProvider.getGraphics().setViewport(game.getScreen().getViewport().getLeftOffset(),
                                game.getScreen().getViewport().getTopOffset(),
                                this.window.getWidth() - game.getScreen().getViewport().getRightOffset(),
                                this.window.getHeight() - game.getScreen().getViewport().getBottomOffset());
                    }
                    if (!this.idle) {
                        this.input.pollEvents();
                        game.update((System.nanoTime() - curTime) / (float) 1e6);
                        curTime = System.nanoTime();
                        StatMonitor.newFrame();
                        game.render();
                    }
                    waitTick(this.MAX_FRAME_DURATION - System.nanoTime() + curTime);
                    waitBarrier(this.barrier);
                }
            });
            this.workThreads.add(rendering);
        }
    }

    @Override
    public synchronized void destroy() {
        stop();
        this.workThreads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                LOGGER.debug("Interrupted worker thread by joining them at start end", e);
            }
        });
    }
}
