package vine.game.scene;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Steffen
 *
 */
public class Chunk
{
    boolean                               active;
    /**
     * Entities of the chunk.
     */
    private Collection<GameEntity>        entities       = new ArrayList<>();
    private final Map<String, GameEntity> entitiesMapped = new ConcurrentHashMap<>();

    public boolean isActive()
    {
        return this.active;
    }

    public void add(GameEntity entity)
    {
        this.entitiesMapped.put(entity.getName(), entity);
        this.entities = this.entitiesMapped.values();
    }

    public void remove(GameEntity entity)
    {
        this.entitiesMapped.remove(entity.getName());
        this.entities = this.entitiesMapped.values();
    }

    public Collection<GameEntity> getEntities()
    {
        return this.entities;
    }
}
