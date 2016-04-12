package vine.game.scene;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import vine.assets.AssetManager;
import vine.event.Event.EventType;
import vine.event.EventListener;
import vine.event.EventListener.EventHandler;
import vine.event.KeyEvent;
import vine.event.MouseButtonEvent;
import vine.game.Camera;
import vine.game.Layer;
import vine.game.World;
import vine.game.screen.Screen;
import vine.gameplay.component.StaticSprite;
import vine.gameplay.entity.PlayerPawn;
import vine.graphics.SpriteRenderer;
import vine.graphics.Texture2D;
import vine.graphics.TileMapRenderer;
import vine.reflection.VineClass;
import vine.tilemap.TileMapObject;
import vine.tilemap.UniformTileMap;

/**
 * @author Steffen
 *
 */
public class Scene implements Layer {
    private final EventListener listener = new EventListener();
    private static final String KEY_EVENT_METHOD = "onKeyEvent";
    private static final String MOUSE_BUTTON_EVENT_METHOD = "onMouseButtonEvent";
    private static final int HALF_CHUNK_WIDTH = 700;
    private static final int HALF_CHUNK_HEIGHT = 400;
    private SpriteRenderer renderer;
    private TileMapRenderer tileMapRenderer;
    private Chunk[][] chunks;
    private final Set<GameEntity> entities = new HashSet<>();
    private final Set<GameEntity> visibleSet = new HashSet<>();
    private TileMapObject map;
    private final World world;
    /**
     * 
     */
    public final CameraManager cameras = new CameraManager();

    public Scene(World game) {
        this.world = game;
    }

    /**
     * @return All entities that are rendered by this layer
     */
    public Set<GameEntity> getEntities() {
        return this.entities;
    }

    /**
     * @param i
     *            The x coordinate in multiples of the chunk width
     * @param j
     *            The y coordinate in multiples of the chunk height
     * @return The chunk corresponding to the given indices.
     */
    public Chunk getChunk(int i, int j) {
        return this.chunks[i][j];
    }

    private long time;

    /**
     * @return A Set of game entity that is currently visible to the active
     *         camera.
     */
    public Set<GameEntity> getVisibleEntities() {
        return this.visibleSet;
    }

    /**
     */
    public void calculateVisibleEntities() {
        if (System.currentTimeMillis() - this.time > 16) {
            int x = (int) this.cameras.getActiveCamera().getEntity().getXCoord() / (Scene.HALF_CHUNK_WIDTH * 2);
            int y = (int) this.cameras.getActiveCamera().getEntity().getYCoord() / (Scene.HALF_CHUNK_HEIGHT * 2);
            x = 10 <= x ? 10 - 1 : x;
            y = 10 <= y ? 10 - 1 : y;
            x = x < 0 ? 0 : x;
            y = y < 0 ? 0 : y;
            final int xMod = (int) this.cameras.getActiveCamera().getEntity().getXCoord()
                    % (Scene.HALF_CHUNK_WIDTH * 2);
            final int yMod = (int) this.cameras.getActiveCamera().getEntity().getYCoord()
                    % (Scene.HALF_CHUNK_HEIGHT * 2);
            this.visibleSet.clear();
            this.visibleSet.addAll(this.chunks[x][y].entities.values());
            if (Scene.HALF_CHUNK_WIDTH > xMod && x > 0) {
                this.visibleSet.addAll(this.chunks[x - 1][y].entities.values());
            } else if (x < 9) {
                this.visibleSet.addAll(this.chunks[x + 1][y].entities.values());
            }
            if (Scene.HALF_CHUNK_HEIGHT > yMod && y > 0) {
                this.visibleSet.addAll(this.chunks[x][y - 1].entities.values());
            } else if (y < 9) {
                this.visibleSet.addAll(this.chunks[x][y + 1].entities.values());
            }
            if (xMod <= Scene.HALF_CHUNK_WIDTH && yMod <= Scene.HALF_CHUNK_HEIGHT && x > 0 && y > 0) {
                this.visibleSet.addAll(this.chunks[x - 1][y - 1].entities.values());
            } else if (xMod <= Scene.HALF_CHUNK_WIDTH && yMod > Scene.HALF_CHUNK_HEIGHT && x > 0 && y < 9) {
                this.visibleSet.addAll(this.chunks[x - 1][y + 1].entities.values());
            } else if (xMod > Scene.HALF_CHUNK_WIDTH && yMod > Scene.HALF_CHUNK_HEIGHT && x < 9 && y < 9) {
                this.visibleSet.addAll(this.chunks[x + 1][y + 1].entities.values());
            } else if (x < 9 && y > 0) {
                this.visibleSet.addAll(this.chunks[x + 1][y - 1].entities.values());
            }
            this.time = System.currentTimeMillis();
        }
    }

    /**
     * Adds the given entity to this layer.
     * 
     * @param entity
     *            Entity that will be rendered with this layer.
     */
    public final void add(final GameEntity entity) {
        if (!this.entities.contains(entity)) {
            this.entities.add(entity);
            entity.registerDestructionCallback(e -> this.entities.remove(e));
            entity.addToScene(this);
            final VineClass<?> entityClass = new VineClass<>(entity.getClass());
            if (entityClass.hasMethodImplemented(Scene.KEY_EVENT_METHOD, KeyEvent.class)) {
                final EventHandler handler = event -> entity.onKeyEvent((KeyEvent) event);
                this.listener.addEventHandler(EventType.KEY, handler);
                entity.registerDestructionCallback(o -> this.listener.remove(handler));
            }
            if (entityClass.hasMethodImplemented(Scene.MOUSE_BUTTON_EVENT_METHOD, MouseButtonEvent.class)) {
                // GamePlayer.getRunningGame().getEventDispatcher().registerHandler(object,
                // event -> object.onMouseButtonEvent((MouseButtonEvent) event),
                // EventType.MOUSE_BUTTON);
            }
        }
    }

    /**
     * @param level
     *            The asset name of the level
     * @param screen
     *            The screen, that is used to render the game on
     */
    public void loadScene(final String level, final Screen screen) {
        this.chunks = new Chunk[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                this.chunks[i][j] = new Chunk();
            }
        }
        this.map = this.world.instantiate(TileMapObject.class, new UniformTileMap(this.world, 200, 100));
        this.renderer = new SpriteRenderer();
        this.tileMapRenderer = new TileMapRenderer();
        this.tileMapRenderer.submit(this.map.getMap(), screen);
        for (int i = 0; i < 500; i++) {
            final GameEntity entity = this.world.instantiate(GameEntity.class,
                    Integer.valueOf((int) (Math.random() * 10000)), Integer.valueOf((int) (Math.random() * 10000)));
            final StaticSprite sprite = this.world.instantiate(StaticSprite.class,
                    AssetManager.loadSync("hero", Texture2D.class), Integer.valueOf(0), Integer.valueOf(0),
                    Integer.valueOf(16), Integer.valueOf(32), Integer.valueOf(32), Integer.valueOf(64));
            entity.addComponent(sprite);
            this.add(entity);
        }
        final PlayerPawn entity = this.world.instantiate(PlayerPawn.class, "player", Integer.valueOf(1),
                Integer.valueOf(2));

        final Camera camera = this.cameras.instantiateCamera();
        entity.addComponent(camera);
        this.cameras.activate(camera);
        this.add(entity);
    }

    /**
     * @return The map that is currently used in the scene.
     */
    public final UniformTileMap getMap() {
        return this.map.getMap();
    }

    @Override
    public String getName() {
        return "Scene";
    }

    @Override
    public void render(Screen screen) {

        this.tileMapRenderer.renderScene(this, screen);
        this.renderer.renderScene(this, screen);
    }

    @Override
    public EventListener getListener() {
        return this.listener;
    }

    public World getWorld() {
        return this.world;
    }

    /**
     * @author Steffen
     *
     */
    public class CameraManager {
        private final List<Camera> managedCameras = new ArrayList<>();
        private Camera activeCamera;

        /**
         * 
         */
        protected CameraManager() {
            // Default Constructor visible, so only Scene can instantiate a
            // camera manager.
        }

        /**
         * @return The camera thats viewport is currently used to render.
         */
        public Camera getActiveCamera() {
            return this.activeCamera;
        }

        /**
         * @param camera
         *            Camera, that should be managed
         */
        public void removeCamera(final Camera camera) {
            this.managedCameras.remove(camera);
        }

        /**
         * Use this method to create new cameras.
         * 
         * @return A camera that is usable in this Scene
         */
        public Camera instantiateCamera() {
            final Camera camera = Scene.this.world.instantiate(Camera.class);
            this.managedCameras.add(camera);
            return camera;
        }

        /**
         * @param camera
         *            The camera to activate
         */
        public void activate(final Camera camera) {
            if (this.managedCameras.contains(camera)) {
                this.activeCamera = camera;
            }
        }
    }

}
