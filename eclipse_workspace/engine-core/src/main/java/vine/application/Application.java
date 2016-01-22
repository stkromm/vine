package vine.application;

import java.util.logging.Level;
import java.util.logging.Logger;

import vine.display.Display;
import vine.game.GameRunner;
import vine.graphics.Graphics;
import vine.input.Input;
import vine.window.Window;
import vine.window.WindowCreationException;

/**
 * @author Steffen
 *
 */
public class Application {
    /**
     * 
     */
    protected Display display;

    /**
     * @return The display of the application
     */
    public Display getDisplay() {
        return display;
    }

    /**
     * @param args
     *            Contains the settings of the game. Contains the startup-level
     *            path, asset path variables, game title, config-file paths,
     *            window and graphic settings.
     */
    public static void main(String[] args) {
        Application app = new Application();
        app.run();
    }

    /**
     * Begin the game loop.
     */
    public void run() {
        // Don't change this order, window must be the first to initialize
        // because of glfw dependencies
        Window window = PlatformDependencyResolver.getPlatformWindow();
        display = PlatformDependencyResolver.getDisplay();
        Input input = PlatformDependencyResolver.getInput();
        // end glfw components
        Graphics graphics = PlatformDependencyResolver.getGraphics();
        try {
            window.init(display);
        } catch (WindowCreationException e) {
            Logger.getGlobal().log(Level.SEVERE, "Failed to initialize window", e);
        }
        input.listenToWindow(window.getContext());
        GameRunner runner = new GameRunner(window, input, graphics);
        runner.run();
        window.close();
    }

    /**
     * @return The number of (virtual) processors available to the system.
     */
    public static int getProcessorCount() {
        return 1;//Runtime.getRuntime().availableProcessors();
    }
}