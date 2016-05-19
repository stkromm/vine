package vine.game.scene;

public class TraceResult
{
    GameEntity entity;
    float      distance;

    public GameEntity getEntity()
    {
        return this.entity;
    }

    public void setEntity(final GameEntity entity)
    {
        this.entity = entity;
    }

    public float getDistance()
    {
        return this.distance;
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
