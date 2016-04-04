package vine.game;

import vine.game.scene.GameEntity;

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
        return this.entity;
    }

    /**
     * @param holder
     *            the entity this component should be attached to
     */
    public void attachTo(final GameEntity holder) {
        this.entity = holder;
    }

}
