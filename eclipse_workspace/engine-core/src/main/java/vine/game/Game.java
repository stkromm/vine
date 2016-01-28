package vine.game;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vine.application.StatMonitor;
import vine.event.EventDispatcher;
import vine.game.GameObject.ReferenceManager;
import vine.game.screen.GameScreen;
import vine.game.screen.Screen;
import vine.gameplay.component.Camera;
import vine.gameplay.component.Sprite;
import vine.gameplay.entity.GameEntity;
import vine.gameplay.entity.PlayerPawn;
import vine.gameplay.scene.Scene;
import vine.gameplay.scene.SceneCreationException;
import vine.graphics.Renderer;
import vine.settings.GameSettings;
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
    private volatile Scene scene;
    private volatile Screen screen;
    private EventDispatcher eventDispatcher;
    private static Game runningGame;

    private Game() {

    }

    protected EventDispatcher getEventDispatcher() {
        return eventDispatcher;
    }

    /**
     * @return The current running game.
     */
    public static Game getGame() {
        if (runningGame == null) {
            return null;
        }
        return runningGame;
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
     * @param delta
     *            The time that passed since the last update
     */
    static void update(final float delta) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Start new update at "
                    + new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.GERMAN).format(new Date()) + "\nTime delta is:"
                    + delta + " milliseconds\nCurrent FPS about:" + StatMonitor.getFPS() + "\n");
        }
        runningGame.scene.update(delta);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Ended update at "
                    + new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.GERMAN).format(new Date()) + "\n");
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
    static void init(final Window window) {
        runningGame = new Game();
        runningGame.screen = new GameScreen(window, 1280, 720);
        runningGame.eventDispatcher = new EventDispatcher();
        getObjectsByType(GameObject.class).stream().forEach(GameObject::destroy);
        changeLevel(GameSettings.getStartLevelName());
    }

    /**
     * @param level
     *            The asset name of the level that should be loaded.
     */
    public static void changeLevel(final String level) {
        try {
            runningGame.scene = Scene.createScene(level);
        } catch (SceneCreationException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Failed to create scene " + level, e);
            }
        }
        Game.getGame().getEventDispatcher().registerListener(runningGame.scene);
        for (int i = 0; i < 5000; i++) {
            final GameEntity entity = Game.instantiate(GameEntity.class, Integer.valueOf((i / 30) * 32),
                    Integer.valueOf((i % 30) * 32));
            final Sprite sprite = Game.instantiate(Sprite.class, Integer.valueOf(32), Integer.valueOf(32),
                    Renderer.DEFAULT_CHIPSET, Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(16),
                    Integer.valueOf(16));
            entity.addComponent(sprite);
            runningGame.scene.getEntities().add(entity);
            entity.setScene(runningGame.scene);
        }
        final PlayerPawn entity = Game.instantiate(PlayerPawn.class, Integer.valueOf(1), Integer.valueOf(2));
        final Sprite sprite = Game.instantiate(Sprite.class, Integer.valueOf(32), Integer.valueOf(64),
                Renderer.DEFAULT_TEXTURE, Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(16),
                Integer.valueOf(32));
        entity.addComponent(sprite);
        final Camera camera = runningGame.scene.cameras.instantiateCamera();
        entity.addComponent(camera);
        runningGame.getScene().cameras.activate(camera);
        runningGame.getScene().getEntities().add(entity);
        entity.setScene(runningGame.scene);
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
        return ReferenceManager.instantiate(type, ReferenceManager.generateObjectName(type), params);
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
