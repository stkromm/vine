package vine.game.scene;

/**
 * @author Steffen
 *
 */
public abstract class Component
{
    protected GameEntity entity;

    /**
     * @return The entity this component is attached to
     */
    public final GameEntity getEntity()
    {
        return entity;
    }

    /**
     * @param holder
     *            the entity this component should be attached to
     */
    public void attachTo(final GameEntity holder)
    {
        if (holder != null && (entity == null || !entity.equals(holder)))
        {
            entity = holder;
        }
    }

    public boolean isAttached()
    {
        return entity == null;
    }

    public void detach()
    {
        if (entity != null)
        {
            entity.detachComponent(this);
        }
        entity = null;
    }

    /**
     * This check is faster than checking if the entity contains this component.
     */
    public boolean isAttachedTo(final GameEntity entity)
    {
        if (entity != null && this.entity != null)
        {
            return entity.equals(this.entity);
        }
        return false;
    }

    public abstract void onUpdate(final float delta);

    public abstract void onAttach();

    public abstract void onDetach();

    public abstract void onDeactivation();

    public abstract void onActivation();
}
