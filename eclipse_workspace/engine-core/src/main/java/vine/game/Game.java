package vine.game;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vine.application.StatMonitor;
import vine.event.Event.EventType;
import vine.event.EventDispatcher;
import vine.event.EventListener;
import vine.game.GameObject.ReferenceManager;
import vine.game.screen.GameScreen;
import vine.game.screen.Screen;
import vine.gameplay.scene.Scene;
import vine.gameplay.scene.SceneCreationException;
import vine.graphics.Graphics;
import vine.settings.Configuration;
import vine.window.Window;

/**
 * Manages the gameplay on a global level. That is managing level changer
 * persistent objects and resources.
 * 
 * * Use the instantiate methods to create new objects of GameObject derived
 * classes.
 * 
 * You can add a name parameter to the instantiate method. Use this to declare a
 * name identifier for the new gameobject you can later use, to access the
 * gameobject from other objects in the game. You might check, that the given
 * name parameter is not used for a gameobject already though or the
 * instantiation of the gameobject will fail.
 * 
 * @author Steffen
 *
 */
public final class Game {
    /**
     * Used logger for gameplay logs.
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(Game.class);
    private static Game runningGame = new Game();

    private static float timer = 0;

    private Scene scene;
    private Screen screen;
    private EventDispatcher eventDispatcher;
    private Graphics graphics;
    private Configuration configuration;

    protected Set<GameObject> updateList = new HashSet<>();

    private Game() {

    }

    protected EventDispatcher getEventDispatcher() {
        return eventDispatcher;
    }

    /**
     * @return The current running game.
     */
    public static Game getGame() {
        return runningGame;
    }

    /**
     * @return
     */
    public static Configuration getSettings() {
        return runningGame.configuration;
    }

    /**
     * @return Returns the games screen.
     */
    public Screen getScreen() {
        return screen;
    }

    /**
     * @return The current scene of the game.
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * @return
     */
    public Graphics getGraphics() {
        return graphics;
    }

    /**
     * @param delta
     *            The time that passed since the last update
     */
    static void update(final float delta) {
        timer += delta;
        if (timer > 1000) {
            timer = 0;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Start new update at "
                        + new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.GERMAN).format(new Date())
                        + "\nTime delta is:" + delta + " milliseconds\nCurrent FPS about:" + StatMonitor.getFPS()
                        + "\n");
            }
        }
        for (GameObject object : runningGame.updateList) {
            object.update(delta);
        }
    }

    /**
     * 
     */
    static void render() {
        runningGame.scene.render();
    }

    /**
     * Creates a new World and destroys all objects of the old world.
     * 
     * @param window
     *            the window the game runs in
     * @param graphics
     *            The graphics provider used to render the game
     */
    static void init(final Window window, final Graphics graphics) {
        runningGame.screen = new GameScreen(window, 1280, 720);
        runningGame.graphics = graphics;
        runningGame.eventDispatcher = new EventDispatcher();

        Game.getGame().getEventDispatcher().registerListener(new EventListener(EventType.KEY));
        Game.getGame().getEventDispatcher().registerListener(new EventListener(EventType.MOUSE_BUTTON));
        Game.getGame().getEventDispatcher().registerListener(new EventListener(EventType.MOUSE_MOVE));

        runningGame.configuration = new Configuration("res/settings.ini");
        runningGame.scene = new Scene();

        changeLevel("default-level");
    }

    /**
     * @param level
     *            The asset name of the level that should be loaded.
     */
    public static void changeLevel(final String level) {
        getObjectsByType(GameObject.class).stream().forEach(object -> {
            if (!object.isLevelPeristent()) {
                object.destroy();
            }
        });
        try {
            runningGame.scene.loadScene(level);
        } catch (SceneCreationException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Failed to create scene " + level, e);
            }
        }

    }

    /**
     * @param type
     *            Class, that is instantiated
     * @param params
     *            The optinal arguments of the construct method of the
     *            instantiated type.
     * @return Returns all GameObjects in the Game of the given type.
     */
    public static <T extends GameObject> T instantiate(final Class<T> type, final Object... params) {
        return type == null ? null
                : ReferenceManager.instantiate(type, ReferenceManager.generateObjectName(type), params);
    }

    /**
     * @param type
     *            The class type of the new object. Has to be a subclass of
     *            Gameobject.
     * @param name
     *            The name of the object. The gameobject can be found by this
     *            name throughout the game.
     * @param params
     *            The optinal arguments of the construct method of the
     *            instantiated type.
     * @return the newly created gameobject
     */
    public static <T extends GameObject> T instantiate(final Class<T> type, final String name, final Object... params) {
        return !GameUtils.isValidGameObjectName(name) || type == null ? null
                : ReferenceManager.instantiate(type, name, params);
    }

    /**
     * @param type
     *            Class that is used to look for objects of.
     * @return A stream object with all gameobject of the given type.
     */
    public static <T extends GameObject> List<T> getObjectsByType(final Class<T> type) {
        final List<T> list = new ArrayList<>();
        for (final GameObject object : ReferenceManager.OBJECTS.values()) {
            if (object.getClass() == type) {
                list.add(type.cast(object));
            }
        }
        return list;
    }

    /**
     * @param name
     *            Name identifier that is look up for a object.
     * @return The objects, that exists in the system by the given name.
     */
    public static GameObject getObjectByName(final String name) {
        return ReferenceManager.OBJECTS.get(name);
    }

}
