package vine.game.scene;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import vine.animation.AnimationClip;
import vine.animation.AnimationFrame;
import vine.animation.AnimationState;
import vine.animation.AnimationStateManager;
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
import vine.game.tilemap.Tile;
import vine.game.tilemap.TileMapObject;
import vine.game.tilemap.UniformTileMap;
import vine.gameplay.component.AnimatedSprite;
import vine.gameplay.component.PhysicsComponent;
import vine.gameplay.component.StaticSprite;
import vine.gameplay.entity.PlayerPawn;
import vine.graphics.Color;
import vine.graphics.Image;
import vine.graphics.Sprite;
import vine.graphics.renderer.SpriteRenderer;
import vine.graphics.renderer.TileMapRenderer;
import vine.math.Vector2f;
import vine.util.Log;
import vine.util.reflection.VineClass;

/**
 * @author Steffen
 *
 */
public class Scene implements Layer
{

    private long                  time;
    private final EventListener   listener                  = new EventListener();
    private static final String   KEY_EVENT_METHOD          = "onKeyEvent";
    private static final String   MOUSE_BUTTON_EVENT_METHOD = "onMouseButtonEvent";
    private static final int      HALF_CHUNK_WIDTH          = 700;
    private static final int      HALF_CHUNK_HEIGHT         = 400;
    private SpriteRenderer        renderer;
    private TileMapRenderer       tileMapRenderer;
    private Chunk[][]             chunks;
    private final Set<GameEntity> entities                  = new HashSet<>();
    private final Set<GameEntity> visibleSet                = new HashSet<>();
    private TileMapObject         map;
    private World                 world;
    /**
     * 
     */
    public final CameraManager    cameras                   = new CameraManager();

    public Scene()
    {
    }

    public void addToWorld(World world)
    {
        this.world = world;
    }

    /**
     * @return All entities that are rendered by this layer
     */
    public Set<GameEntity> getEntities()
    {
        return this.entities;
    }

    public <T extends GameEntity> T spawn(Class<T> type, float x, float y, boolean spawnIfBlocked, Object... e)
    {
        if (!spawnIfBlocked)
        {
            final boolean blocked = false;
            if (blocked)
            {
                return null;
            }
        }
        final GameEntity entity = this.getWorld().instantiate(type, e);
        entity.setPosition(x, y);
        this.add(entity);
        return type.cast(entity);
    }

    /**
     * @param i
     *            The x coordinate in multiples of the chunk width
     * @param j
     *            The y coordinate in multiples of the chunk height
     * @return The chunk corresponding to the given indices.
     */
    public Chunk getChunk(int i, int j)
    {
        return this.chunks[i][j];
    }

    public World getWorld()
    {
        return this.world;
    }

    /**
     * @return A Set of game entity that is currently visible to the active
     *         camera.
     */
    public Set<GameEntity> getVisibleEntities()
    {
        return this.visibleSet;
    }

    /**
     */
    public boolean calculateVisibleEntities()
    {
        if (System.currentTimeMillis() - this.time > 120)
        {
            int x = (int) this.cameras.getActiveCamera().getEntity().getXPosition() / (Scene.HALF_CHUNK_WIDTH * 2);
            int y = (int) this.cameras.getActiveCamera().getEntity().getYPosition() / (Scene.HALF_CHUNK_HEIGHT * 2);
            x = 10 <= x ? 10 - 1 : x;
            y = 10 <= y ? 10 - 1 : y;
            x = x < 0 ? 0 : x;
            y = y < 0 ? 0 : y;
            final int xMod = (int) this.cameras.getActiveCamera().getEntity().getXPosition()
                    % (Scene.HALF_CHUNK_WIDTH * 2);
            final int yMod = (int) this.cameras.getActiveCamera().getEntity().getYPosition()
                    % (Scene.HALF_CHUNK_HEIGHT * 2);
            this.visibleSet.clear();
            this.visibleSet.addAll(this.chunks[x][y].entities.values());
            if (Scene.HALF_CHUNK_WIDTH > xMod && x > 0)
            {
                this.visibleSet.addAll(this.chunks[x - 1][y].entities.values());
            } else if (x < 9)
            {
                this.visibleSet.addAll(this.chunks[x + 1][y].entities.values());
            }
            if (Scene.HALF_CHUNK_HEIGHT > yMod && y > 0)
            {
                this.visibleSet.addAll(this.chunks[x][y - 1].entities.values());
            } else if (y < 9)
            {
                this.visibleSet.addAll(this.chunks[x][y + 1].entities.values());
            }
            if (xMod <= Scene.HALF_CHUNK_WIDTH && yMod <= Scene.HALF_CHUNK_HEIGHT && x > 0 && y > 0)
            {
                this.visibleSet.addAll(this.chunks[x - 1][y - 1].entities.values());
            } else if (xMod <= Scene.HALF_CHUNK_WIDTH && yMod > Scene.HALF_CHUNK_HEIGHT && x > 0 && y < 9)
            {
                this.visibleSet.addAll(this.chunks[x - 1][y + 1].entities.values());
            } else if (xMod > Scene.HALF_CHUNK_WIDTH && yMod > Scene.HALF_CHUNK_HEIGHT && x < 9 && y < 9)
            {
                this.visibleSet.addAll(this.chunks[x + 1][y + 1].entities.values());
            } else if (x < 9 && y > 0)
            {
                this.visibleSet.addAll(this.chunks[x + 1][y - 1].entities.values());
            }
            this.time = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    /**
     * Adds the given entity to this layer.
     * 
     * @param entity
     *            Entity that will be rendered with this layer.
     */
    public final void add(final GameEntity entity)
    {
        if (!this.entities.contains(entity))
        {
            this.entities.add(entity);
            entity.registerDestructionCallback(e -> this.entities.remove(e));
            entity.setScene(this);
            final VineClass<?> entityClass = new VineClass<>(entity.getClass());
            if (entityClass.hasMethodImplemented(Scene.KEY_EVENT_METHOD, KeyEvent.class))
            {
                final EventHandler handler = event -> entity.onKeyEvent((KeyEvent) event);
                this.listener.addEventHandler(EventType.KEY, handler);
                entity.registerDestructionCallback(o -> this.listener.remove(handler));
            }
            if (entityClass.hasMethodImplemented(Scene.MOUSE_BUTTON_EVENT_METHOD, MouseButtonEvent.class))
            {
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
    public void loadScene(final String level)
    {
        Log.lifecycle("Load Scene");
        this.chunks = new Chunk[10][10];
        for (int i = 0; i < 10; i++)
        {
            for (int j = 0; j < 10; j++)
            {
                this.chunks[i][j] = new Chunk();
            }
        }
        final Image chipset = AssetManager.loadSync("chipset", Image.class);
        float[] uv1 = chipset.getPackedUVSquad(16, 16, 16, 16);
        float[] uv2 = chipset.getPackedUVSquad(96, 32, 16, 16);
        final AnimationStateManager animation = new AnimationStateManager(new AnimationState[] { new AnimationState(
                new AnimationClip(new AnimationFrame(uv1, 500), new AnimationFrame(uv2, 1000)), "idle", 2) });
        final AnimatedSprite water = new AnimatedSprite(animation, chipset, 32, 32);
        final Sprite grass = new StaticSprite(chipset, 0, 0, 16, 16, 32, 32);
        final Sprite earth = new StaticSprite(chipset, 32, 48, 16, 16, 32, 32);
        final Tile[] indices = new Tile[200 * 200];
        for (int i = 0; i < indices.length; i++)
        {
            if (Math.random() > 0.3)
            {
                indices[i] = new Tile(grass, new Color(0, 0, 0, 0), false, 0.5f);
            } else if (Math.random() > 0.2)
            {
                indices[i] = new Tile(earth, new Color(0, 0, 0, 0), false, 0.5f);
            } else
            {
                indices[i] = new Tile(water, new Color(0, 0, 0, 0), true, 0.5f);
            }
        }
        this.map = this.world.instantiate(TileMapObject.class, new UniformTileMap(200, indices, chipset));
        this.map.attachComponent(water);
        this.renderer = new SpriteRenderer();
        this.tileMapRenderer = new TileMapRenderer(this.world.getScreen());
        this.tileMapRenderer.submit(this.map.getMap(), this.world.getScreen());

        final PlayerPawn entity = this.world.instantiate(PlayerPawn.class, "player");

        final Camera camera = this.cameras.instantiateCamera();
        entity.attachComponent(camera);
        this.cameras.activate(camera);
        final Image tex = AssetManager.loadSync("herosheet", Image.class);
        uv1 = tex.getPackedUVSquad(0, 0, 24, 32);
        uv2 = tex.getPackedUVSquad(24, 0, 24, 32);
        float[] uv3 = tex.getPackedUVSquad(48, 0, 24, 32);
        final AnimationClip moveUpwards = new AnimationClip(new AnimationFrame(uv2, 200), new AnimationFrame(uv1, 400),
                new AnimationFrame(uv2, 600), new AnimationFrame(uv3, 800));
        uv1 = tex.getPackedUVSquad(0, 64, 24, 32);
        uv2 = tex.getPackedUVSquad(24, 64, 24, 32);
        uv3 = tex.getPackedUVSquad(48, 64, 24, 32);
        final AnimationClip moveDownwards = new AnimationClip(new AnimationFrame(uv2, 200),
                new AnimationFrame(uv1, 400), new AnimationFrame(uv2, 600), new AnimationFrame(uv3, 800));
        uv1 = tex.getPackedUVSquad(0, 32, 24, 32);
        uv2 = tex.getPackedUVSquad(24, 32, 24, 32);
        uv3 = tex.getPackedUVSquad(48, 32, 24, 32);
        final AnimationClip moveRight = new AnimationClip(new AnimationFrame(uv2, 200), new AnimationFrame(uv1, 400),
                new AnimationFrame(uv2, 600), new AnimationFrame(uv3, 800));
        uv1 = tex.getPackedUVSquad(0, 96, 24, 32);
        uv2 = tex.getPackedUVSquad(24, 96, 24, 32);
        uv3 = tex.getPackedUVSquad(48, 96, 24, 32);
        final AnimationClip moveLeft = new AnimationClip(new AnimationFrame(uv2, 200), new AnimationFrame(uv1, 400),
                new AnimationFrame(uv2, 600), new AnimationFrame(uv3, 800));
        final AnimationClip idleDown = new AnimationClip(
                new AnimationFrame(tex.getPackedUVSquad(24, 64, 24, 32), 100f));
        final AnimationClip idleUp = new AnimationClip(new AnimationFrame(tex.getPackedUVSquad(24, 0, 24, 32), 100f));
        final AnimationClip idleLeft = new AnimationClip(
                new AnimationFrame(tex.getPackedUVSquad(24, 96, 24, 32), 100f));
        final AnimationClip idleRight = new AnimationClip(
                new AnimationFrame(tex.getPackedUVSquad(24, 32, 24, 32), 100f));
        final AnimationStateManager animation1 = new AnimationStateManager(new AnimationState[] {
                new AnimationState(idleUp, "idle-up", 1), new AnimationState(idleRight, "idle-right", 1),
                new AnimationState(idleDown, "idle-down", 1), new AnimationState(idleLeft, "idle-left", 1),
                new AnimationState(moveUpwards, "up", 1), new AnimationState(moveDownwards, "down", 1),
                new AnimationState(moveRight, "right", 1), new AnimationState(moveLeft, "left", 1) });
        animation1.changeState("idle-down");
        final AnimatedSprite sprite = new AnimatedSprite(animation1, tex, 48, 64);
        entity.attachComponent(sprite);
        entity.setSprite(sprite);
        entity.attachComponent(new PhysicsComponent());
        this.add(entity);

        for (int i = 0; i < 500; i++)
        {
            final GameEntity entity1 = this.spawn(GameEntity.class, (int) (Math.random() * 5000),
                    (int) (Math.random() * 5000), false);
            final StaticSprite sprite1 = new StaticSprite(AssetManager.loadSync("hero", Image.class), 0, 0, 16, 32, 32,
                    64);
            entity1.attachComponent(sprite1);
            entity1.setSprite(sprite1);
            entity1.setZ(0.1f);
            this.add(entity1);
        }
    }

    /**
     * @return The map that is currently used in the scene.
     */
    public final UniformTileMap getMap()
    {
        return this.map.getMap();
    }

    @Override
    public String getName()
    {
        return "Scene";
    }

    @Override
    public void render(Screen screen)
    {
        this.tileMapRenderer.renderScene(this);
        this.renderer.render(this);
    }

    @Override
    public EventListener getListener()
    {
        return this.listener;
    }

    public Class<? extends GameEntity> getPlayerPawnClass()
    {
        return PlayerPawn.class;
    }

    public Vector2f getStartPoint()
    {
        return new Vector2f(100, 100);
    }

    /**
     * @author Steffen
     *
     */
    public class CameraManager
    {
        private final List<Camera> managedCameras = new ArrayList<>();
        private Camera             activeCamera;

        /**
         * 
         */
        protected CameraManager()
        {
            // Default Constructor visible, so only Scene can instantiate a
            // camera manager.
        }

        /**
         * @return The camera thats viewport is currently used to render.
         */
        public Camera getActiveCamera()
        {
            return this.activeCamera;
        }

        /**
         * @param camera
         *            Camera, that should be managed
         */
        public void removeCamera(final Camera camera)
        {
            this.managedCameras.remove(camera);
        }

        /**
         * Use this method to create new cameras.
         * 
         * @return A camera that is usable in this Scene
         */
        public Camera instantiateCamera()
        {
            final Camera camera = new Camera();
            this.managedCameras.add(camera);
            return camera;
        }

        /**
         * @param camera
         *            The camera to activate
         */
        public void activate(final Camera camera)
        {
            if (this.managedCameras.contains(camera))
            {
                this.activeCamera = camera;
            }
        }
    }

}
