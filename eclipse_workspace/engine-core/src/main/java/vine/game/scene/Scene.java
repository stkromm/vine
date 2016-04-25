package vine.game.scene;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import vine.animation.AnimationClip;
import vine.animation.AnimationFrame;
import vine.animation.AnimationState;
import vine.animation.AnimationStateManager;
import vine.assets.AssetManager;
import vine.event.EventListener;
import vine.game.Camera;
import vine.game.Layer;
import vine.game.World;
import vine.game.screen.Screen;
import vine.game.tilemap.Tile;
import vine.game.tilemap.TileMapObject;
import vine.game.tilemap.UniformTileMap;
import vine.gameplay.AnimatedSprite;
import vine.gameplay.EnemyAI;
import vine.gameplay.PhysicsComponent;
import vine.gameplay.PlayerPawn;
import vine.gameplay.StaticSprite;
import vine.graphics.Color;
import vine.graphics.Image;
import vine.graphics.Sprite;
import vine.graphics.renderer.PrimitiveRenderer;
import vine.graphics.renderer.SpriteBatch;
import vine.graphics.renderer.TileMapRenderer;
import vine.math.Vec2f;
import vine.math.VineMath;
import vine.physics.Intersection2D;
import vine.util.Log;

/**
 * @author Steffen
 *
 */
public class Scene implements Layer
{
    interface RenderCallback
    {
        void onScreen();
    }

    public Map<RenderCallback, RenderCallback> addedCallbacks    = new ConcurrentHashMap<>();
    public Map<RenderCallback, RenderCallback> removedCallbacks  = new ConcurrentHashMap<>();
    public Set<RenderCallback>                 renderCallbacks   = new HashSet<>();
    private long                               time;
    private final EventListener                listener          = new EventListener();
    private static final int                   HALF_CHUNK_WIDTH  = 700;
    private static final int                   HALF_CHUNK_HEIGHT = 400;
    private SpriteBatch                        renderer;
    private TileMapRenderer                    tileMapRenderer;
    private final PrimitiveRenderer            primitiveRenderer = new PrimitiveRenderer();
    private Chunk[][]                          chunks;
    private final Set<GameEntity>              entities          = new HashSet<>();
    private final Set<GameEntity>              visibleSet        = new HashSet<>();
    private TileMapObject                      map;
    private World                              world;
    /**
     * 
     */
    public final CameraManager                 cameras           = new CameraManager();

    public Scene()
    {
    }

    protected Chunk[][] getChunks()
    {
        return this.chunks;
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

    public <T extends GameEntity> WeakReference<T> spawn(
            Class<T> type,
            float x,
            float y,
            boolean spawnIfBlocked,
            Object... e)
    {
        if (!spawnIfBlocked)
        {
            final boolean blocked = false;
            if (blocked)
            {
                return null;
            }
        }
        final GameEntity entity = this.getWorld().instantiate(type, e).get();
        entity.setPosition(x, y);
        this.add(entity);
        return new WeakReference<>(type.cast(entity));
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
    public void calculateVisibleEntities()
    {
        if (System.currentTimeMillis() - this.time > 120)
        {
            final int x = (int) VineMath.clamp(
                    this.cameras.getActiveCamera().getEntity().getXPosition() / (Scene.HALF_CHUNK_WIDTH * 2) - 1,
                    0,
                    9);
            final int y = (int) VineMath.clamp(
                    this.cameras.getActiveCamera().getEntity().getYPosition() / (Scene.HALF_CHUNK_HEIGHT * 2) - 1,
                    0,
                    9);
            this.visibleSet.clear();
            for (int j = 9; j >= 0; j--)
            {
                for (int i = 9; i >= 0; i--)
                {
                    this.chunks[i][j].active = i >= x && i <= x + 2 && j >= y && j <= y + 2;
                }
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
    public final void add(final GameEntity entity)
    {
        if (!this.entities.contains(entity))
        {
            this.entities.add(entity);
            entity.registerDestructionCallback(e -> this.entities.remove(e));
            entity.setScene(this);
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
        this.map = this.world.instantiate(TileMapObject.class, new UniformTileMap(200, indices, chipset)).get();
        this.map.attachComponent(water);
        this.renderer = new SpriteBatch();
        this.tileMapRenderer = new TileMapRenderer(this.world.getScreen());
        this.tileMapRenderer.submit(this.map.getMap(), this.world.getScreen());

        final PlayerPawn entity = this.world.instantiate(PlayerPawn.class, "player").get();

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
        entity.attachComponent(new PhysicsComponent());
        this.add(entity);

        for (int i = 0; i < 500; i++)
        {
            this.spawnEntity();
        }
    }

    public final void spawnEntity()
    {
        final GameEntity entity1 = this
                .spawn(GameEntity.class, (int) (Math.random() * 5000), (int) (Math.random() * 5000), false).get();
        final StaticSprite sprite1 = new StaticSprite(AssetManager.loadSync("hero", Image.class), 0, 0, 16, 32, 32, 64);
        entity1.attachComponent(sprite1);
        entity1.setZ(0.1f);
        entity1.attachComponent(new PhysicsComponent());
        if (Math.random() > 0.99)
        {
            entity1.attachComponent(new EnemyAI());
        }
        entity1.setLifetime((float) (Math.random() * 1000000));
        entity1.registerDestructionCallback(entity -> this.spawnEntity());
        entity1.addSpeed(100, 100);
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

    public void addRenderCallback(RenderCallback render)
    {
        this.addedCallbacks.put(render, render);
    }

    public void removeRenderCallback(RenderCallback render)
    {
        this.removedCallbacks.put(render, render);
    }

    @Override
    public void render(final Screen screen)
    {
        final GameEntity cameraEntity = this.cameras.getActiveCamera().getEntity();
        final float posX = cameraEntity.getXPosition() - this.getWorld().getScreen().getWidth() / 2;
        final float posY = cameraEntity.getYPosition() - this.getWorld().getScreen().getHeight() / 2 - 50;
        this.primitiveRenderer.prepare(this);
        this.renderer.prepare(this);
        for (int i = this.chunks.length - 1; i >= 0; i--)
        {
            for (int j = this.chunks[i].length - 1; j >= 0; j--)
            {
                if (this.chunks[i][j].isActive())
                {
                    for (final GameEntity entity : this.chunks[i][j].getEntities())
                    {
                        if (Intersection2D.doesAabbIntersectAabb(
                                posX,
                                posY,
                                this.getWorld().getScreen().getWidth(),
                                this.getWorld().getScreen().getHeight() + 100,
                                entity.getPosition().getX(),
                                entity.getPosition().getY(),
                                entity.getBoundingBoxExtends().getX(),
                                entity.getBoundingBoxExtends().getY()))
                        {
                            entity.onRender(this.renderer);
                        }
                    }
                }
            }
        }
        this.renderer.finish();
        this.tileMapRenderer.renderScene(this);
        if (!this.addedCallbacks.isEmpty())
        {
            for (final RenderCallback callback : this.addedCallbacks.values())
            {
                this.renderCallbacks.add(callback);
            }
            this.addedCallbacks.clear();
        }
        if (!this.renderCallbacks.isEmpty())
        {
            for (final RenderCallback callback : this.renderCallbacks)
            {
                callback.onScreen();
            }
        }
        if (!this.removedCallbacks.isEmpty())
        {
            for (final RenderCallback callback : this.removedCallbacks.values())
            {
                this.renderCallbacks.remove(callback);
            }
            this.removedCallbacks.clear();
        }
        this.primitiveRenderer.finish();
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

    public Vec2f getStartPoint()
    {
        return new Vec2f(100, 100);
    }

    /**
     * Traces for entities in the scene that intersect the given ray from the
     * origin in the given direction with the given length (in world-units).
     */
    public GameEntity lineTrace(GameEntity tracer, boolean ignoreSelf, Vec2f origin, Vec2f direction, float f)
    {
        final long startTime = System.nanoTime();
        final int startChunkX = (int) VineMath.clamp(origin.getX() / 1400f, 0, 9);
        final int startChunkY = (int) (origin.getY() * (1f / 800));
        final int endChunkX = (int) VineMath.clamp((origin.getX() + f) / 1400f, 0, 9);

        final float iLength = 1f / (float) direction.length();
        final float iDirecX = 1 / direction.getX() * iLength;
        final float iDirecY = 1 / direction.getY() * iLength;

        GameEntity result = null;
        float smallestDistance = Float.MAX_VALUE;

        for (int i = endChunkX - startChunkX; i >= 0; i--)
        {
            for (final GameEntity entity : this.chunks[startChunkX + i][startChunkY].getEntities())
            {
                if (!ignoreSelf || !tracer.equals(entity))
                {
                    final float length = Intersection2D.whereDoesRayIntersectAabb(
                            origin.getX(),
                            origin.getY(),
                            iDirecX,
                            iDirecY,
                            entity.getPosition().getX(),
                            entity.getPosition().getY(),
                            entity.getBoundingBoxExtends().getX(),
                            entity.getBoundingBoxExtends().getY());
                    if (length < f && length < smallestDistance)
                    {
                        smallestDistance = length;
                        result = entity;
                    }
                }
            }
        }

        Log.debug("LineTrace took " + (System.nanoTime() - startTime) / 1000000f + "milliseconds");
        Log.debug("Traced distance " + smallestDistance);
        return result;
    }

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
