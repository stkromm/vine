package vine.game;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vine.application.StatMonitor;
import vine.event.EventDispatcher;
import vine.game.GameObject.ReferenceManager;
import vine.game.scene.Scene;
import vine.game.screen.Screen;
import vine.graphics.Graphics;
import vine.graphics.SceneRenderer;
import vine.settings.Configuration;

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
    private static final Logger LOGGER = LoggerFactory.getLogger(Game.class);
    private final Screen screen;
    private final EventDispatcher eventDispatcher;
    private final Graphics graphics;
    private final Configuration configuration;
    private float timer = 0;
    private Scene scene;
    protected SceneRenderer renderer;
    protected final Set<GameObject> updateList = new HashSet<>(1000);

    public Game(Screen screen, final Graphics graphics, final EventDispatcher dispatcher) {
        this.screen = screen;
        this.graphics = graphics;
        this.eventDispatcher = dispatcher;
        this.configuration = new Configuration("res/settings.ini");
    }

    protected final EventDispatcher getEventDispatcher() {
        return eventDispatcher;
    }

    /**
     * @return
     */
    public final Configuration getSettings() {
        return configuration;
    }

    /**
     * @param delta
     *            The time that passed since the last update
     */
    public void update(final float delta) {
        scene.update(delta);
        timer += delta;
        if (timer > 1000) {
            timer = 0;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Start new update at "
                        + new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.GERMAN).format(new Date())
                        + "\nTime delta is:" + delta + " milliseconds\nCurrent FPS about:" + StatMonitor.getFPS()
                        + "\n");
                LOGGER.debug("" + Arrays.toString(screen.getOrthographicProjection().elements));
            }
        }
        for (final GameObject object : updateList) {
            object.update(delta);
        }
    }

    /**
     * 
     */
    public void render() {
        graphics.clearBuffer();
        renderer.renderScene(scene, screen, graphics);
        graphics.swapBuffer();
    }

    /**
     * @param level
     *            The asset name of the level that should be loaded.
     */
    public void changeLevel(final String level) {
        if (scene == null)
            this.scene = new Scene();
        if (renderer == null)
            this.renderer = new SceneRenderer();
        getObjectsByType(GameObject.class).stream().forEach(object -> {
            if (!object.isLevelPeristent()) {
                object.destroy();
            }
        });
        scene.loadScene(level, renderer, screen);
    }

    /**
     * @param type
     *            Class, that is instantiated
     * @param params
     *            The optional arguments of the construct method of the
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
