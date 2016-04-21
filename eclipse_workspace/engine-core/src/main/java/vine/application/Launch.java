package vine.application;

import org.apache.log4j.PropertyConfigurator;

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
import vine.graphics.RenderStack;
import vine.input.Input;
import vine.settings.Configuration;
import vine.util.Log;
import vine.window.Window;
import vine.window.WindowConfig;

/**
 * @author Steffen
 *
 */
public final class Launch
{
    private Launch()
    {
        // Only instantiate through main method.
    }

    /**
     * @param args
     *            Contains the settings of the game. Contains the startup-level
     *            path, asset path variables, game title, config-file paths,
     *            window and graphic settings.
     */
    public static void main(final String... args)
    {
        Launch.run(args);
    }

    private static EngineMode parseEngineMode(String... commandLine)
    {
        return EngineMode.DEBUG;
    }

    /**
     * Begin the game loop.
     */
    private static void run(String... commandLine)
    {
        Thread.currentThread().setName(RuntimeInfo.STARTUP_THREAD_NAME);
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
        case BENCHMARK:
        default:
            PropertyConfigurator.configure(Launch.class.getClassLoader().getResource("log4j-debug.properties"));
        }
        Log.lifecycle("\\        //  || ||\\    || ||//////");
        Log.lifecycle(" \\      //   || || \\   || ||");
        Log.lifecycle("  \\    //    || ||  \\  || ||//////");
        Log.lifecycle("   \\  //     || ||   \\ || ||");
        Log.lifecycle("    \\//      || ||    \\|| ||//////");
        Log.lifecycle("Started application");
        Log.lifecycle("Resolving application platform dependencies.");
        Log.lifecycle("Checking display device.");
        final Display display = PlatformResolver.getDisplay("vine.platform.lwjgl3.GLFWDisplay");
        Log.lifecycle("Creating system application window.");
        final Window window = PlatformResolver.getWindow("vine.platform.lwjgl3.GLFWWindow", display);
        Log.lifecycle("Checking input devices.");
        final Input input = PlatformResolver.getInput("vine.platform.lwjgl3.GLFWInput", window);
        Log.lifecycle("Assign graphics provider.");
        final Graphics graphics = PlatformResolver.getGraphics();
        graphics.init();
        GraphicsProvider.setGraphics(graphics);
        window.setWindowContextCallback(context ->
        {
            input.listenToWindow(context);
        });

        Log.lifecycle("Creating game.");
        final Screen screen = new GameScreen(window, 960, 540);

        Log.lifecycle("Assiging game layers.");

        final World game = new World(screen);
        final Scene scene = game.getScene();
        final GameUserInterface gui = new GameUserInterface(screen);
        final RenderStack renderStack = new RenderStack(new Layer[] { scene, gui }, window, screen, input);
        Log.lifecycle("Setting up event dispatcher.");
        final EventDispatcher dispatcher = new EventDispatcher();
        input.setKeyCallback((win, key, scancode, action, mods) ->
        {
            dispatcher.dispatch(new KeyEvent(key, scancode, action, mods));
        });
        input.setMouseButtonCallback((win, key, action, mods) ->
        {
            dispatcher.dispatch(new MouseButtonEvent(key, action, mods, input.getCursorX(), input.getCursorY()));
        });
        dispatcher.registerListener(scene.getListener());
        final EventListener debugKeyEventListener = new EventListener();
        debugKeyEventListener.addEventHandler(EventType.KEY, e ->
        {
            Log.debug("Dispatched key event \n" + e.toString());
            return false;
        });
        // dispatcher.registerListener(debugKeyEventListener);

        Log.lifecycle("Load configuration settings");
        final Configuration configuration = new Configuration("res/settings.ini");
        configuration.addConfigurable(new WindowConfig(window));
        configuration.load();
        configuration.apply();

        final Engine runner = new Engine(window, input, game, renderStack);
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
        {
            Thread.currentThread().setName(RuntimeInfo.SHUTDOWN_THREAD_NAME);
            Log.lifecycle("Dispose resources");
            runner.destroy();
            window.close();
            Log.lifecycle("Save settings");
            configuration.save();
            Log.lifecycle("Successfully closed the game.");
            Log.lifecycle("Total run time:" + PerformanceMonitor.getRunningTime() / 1000f + "seconds");
            Log.lifecycle("Average fps:" + PerformanceMonitor.getAverageFps());
            Log.lifecycle("Lowest fps:" + PerformanceMonitor.getLowestFps());
            Log.lifecycle("Highest fps:" + PerformanceMonitor.getHighestFps());
        }, "shutdown"));
        runner.create();
        gui.addWidget(game.instantiate(Text.class));
        Log.lifecycle("Start the game");
        runner.start();
    }
}