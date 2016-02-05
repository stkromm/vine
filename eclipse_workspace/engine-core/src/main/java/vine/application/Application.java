package vine.application;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vine.display.Display;
import vine.game.Game;
import vine.game.GameRunner;
import vine.game.screen.Viewport;
import vine.graphics.Graphics;
import vine.input.Input;
import vine.settings.Configuration;
import vine.window.Window;
import vine.window.WindowConfig;

/**
 * @author Steffen
 *
 */
public final class Application {
    private static final Application application = new Application();
    /**
     * vine.application package logger.
     */
    static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    private Configuration configuration;

    private Application() {
        // Only instantiate through main method.
    }

    /**
     * @param args
     *            Contains the settings of the game. Contains the startup-level
     *            path, asset path variables, game title, config-file paths,
     *            window and graphic settings.
     */
    public static void main(final String... args) {
        // BasicConfigurator replaced with PropertyConfigurator.
        PropertyConfigurator.configure("src/main/java/log4j.properties");
        Application.application.run();
    }

    /**
     * Begin the game loop.
     */
    private void run() {
        LOGGER.info("Started application");
        LOGGER.info("Resolving application platform dependencies.");
        LOGGER.info("Checking display device.");
        Display display = PlatformDependencyResolver.getDisplay();
        LOGGER.info("Creating system application window.");
        final Window window = PlatformDependencyResolver.getPlatformWindow(display);
        LOGGER.info("Checking input devices.");
        final Input input = PlatformDependencyResolver.getInput(window);
        LOGGER.info("Assign graphics provider.");
        final Graphics graphics = PlatformDependencyResolver.getGraphics();

        graphics.makeContext(window.getContext());
        graphics.init();
        window.setSizeCallback((w, h) -> {
            window.setWindowSize(w, h);
            final Viewport viewport = Game.getGame().getScreen().getViewport();
            graphics.setViewport(viewport.getLeftOffset(), viewport.getTopOffset(), w - viewport.getRightOffset(),
                    h - viewport.getBottomOffset());
        });
        window.setWindowContextCallback(context -> {
            graphics.makeContext(context);
            graphics.init();
            input.listenToWindow(context);
        });

        LOGGER.info("Load configuration settings");
        configuration = new Configuration("res/settings.ini");
        configuration.addConfigurable(new WindowConfig(window));
        configuration.load();
        configuration.apply();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Dispose resources");
            window.close();
            LOGGER.info("Save settings");
            configuration.save();
        } , "shutdown"));

        LOGGER.info("Start the game");
        final GameRunner runner = new GameRunner(application);
        runner.run(window, input, graphics);
    }

    /**
     * @return The number of (virtual) processors available to the system.
     */
    public static int getProcessorCount() {
        return Runtime.getRuntime().availableProcessors();
    }

}