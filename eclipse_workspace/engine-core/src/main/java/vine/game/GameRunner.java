package vine.game;

import vine.application.Application;
import vine.application.StatMonitor;
import vine.game.screen.Viewport;
import vine.graphics.Graphics;
import vine.input.Input;
import vine.input.InputMapper;
import vine.settings.GameSettings;
import vine.window.Window;

/**
 * @author Steffen
 *
 */
public class GameRunner {
    /**
     * 
     */
    private volatile boolean running = true;
    /**
     * 
     */
    private final Window window;
    /**
     * 
     */
    private final Input input;
    /**
     * 
     */
    private final Graphics graphics;

    /**
     * @param application
     *            The application, that contains the game
     * @param window
     *            System window
     * @param input
     *            Input Manager
     * @param graphics
     *            Graphics Provider
     */
    public GameRunner(final Application application, final Window window, final Input input, final Graphics graphics) {
        if (application != null) {
            // LOG
        }
        this.window = window;
        this.input = input;
        this.graphics = graphics;
    }

    /**
     * Executes the game loop.
     */
    public void run() {
        // init render thread
        Thread.currentThread().setName("render");
        graphics.makeContext(window.getContext());
        graphics.init();
        window.setSizeCallback((w, h) -> {
            window.setWindowSize(w, h);
            Viewport viewport = Game.getGame().getScreen().getViewport();
            graphics.setViewport(viewport.getLeftOffset(), viewport.getTopOffset(), w - viewport.getRightOffset(),
                    h - viewport.getBottomOffset());
        });
        // init game
        Game.init(window);
        InputMapper.initInput(input, Game.getGame().getEventDispatcher());

        final int cores = Application.getProcessorCount();

        if (cores == 1) {
            long currentTime = 0;
            while (running) {
                StatMonitor.newUp();
                input.pollEvents();
                Game.update((System.nanoTime() - currentTime) / 100000000.f);
                currentTime = System.nanoTime();
                StatMonitor.newFrame();
                graphics.clearBuffer();
                Game.render();
                graphics.swapBuffers();
                sleep((int) (GameSettings.getMaxFrameDuration() * 1000000000 - System.nanoTime() + currentTime));
                if (window.requestedClose()) {
                    running = false;
                }
            }
        } else if (cores >= 2) {
            final Thread logic = logicThread();
            logic.start();
            long currentTime;
            while (running) {
                StatMonitor.newFrame();
                currentTime = System.nanoTime();
                input.pollEvents();
                graphics.clearBuffer();
                Game.render();
                graphics.swapBuffers();
                sleep((int) (GameSettings.getMaxFrameDuration() * 1000000000 - System.nanoTime() + currentTime));
                if (window.requestedClose()) {
                    running = false;
                }
            }
            logic.interrupt();
        }
    }

    /**
     * @return A thread, used to calculate the game logic.
     */
    private Thread logicThread() {
        final Thread logic = new Thread(() -> {
            long currentTime = System.nanoTime();
            while (running) {
                StatMonitor.newUp();
                Game.update((System.nanoTime() - currentTime) / 100000000.f);
                currentTime = System.nanoTime();
                sleep((int) (GameSettings.getMaxFrameDuration() * 1000000000 - System.nanoTime() + currentTime));
            }
        });
        logic.setName("logic");
        return logic;
    }

    /**
     * @param sleepTime
     *            The time, the thread should sleep. It is not guaranteed the
     *            thread will sleep the given time.
     */
    private static final void sleep(final long sleepTime) {
        if (sleepTime > 0) {
            try {
                Thread.sleep(sleepTime / 100000000L, (int) (sleepTime % 1000000L));
            } catch (InterruptedException e) {
                // do nothing
            }
        }
    }
}
