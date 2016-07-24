package vine.game.scene;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;

import vine.event.EventListener;
import vine.game.World;
import vine.game.tilemap.TileMapSceneProxy;
import vine.game.tilemap.UniformTileMap;
import vine.gameplay.PlayerPawn;
import vine.math.vector.Vec2f;

/**
 * @author Steffen
 *
 */
public class Scene
{
    private final EventListener   listener = new EventListener();
    private final Chunk[][]       chunks;
    private final Set<GameEntity> entities = new HashSet<>();
    private TileMapSceneProxy         map;
    private World                 world;
    private final SceneTracer     tracer;
    private final CameraManager   cameras  = new CameraManager();

    public Scene()
    {
        tracer = new SceneTracer(this);
        chunks = new Chunk[10][10];
        initializeChunks();
    }

    private void initializeChunks()
    {
        for (int i = 0; i < 10; i++)
        {
            for (int j = 0; j < 10; j++)
            {
                chunks[i][j] = new Chunk(1400, 700, 20, 14);
            }
        }
    }

    public World getWorld()
    {
        return world;
    }

    public void setWorld(final World world)
    {
        this.world = world;
    }

    public CameraManager getCameras()
    {
        return cameras;
    }

    protected Chunk[][] getChunks()
    {
        return chunks;
    }

    public void prepareUpdate()
    {
        for (final Chunk[] chunkArray : chunks)
        {
            for (final Chunk chunk : chunkArray)
            {
                chunk.isActive = false;
            }
        }
    }

    /**
     * @return All entities that are rendered by this layer
     */
    public Iterable<GameEntity> getEntities()
    {
        return entities;
    }

    public boolean removeEntity(final GameEntity entity)
    {
        return entities.remove(entity);
    }

    public <T extends GameEntity> WeakReference<T> spawn(
            final Class<T> type,
            final float x,
            final float y,
            final boolean spawnIfBlocked,
            final Object... e)
    {
        if (!spawnIfBlocked)
        {
            final boolean blocked = false;
            if (blocked)
            {
                return null;
            }
        }
        final GameEntity entity = getWorld().instantiate(type, e).get();
        entity.setPosition(x, y);
        addEntity(entity);
        return new WeakReference<>(type.cast(entity));
    }

    /**
     * @param i
     *            The x coordinate in multiples of the chunk width
     * @param j
     *            The y coordinate in multiples of the chunk height
     * @return The chunk corresponding to the given indices.
     */
    public Chunk getChunk(final int i, final int j)
    {
        return chunks[i][j];
    }

    /**
     * Adds the given entity to this layer.
     * 
     * @param entity
     *            Entity that will be rendered with this layer.
     */
    public final void addEntity(final GameEntity entity)
    {
        if (!entities.contains(entity))
        {
            entities.add(entity);
            entity.registerDestructionCallback(e -> entities.remove(e));
            entity.setScene(this);
        }
    }

    /**
     * @return The map that is currently used in the scene.
     */
    public final UniformTileMap getMap()
    {
        return map.getMap();
    }

    public void addMap(final TileMapSceneProxy map)
    {
        this.map = map;
    }

    public Class<? extends GameEntity> getPlayerPawnClass()
    {
        return PlayerPawn.class;
    }

    public Vec2f getStartPoint()
    {
        return new Vec2f(100, 100);
    }

    public SceneTracer getTracer()
    {
        return tracer;
    }

    public EventListener getListener()
    {
        return listener;
    }
}
