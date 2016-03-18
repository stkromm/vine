package vine.game.scene;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Chunk {
    protected Map<String, GameEntity> entities = new ConcurrentHashMap<>();
}
