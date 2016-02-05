package vine.game;

import vine.application.Application;
import vine.application.StatMonitor;
import vine.graphics.Graphics;
import vine.input.Input;
import vine.input.InputMapper;
import vine.window.Window;

/**
 * @author Steffen
 *
 */
public class GameRunner {
    /**
     * 
     */
    private boolean running = true;

    private boolean idle = false;

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
    public GameRunner(final Application application) {
        if (application == null) {
            throw new IllegalStateException("GameRunner can only be run from the Application class");
        }
    }

    /**
     * Executes the game loop.
     */
    public void run(final Window window, final Input input, final Graphics graphics) {

        // init render thread
        Thread.currentThread().setName("render");

        // init game
        Game.init(window, graphics);
        InputMapper.initInput(input, Game.getGame().getEventDispatcher());

        final long maxFrameDuration = 13L * (long) 1e6;
        long currentTime = 0;

        while (running) {
            if (!idle) {
                input.pollEvents();
                Game.update((System.nanoTime() - currentTime) / (float) 1e6);
            }
            currentTime = System.nanoTime();
            StatMonitor.newFrame();
            graphics.clearBuffer();

            Game.getGame().getScene().render();
            graphics.swapBuffer();
            if (window.requestedClose()) {
                running = false;
            }

            //waitForNextTick((int) (maxFrameDuration - System.nanoTime() + currentTime));
        }
    }

    /**
     * @param sleepTime
     *            The time, the thread should sleep. It is not guaranteed the
     *            thread will sleep the given time.
     */
    private final static void waitForNextTick(final long sleepTime) {
        if (sleepTime > 0) {
            try {
                Thread.sleep((long) (sleepTime / 1e6), (int) (sleepTime % 1e6));
            } catch (InterruptedException e) {
                // do nothing
            }
        }
    }
}
