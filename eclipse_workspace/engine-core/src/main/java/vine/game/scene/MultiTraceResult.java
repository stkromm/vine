package vine.game.scene;

import java.util.ArrayList;
import java.util.List;

public class MultiTraceResult
{
    List<GameEntity> entities = new ArrayList<GameEntity>();
    float            nearestHitDistance;

    public List<GameEntity> getEntities()
    {
        return this.entities;
    }

    public void setEntities(List<GameEntity> entities)
    {
        this.entities = entities;
    }

    public float getNearestHitDistance()
    {
        return this.nearestHitDistance;
    }

    public void setNearestHitDistance(float nearestHitDistance)
    {
        this.nearestHitDistance = nearestHitDistance;
    }

    public void prepare()
    {
        this.entities.clear();
        this.nearestHitDistance = Float.MAX_VALUE;
    }

}
