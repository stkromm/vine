package vine.game;

import java.lang.ref.WeakReference;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Semaphore;

import vine.game.GameObject.ReferenceManager;
import vine.game.scene.Scene;
import vine.game.screen.Screen;
import vine.physics.Physics;
import vine.settings.Configuration;
import vine.util.Log;

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
public final class World
{
    private final Configuration   configuration;
    private final Screen          screen;
    private final Scene           scene;
    private final Physics         physics;
    private final Player          player;
    private final GameState       gameState;
    private final WorldSettings   worldSettings;

    private final Set<GameObject> updatableObjects = new HashSet<>(1000);
    final Deque<GameObject>       addList          = new ArrayDeque<>(100);
    final Deque<GameObject>       removeList       = new ArrayDeque<>(100);
    private final Semaphore       available        = new Semaphore(1, true);

    /**
     * @param screen
     *            Screen to render the game on
     * @param layers
     *            The layers to render
     */
    public World(final Screen screen)
    {
        this.screen = screen;
        this.scene = new Scene();
        this.scene.setWorld(this);
        this.physics = new Physics();
        this.player = new Player();
        this.gameState = new GameState();
        this.worldSettings = new WorldSettings();
        this.configuration = new Configuration("res/settings.ini");
    }

    public final Physics getPhysics()
    {
        return this.physics;
    }

    public Configuration getGameSettings()
    {
        return this.configuration;
    }

    public WorldSettings getWorldSettings()
    {
        return this.worldSettings;
    }

    public Scene getScene()
    {
        return this.scene;
    }

    public Player getPlayer()
    {
        return this.player;
    }

    public GameState getGameState()
    {
        return this.gameState;
    }

    /**
     * @return Getter
     */
    public Screen getScreen()
    {
        return this.screen;
    }

    /**
     * @return Getter
     */
    public Configuration getSettings()
    {
        return this.configuration;
    }

    private void preUpdate()
    {
        if (!this.addList.isEmpty())
        {
            this.updatableObjects.addAll(this.addList);
            try
            {
                this.available.acquire();
            } catch (final InterruptedException exception)
            {
                Log.exception("Interrupted while waiting for add list semaphore", exception);
            }
            for (final GameObject o : this.addList)
            {
                o.begin();
            }
            this.available.release();
            this.addList.clear();
        }
    }

    public void addObject(final GameObject object)
    {
        try
        {
            this.available.acquire();
        } catch (final InterruptedException exception)
        {
            Log.exception("Interrupted while waiting for add list semaphore", exception);
        }
        this.addList.add(object);
        this.available.release();
        object.registerDestructionCallback(o -> this.removeList.add(object));
    }

    /**
     * @param delta
     *            The time that passed since the last update
     */
    public void update(final float delta)
    {
        preUpdate();
        this.scene.prepareUpdate();
        for (final GameObject object : this.updatableObjects)
        {
            object.update(delta);
        }
        postUpdate();
    }

    private void postUpdate()
    {
        if (!this.removeList.isEmpty())
        {
            this.updatableObjects.removeAll(this.removeList);
            this.removeList.clear();
        }
    }

    /**
     * @param level
     *            The asset name of the level that should be loaded.
     */
    public void changeLevel(final String level)
    {
        World.getObjectsByType(GameObject.class).stream().forEach(object ->
        {
            if (!object.isLevelPersistent())
            {
                object.destroy();
            }
        });
        LevelLoader.loadScene(level, this);
    }

    /**
     * @param type
     *            Class, that is instantiated
     * @param params
     *            The optional arguments of the construct method of the
     *            instantiated type.
     * @return Returns all GameObjects in the Game of the given type.
     */
    public <T extends GameObject> WeakReference<T> instantiate(final Class<T> type, final Object... params)
    {
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
    public <T extends GameObject> WeakReference<T> instantiate(
            final Class<T> type,
            final String name,
            final Object... params)
    {
        return !GameObject.ReferenceManager.isValidGameObjectName(name) || type == null ? null
                : ReferenceManager.instantiate(this, type, name, params);
    }

    /**
     * @param type
     *            Class that is used to look for objects of.
     * @return A stream object with all gameobject of the given type.
     */
    public static <T extends GameObject> List<T> getObjectsByType(final Class<T> type)
    {
        final List<T> list = new ArrayList<>();
        for (final GameObject object : ReferenceManager.OBJECTS.values())
        {
            if (object.getClass() == type)
            {
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
    public static GameObject getObjectByName(final String name)
    {
        return ReferenceManager.OBJECTS.get(name);
    }

    public void simulatePhysics(final float delta)
    {
    }
}
