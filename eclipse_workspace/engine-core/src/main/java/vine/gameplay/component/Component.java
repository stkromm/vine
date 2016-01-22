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
    public GameEntity getEntity() {
        return entity;
    }

    /**
     * @param entity
     *            the entity this component should be attached to
     */
    public void attachTo(GameEntity entity) {
        this.entity = entity;
    }

    public float getX() {
        return this.entity.getX();
    }

    public float getY() {
        return this.entity.getY();
    }

    public float getZ() {
        return this.entity.getZ();
    }
}