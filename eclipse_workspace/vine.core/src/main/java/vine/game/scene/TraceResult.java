package vine.game.scene;

import vine.math.HitData;

public class TraceResult
{
    GameEntity entity;
    HitData    hitData;
    float      distance;

    public GameEntity getEntity()
    {
        return entity;
    }

    public void setEntity(final GameEntity entity)
    {
        this.entity = entity;
    }

    public float getDistance()
    {
        return distance;
    }

    public void setDistance(final float distance)
    {
        this.distance = distance;
    }

    public void prepare()
    {
        setDistance(Float.MAX_VALUE);
    }
}
