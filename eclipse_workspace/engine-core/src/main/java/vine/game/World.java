package vine.game;

import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.Semaphore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vine.application.PerformanceMonitor;
import vine.event.EventDispatcher;
import vine.game.GameObject.ReferenceManager;
import vine.game.scene.Scene;
import vine.game.screen.Screen;
import vine.graphics.GraphicsProvider;
import vine.settings.Configuration;
import vine.time.TimerManager;

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
public final class World {
    /**
     * Used logger for gameplay logs.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(World.class);
    private final Screen screen;
    private final Configuration configuration;
    private final Set<GameObject> updateList = new LinkedHashSet<>(1000);
    private final Deque<Layer> layers = new ArrayDeque<>();
    final Deque<GameObject> addList = new ArrayDeque<>(100);
    final Deque<GameObject> removeList = new ArrayDeque<>(100);
    private final Semaphore available = new Semaphore(1, true);
    /**
     * 
     */
    protected EventDispatcher dispatcher;

    /**
     * @param screen
     *            Screen to render the game on
     * @param layers
     *            The layers to render
     */
    public World(final Screen screen) {
        this.screen = screen;
        this.configuration = new Configuration("res/settings.ini");
    }

    /**
     * @return Getter
     */
    public Screen getScreen() {
        return this.screen;
    }

    /**
     * @return Getter
     */
    public final Configuration getSettings() {
        return this.configuration;
    }

    private void preUpdate() {
        if (!this.addList.isEmpty()) {

            this.updateList.addAll(this.addList);
            try {
                this.available.acquire();
            } catch (final InterruptedException exception) {
            }
            for (final GameObject o : this.addList) {
                o.begin();
            }
            this.available.release();
            this.addList.clear();
        }
    }

    public void addObject(GameObject object) {
        try {
            this.available.acquire();
        } catch (final InterruptedException exception) {
        }
        this.addList.add(object);
        this.available.release();
        object.registerDestructionCallback(o -> this.removeList.add(object));
    }

    /**
     * @param delta
     *            The time that passed since the last update
     */
    public void update(final float delta) {
        this.preUpdate();
        for (final GameObject object : this.updateList) {
            object.update(delta);
        }
        this.postUpdate();
    }

    private void postUpdate() {
        if (!this.removeList.isEmpty()) {
            this.updateList.removeAll(this.removeList);
            this.removeList.clear();
        }
    }

    /**
     * 
     */
    public synchronized void render() {
        GraphicsProvider.getGraphics().clearBuffer();
        for (final Layer layer : this.layers) {
            layer.render(this.screen);
        }
        GraphicsProvider.getGraphics().swapBuffer();
    }

    /**
     * @param level
     *            The asset name of the level that should be loaded.
     */
    public void changeLevel(final String level) {
        World.getObjectsByType(GameObject.class).stream().forEach(object -> {
            if (!object.isLevelPersistent()) {
                object.destroy();
            }
        });
        TimerManager.get().createTimer(1, -1, () -> {
            if (World.LOGGER.isDebugEnabled()) {
                World.LOGGER.debug("Start new update at "
                        + new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.GERMAN).format(new Date())
                        + "\nTime delta is:" + 1 / PerformanceMonitor.getFPS() + " milliseconds\nCurrent FPS about:"
                        + PerformanceMonitor.getFPS() + "\n");
            }
        });
        for (final Layer layer : this.layers) {
            if (layer.getName().equals("Scene")) {
                ((Scene) layer).loadScene(level, this.screen);
            }
        }
    }

    /**
     * @param type
     *            Class, that is instantiated
     * @param params
     *            The optional arguments of the construct method of the
     *            instantiated type.
     * @return Returns all GameObjects in the Game of the given type.
     */
    public <T extends GameObject> T instantiate(final Class<T> type, final Object... params) {
        return type == null ? null
                : ReferenceManager.instantiate(this, type, ReferenceManager.generateObjectName(type), params);
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
    public <T extends GameObject> T instantiate(final Class<T> type, final String name, final Object... params) {
        return !GameUtils.isValidGameObjectName(name) || type == null ? null
                : ReferenceManager.instantiate(this, type, name, params);
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

    public void addLayer(Layer scene) {
        this.layers.push(scene);
    }
}
