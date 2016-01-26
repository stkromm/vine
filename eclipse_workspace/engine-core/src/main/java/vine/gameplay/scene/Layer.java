package vine.gameplay.scene;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import vine.event.Event;
import vine.event.EventDispatcher.EventHandler;
import vine.event.EventLayer;
import vine.gameplay.entity.GameEntity;
import vine.graphics.Renderer;

/**
 * Provides a unique rendering method with post processing, global screnn
 * maniuplation for a set of gameobjects. Offers no semantic ordering of
 * gameobjects and is not relevant for physics or audio.
 * 
 * @author Steffen
 * 
 */
public abstract class Layer implements EventLayer {
    /**
     * 
     */
    protected final List<GameEntity> entities;
    /**
     * 
     */
    protected final Renderer renderer;

    /**
     * 
     */
    public final List<EventHandler> handler;

    /**
     * 
     */
    public Layer() {
        entities = new ArrayList<>();
        renderer = new Renderer();
        handler = new ArrayList<>();
    }

    @Override
    public void addEventHandler(final EventHandler handler) {
        this.handler.add(handler);
    }

    @Override
    public boolean onEvent(final Event event) {
        final Optional<EventHandler> opt = handler.stream().filter(h -> h.onEvent(event)).findFirst();
        return opt.isPresent();
    }

    /**
     * @return All entities that are rendered by this layer
     */
    public List<GameEntity> getEntities() {
        return entities;
    }

    /**
     * Adds the given entity to this layer.
     * 
     * @param entity
     *            Entity that will be rendered with this layer.
     */
    public final void add(final GameEntity entity) {
        if (!entities.contains(entity)) {
            entities.add(entity);
        }
    }

    /**
     * Renders all entities assigned to this layer with it renderer.
     */
    public abstract void render();

}
