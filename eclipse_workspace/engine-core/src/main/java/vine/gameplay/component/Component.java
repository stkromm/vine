package vine.gameplay.component;

import vine.game.GameObject;
import vine.gameplay.entity.GameEntity;

/**
 * @author Steffen
 *
 */
public class Component extends GameObject { // NOSONAR

    /**
     * 
     */
    protected GameEntity entity;

    /**
     * @return The entity this component is attached to
     */
    public final GameEntity getEntity() {
        return entity;
    }

    /**
     * @param entity
     *            the entity this component should be attached to
     */
    public void attachTo(final GameEntity entity) {
        this.entity = entity;
    }

    /**
     * @return Current x position of the entity.
     */
    public float getX() {
        return this.entity.getX();
    }

    /**
     * @return Current y position of the entity.
     */
    public float getY() {
        return this.entity.getY();
    }

    /**
     * @return Current z position of the entity.
     */
    public float getZ() {
        return this.entity.getZ();
    }
}
