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
import vine.game.Layer;
import vine.game.scene.Scene;
import vine.game.screen.GameScreen;
import vine.game.screen.Screen;
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
        Application.run(EngineMode.DEBUG, args);
    }

    /**
     * Begin the game loop.
     */
    private static void run(EngineMode mode, String... args) {
        switch (mode) {
        case DEBUG:
            PropertyConfigurator.configure("src/main/java/log4j-debug.properties");
            // StatMonitor.logGC();
            break;
        case PRODUCTION:
            PropertyConfigurator.configure("src/main/java/log4j-production.properties");
            break;
        case EDITOR:
            break;
        default:
            PropertyConfigurator.configure("src/main/java/log4j-debug.properties");
        }
        LOGGER.info("Started application");
        LOGGER.info("Resolving application platform dependencies.");
        LOGGER.info("Checking display device.");
        final Display display = PlatformDependencyResolver.getDisplay();
        LOGGER.info("Creating system application window.");
        final Window window = PlatformDependencyResolver.getPlatformWindow("vine.platform.lwjgl3.GLFWWindow", display);
        LOGGER.info("Checking input devices.");
        final Input input = PlatformDependencyResolver.getInput(window);
        LOGGER.info("Assign graphics provider.");
        final Graphics graphics = PlatformDependencyResolver.getGraphics();
        graphics.makeContext(window.getContext());
        graphics.init();
        GraphicsProvider.setGraphics(graphics);

        final EventDispatcher dispatcher = new EventDispatcher();
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

        Layer scene = new Scene();
        // GameUserInterface gui = new GameUserInterface();
        final Game game = new Game(screen, new Layer[] { scene });
        // gui.addWidget(Game.instantiate(Widget.class));
        window.setWindowContextCallback(context -> {
            input.listenToWindow(context);
        });
        dispatcher.registerListener(scene.getListener());
        EventListener debugKeyEventListener = new EventListener();
        debugKeyEventListener.addEventHandler(EventType.KEY, e -> {
            LOGGER.debug("Dispatched key event \n" + e.toString());
            return false;
        });
        // dispatcher.registerListener(debugKeyEventListener);
        LOGGER.info("Load configuration settings");
        Configuration configuration = new Configuration("res/settings.ini");
        configuration.addConfigurable(new WindowConfig(window));
        configuration.load();
        configuration.apply();

        final GamePlayer runner = new GamePlayer(window, input, game);
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