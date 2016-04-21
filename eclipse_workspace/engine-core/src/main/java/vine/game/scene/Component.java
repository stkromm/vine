package vine.game.scene;

/**
 * @author Steffen
 *
 */
public class Component
{
    /**
     * 
     */
    protected GameEntity entity;

    /**
     * @return The entity this component is attached to
     */
    public final GameEntity getEntity()
    {
        return this.entity;
    }

    /**
     * @param holder
     *            the entity this component should be attached to
     */
    public void attachTo(final GameEntity holder)
    {
        if (holder != null && (this.entity == null || !this.entity.equals(holder)))
        {
            this.entity = holder;
        }
    }

    public boolean isAttached()
    {
        return this.entity == null;
    }

    public void detach()
    {
        if (this.entity != null)
        {
            this.entity.detachComponent(this);
        }
        this.entity = null;
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

    public void updatePhysics(final float delta)
    {
        // override to add logic
    }

    public void onUpdate(final float delta)
    {
        // TODO Auto-generated method stub

    }

    public void onAttach()
    {
        // TODO Auto-generated method stub

    }

    public void onDeactivation()
    {
        // TODO Auto-generated method stub

    }

    public void onActivation()
    {
        //
    }
}
