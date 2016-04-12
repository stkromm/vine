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
import vine.game.Layer;
import vine.game.World;
import vine.game.gui.GameUserInterface;
import vine.game.gui.Text;
import vine.game.scene.Scene;
import vine.game.screen.GameScreen;
import vine.game.screen.Screen;
import vine.graphics.Graphics;
import vine.graphics.GraphicsProvider;
import vine.input.Input;
import vine.settings.Configuration;
import vine.window.Window;
import vine.window.WindowConfig;

/**
 * @author Steffen
 *
 */
public final class Launch {
    static final Logger LOGGER = LoggerFactory.getLogger(Launch.class);

    private Launch() {
        // Only instantiate through main method.
    }

    /**
     * @param args
     *            Contains the settings of the game. Contains the startup-level
     *            path, asset path variables, game title, config-file paths,
     *            window and graphic settings.
     */
    public static void main(final String... args) {
        Launch.run(args);
    }

    private static EngineMode parseEngineMode(String... commandLine) {
        return EngineMode.DEBUG;
    }

    /**
     * Begin the game loop.
     */
    private static void run(String... commandLine) {
        switch (Launch.parseEngineMode(commandLine)) {
        case DEBUG:
            PropertyConfigurator.configure(Launch.class.getClassLoader().getResource("log4j-debug.properties"));
            // StatMonitor.logGC();
            break;
        case PRODUCTION:
            PropertyConfigurator.configure(Launch.class.getClassLoader().getResource("log4j-production.properties"));
            break;
        case EDITOR:
            PropertyConfigurator.configure(Launch.class.getClassLoader().getResource("log4j-debug.properties"));
            break;
        default:
            PropertyConfigurator.configure(Launch.class.getClassLoader().getResource("log4j-debug.properties"));
        }

        Launch.LOGGER.info("Started application");
        Launch.LOGGER.info("Resolving application platform dependencies.");
        Launch.LOGGER.info("Checking display device.");
        final Display display = PlatformResolver.getDisplay("vine.platform.lwjgl3.GLFWDisplay");
        Launch.LOGGER.info("Creating system application window.");
        final Window window = PlatformResolver.getWindow("vine.platform.lwjgl3.GLFWWindow", display);
        Launch.LOGGER.info("Checking input devices.");
        final Input input = PlatformResolver.getInput("vine.platform.lwjgl3.GLFWInput", window);
        Launch.LOGGER.info("Assign graphics provider.");
        final Graphics graphics = PlatformResolver.getGraphics();
        graphics.makeContext(window.getContext());
        graphics.init();
        GraphicsProvider.setGraphics(graphics);

        Launch.LOGGER.info("Creating game.");
        final Screen screen = new GameScreen(window, 1280, 720);

        Launch.LOGGER.info("Assiging game layers.");
        final World game = new World(screen);
        final Layer scene = new Scene(game);
        final GameUserInterface gui = new GameUserInterface();
        game.addLayer(scene);
        game.addLayer(gui);
        window.setWindowContextCallback(context -> {
            input.listenToWindow(context);
        });

        Launch.LOGGER.info("Setting up event dispatcher.");
        final EventDispatcher dispatcher = new EventDispatcher();
        input.setKeyCallback((win, key, scancode, action, mods) -> {
            dispatcher.dispatch(new KeyEvent(key, scancode, action, mods));
        });
        input.setMouseButtonCallback((win, key, action, mods) -> {
            dispatcher.dispatch(new MouseButtonEvent(key, action, mods, input.getCursorX(), input.getCursorY()));
        });
        dispatcher.registerListener(scene.getListener());
        final EventListener debugKeyEventListener = new EventListener();
        debugKeyEventListener.addEventHandler(EventType.KEY, e -> {
            Launch.LOGGER.debug("Dispatched key event \n" + e.toString());
            return false;
        });
        // dispatcher.registerListener(debugKeyEventListener);

        Launch.LOGGER.info("Load configuration settings");
        final Configuration configuration = new Configuration("res/settings.ini");
        configuration.addConfigurable(new WindowConfig(window));
        configuration.load();
        configuration.apply();

        final Engine runner = new Engine(window, input, game);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Launch.LOGGER.info("Dispose resources");
            runner.destroy();
            window.close();
            Launch.LOGGER.info("Save settings");
            configuration.save();
        }, "shutdown"));

        Launch.LOGGER.info("Start the game");
        runner.create();
        gui.addWidget(game.instantiate(Text.class));
        runner.start();
    }
}