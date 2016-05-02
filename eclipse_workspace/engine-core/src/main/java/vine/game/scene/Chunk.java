package vine.game.scene;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import vine.math.VineMath;

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
        this.tileWidth = this.width / divisionX;
        this.tileHeight = this.height / divisionY;
        this.spatialGraph = new ArrayList<>(divisionX * divisionY);
        for (int j = 0; j < divisionY; j++)
        {
            for (int i = 0; i < divisionX; i++)
            {

                this.spatialGraph.add(new ArrayList<>(4));
            }
        }
    }

    public List<GameEntity> getSubdivisonChunk(final float posX, final float posY)
    {
        if (!this.isActive)
        {
            createSubdivisionGraph();

        }
        final int x = (int) (posX % this.width);
        final int y = (int) (posY % this.height);
        final int index = VineMath
                .clamp(x / this.tileWidth + y / this.tileHeight * this.divisionX, 0, this.divisionX * this.divisionY);
        return this.spatialGraph.get(index);
    }

    public void createSubdivisionGraph()
    {
        this.isActive = true;
        for (final List<GameEntity> set : this.spatialGraph)
        {
            set.clear();
        }
        for (final GameEntity entity : this.entities)
        {
            final int x = Math.round(entity.getBoundingBoxExtends().getX()) / this.tileWidth;
            final int y = Math.round(entity.getBoundingBoxExtends().getX()) / this.tileHeight;
            for (int i = x + 1; i >= 0; i--)
            {
                for (int j = y + 1; j >= 0; j--)
                {
                    getSubdivisonChunk(
                            entity.getXPosition() + i * this.tileWidth,
                            entity.getYPosition() + j * this.tileHeight).add(entity);
                }
            }
        }
    }

    public void add(final GameEntity entity)
    {
        this.entitiesMapped.put(entity.getName(), entity);
        this.entities = this.entitiesMapped.values();
    }

    public void remove(final GameEntity entity)
    {
        this.entitiesMapped.remove(entity.getName());
        this.entities = this.entitiesMapped.values();
    }

    public Collection<GameEntity> getEntities()
    {
        return this.entities;
    }

    public int getTileHeight()
    {
        return this.tileHeight;
    }

    public int getTileWidth()
    {
        return this.tileWidth;
    }
}
