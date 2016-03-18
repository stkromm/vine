package vine.application;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vine.display.Display;
import vine.event.Event.EventType;
import vine.event.EventDispatcher;
import vine.event.EventListener;
import vine.event.KeyEvent;
import vine.event.MouseButtonEvent;
import vine.game.Game;
import vine.game.screen.GameScreen;
import vine.game.screen.Screen;
import vine.game.screen.Viewport;
import vine.graphics.Graphics;
import vine.graphics.GraphicsProvider;
import vine.input.Input;
import vine.input.InputMapper;
import vine.settings.Configuration;
import vine.window.Window;
import vine.window.WindowConfig;

/**
 * @author Steffen
 *
 */
public final class Application {
    private static final Application INSTANCE = new Application();
    /**
     * vine.application package logger.
     */
    static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

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
        Application.INSTANCE.run(EngineMode.DEBUG, args);
    }

    /**
     * Begin the game loop.
     */
    private void run(EngineMode mode, String... args) {
        switch (mode) {
        case DEBUG:
            PropertyConfigurator.configure("src/main/java/log4j-debug.properties");
            // StatMonitor.logGC();
            break;
        case PRODUCTION:
            PropertyConfigurator.configure("src/main/java/log4j-production.properties");
            break;
        default:
            PropertyConfigurator.configure("src/main/java/log4j-debug.properties");
        }
        LOGGER.info("Started application");
        LOGGER.info("Resolving application platform dependencies.");
        LOGGER.info("Checking display device.");
        final Display display = PlatformDependencyResolver.getDisplay();
        LOGGER.info("Creating system application window.");
        final Window window = PlatformDependencyResolver.getPlatformWindow(display);
        LOGGER.info("Checking input devices.");
        final Input input = PlatformDependencyResolver.getInput(window);
        LOGGER.info("Assign graphics provider.");
        final Graphics graphics = PlatformDependencyResolver.getGraphics();
        graphics.makeContext(window.getContext());
        graphics.init();
        GraphicsProvider.setGraphics(graphics);

        final EventDispatcher dispatcher = new EventDispatcher();
        dispatcher.registerListener(new EventListener(EventType.KEY));
        dispatcher.registerListener(new EventListener(EventType.MOUSE_BUTTON));
        dispatcher.registerListener(new EventListener(EventType.MOUSE_MOVE));
        input.setKeyCallback((win, key, scancode, action, mods) -> {
            if (InputMapper.getNumberOfKeys() > key && key >= 0) {
                InputMapper.setKeyPressed(key, action);
            }
            dispatcher.dispatch(new KeyEvent(key, scancode, action, mods));
        });
        input.setMouseButtonCallback((win, key, action, mods) -> {
            dispatcher.dispatch(new MouseButtonEvent(key, action, mods, input.getCursorX(), input.getCursorY()));
        });
        final Screen screen = new GameScreen(window, 1280, 720);
        final Game game = new Game(screen, graphics, dispatcher);
        window.setSizeCallback((w, h) -> {
            window.setWindowSize(w, h);
            final Viewport viewport = screen.getViewport();
            graphics.setViewport(viewport.getLeftOffset(), viewport.getTopOffset(), w - viewport.getRightOffset(),
                    h - viewport.getBottomOffset());
        });
        window.setWindowContextCallback(context -> {
            graphics.makeContext(context);
            graphics.init();
            input.listenToWindow(context);
        });
        LOGGER.info("Load configuration settings");
        Configuration configuration = new Configuration("res/settings.ini");
        configuration.addConfigurable(new WindowConfig(window));
        configuration.load();
        configuration.apply();

        final GameLifecycle runner = new GameLifecycle(window, input, game);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Dispose resources");
            runner.destroy();
            window.close();
            LOGGER.info("Save settings");
            configuration.save();
        } , "shutdown"));

        LOGGER.info("Start the game");

        runner.create();
        runner.start();
    }
}