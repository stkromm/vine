package vine.game.scene;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Steffen
 *
 */
public class Chunk {
    /**
     * Entities of the chunk.
     */
    protected Map<String, GameEntity> entities = new ConcurrentHashMap<>();
}
