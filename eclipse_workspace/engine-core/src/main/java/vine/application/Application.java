package vine.application;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private final Logger logger = LoggerFactory.getLogger(Application.class);
    /**
     * 
     */
    protected Display display;

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
    public static void main(String... args) {
        final Application app = new Application();
        // BasicConfigurator replaced with PropertyConfigurator.
        PropertyConfigurator.configure("src/main/java/log4j.properties");
        app.run();
    }

    /**
     * Begin the game loop.
     */
    private void run() {
        logger.info("Started application");
        logger.info("Resolving application platform dependencies.");
        logger.info("Checking display device.");
        display = PlatformDependencyResolver.getDisplay();
        logger.info("Checking system application window.");
        final Window window = PlatformDependencyResolver.getPlatformWindow(display);
        logger.info("Checking input devices.");
        final Input input = PlatformDependencyResolver.getInput(window);
        logger.info("Assign graphics provider.");
        final Graphics graphics = PlatformDependencyResolver.getGraphics();

        GameRunner runner = new GameRunner(this, window, input, graphics);
        runner.run();
        window.close();
    }

    /**
     * @return The number of (virtual) processors available to the system.
     */
    public static int getProcessorCount() {
        return 1;// Runtime.getRuntime().availableProcessors();
    }
}