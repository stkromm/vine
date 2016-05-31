package vine.game.scene;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import vine.math.GMath;

/**
 * @author Steffen
 *
 */
public class Chunk
{
    boolean                               isActive;
    int                                   width;
    int                                   height;
    final int                             tileWidth;
    final int                             tileHeight;
    final int                             divisionX;
    final int                             divisionY;
    final List<List<GameEntity>>          spatialGraph;
    /**
     * Entities of the chunk.
     */
    private Collection<GameEntity>        entities       = new ArrayList<>();
    private final Map<String, GameEntity> entitiesMapped = new ConcurrentHashMap<>();

    public Chunk(final int width, final int height, final int divisionX, final int divisionY)
    {
        this.width = width;
        this.height = height;
        this.divisionX = divisionX;
        this.divisionY = divisionY;
        assert this.width % divisionX == 0
                && this.height % divisionY == 0 : "Chunk can only be divided into even parts";
        tileWidth = this.width / divisionX;
        tileHeight = this.height / divisionY;
        spatialGraph = new ArrayList<>(divisionX * divisionY);
        for (int j = 0; j < divisionY; j++)
        {
            for (int i = 0; i < divisionX; i++)
            {

                spatialGraph.add(new ArrayList<>(4));
            }
        }
    }

    public List<GameEntity> getSubdivisonChunk(final float posX, final float posY)
    {
        if (!isActive)
        {
            createSubdivisionGraph();
        }
        final int x = (int) (posX % width);
        final int y = (int) (posY % height);
        final int index = GMath.clamp(x / tileWidth + y / tileHeight * divisionX, 0, divisionX * divisionY);
        return spatialGraph.get(index);
    }

    public void createSubdivisionGraph()
    {
        isActive = true;
        for (final List<GameEntity> set : spatialGraph)
        {
            set.clear();
        }
        for (final GameEntity entity : entities)
        {
            final int x = Math.round(entity.getBoundingBoxExtends().getX()) / tileWidth;
            final int y = Math.round(entity.getBoundingBoxExtends().getX()) / tileHeight;
            for (int i = x + 1; i >= 0; i--)
            {
                for (int j = y + 1; j >= 0; j--)
                {
                    getSubdivisonChunk(entity.getXPosition() + i * tileWidth, entity.getYPosition() + j * tileHeight)
                            .add(entity);
                }
            }
        }
    }

    public void add(final GameEntity entity)
    {
        entitiesMapped.put(entity.getName(), entity);
        entities = entitiesMapped.values();
    }

    public void remove(final GameEntity entity)
    {
        entitiesMapped.remove(entity.getName());
        entities = entitiesMapped.values();
    }

    public Collection<GameEntity> getEntities()
    {
        return entities;
    }

    public int getTileHeight()
    {
        return tileHeight;
    }

    public int getTileWidth()
    {
        return tileWidth;
    }
}
