package vine.application;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vine.display.Display;
import vine.game.GameRunner;
import vine.graphics.Graphics;
import vine.input.Input;
import vine.window.Window;

/**
 * @author Steffen
 *
 */
public final class Application {
    /**
     * vine.application package logger.
     */
    static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
    /**
     * 
     */
    private Display display;

    private Application() {
        // Only instantiate through main method.
    }

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
    public static void main(final String... args) {
        final Application app = new Application();
        // BasicConfigurator replaced with PropertyConfigurator.
        PropertyConfigurator.configure("src/main/java/log4j.properties");
        app.run();
    }

    /**
     * Begin the game loop.
     */
    private void run() {
        LOGGER.info("Started application");
        LOGGER.info("Resolving application platform dependencies.");
        LOGGER.info("Checking display device.");
        display = PlatformDependencyResolver.getDisplay();
        LOGGER.info("Checking system application window.");
        final Window window = PlatformDependencyResolver.getPlatformWindow(display);
        LOGGER.info("Checking input devices.");
        final Input input = PlatformDependencyResolver.getInput(window);
        LOGGER.info("Assign graphics provider.");
        final Graphics graphics = PlatformDependencyResolver.getGraphics();

        final GameRunner runner = new GameRunner(this, window, input, graphics);
        runner.run();
        window.close();
    }

    /**
     * @return The number of (virtual) processors available to the system.
     */
    public static int getProcessorCount() {
        return Runtime.getRuntime().availableProcessors();
    }
}