package vine.game.scene;

import vine.math.vector.MutableVec2f;

/**
 * @author Steffen
 *
 */
public abstract class Component
{
    /**
     * 
     */
    protected GameEntity   entity;
    protected MutableVec2f worldOffset = new MutableVec2f();

    /**
     * @return The entity this component is attached to
     */
    public final GameEntity getEntity()
    {
        return this.entity;
    }

    public void addWorldOffset(final float x, final float y)
    {
        this.worldOffset.set(x, y);
    }

    public void setWorldOffset(final float x, final float y)
    {
        this.worldOffset.set(x, y);
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

    public abstract void onUpdate(final float delta);

    public abstract void onAttach();

    public abstract void onDetach();

    public abstract void onDeactivation();

    public abstract void onActivation();
}
