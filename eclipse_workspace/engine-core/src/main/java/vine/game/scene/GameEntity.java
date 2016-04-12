package vine.game.scene;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import vine.game.Component;
import vine.game.GameObject;
import vine.game.collision.BoxCollision;
import vine.gameplay.component.AnimatedSprite;
import vine.graphics.Color;
import vine.graphics.Sprite;
import vine.math.Vector2f;
import vine.tilemap.UniformTileMap;

/**
 * @author Steffen
 *
 */
public class GameEntity extends GameObject {
    private final List<String> tags = new ArrayList<>();
    private final Vector2f position = new Vector2f(32, 32);
    private final float zOrder = 0.2f;
    /**
     * The scene, that contains this entity.
     */
    protected Scene scene;
    private final Color color = new Color(0, 0, 0, 0);
    private Sprite sprite;

    private Chunk currentChunk;
    private int chunkX = -1;
    private int chunkY = -1;
    private final int numberOfChunks = 10;

    private final Vector2f move = new Vector2f(0, 0);
    /**
     * The current velocity of the entity.
     */
    protected final Vector2f velocity = new Vector2f(0, 0);

    /**
     * Does this entity check for collision when moving.
     */
    protected boolean collisionEnabled = true;
    /**
     * Does this entity get blocked by dynamic entities.
     */
    protected boolean blockDynamic = false;
    /**
     * Does this entity get blocked by static objects.
     */
    protected boolean blockStatic = true;
    /**
     * The extends of the collision box. Origin is the world space position of
     * this entity.
     */
    protected final Vector2f boundingBoxExtends = new Vector2f(24, 31.9f);
    /*
     * Cache for Collision.
     */
    private GameEntity lastCollidedEntity = this;
    public GameEntity currentCollidedEntity = null;
    /**
     * 
     */
    private final List<Component> components = new ArrayList<>(5);

    /**
     * @return The x coord
     */
    public final float getXCoord() {
        return this.position.getX();
    }

    /**
     * @return The y coord
     */
    public final float getYCoord() {
        return this.position.getY();
    }

    /**
     * @return The z order of this entity
     */
    public final float getZOrder() {
        return this.zOrder;
    }

    /**
     * @return The scene that contains this entity
     */
    public Scene getScene() {
        return this.scene;
    }

    @Override
    public void onUpdate(final float delta) {
        this.move(this.velocity.getX() * delta / 1000, this.velocity.getY() * delta / 1000);
        this.setCurrentChunk((int) this.position.getX() / 1400, (int) this.position.getY() / 800);
    }

    private final boolean intersect(GameEntity e) {
        return e.collisionEnabled //
                && BoxCollision.collide(this.position, this.boundingBoxExtends, e.position, e.boundingBoxExtends)//
                && e != this;
    }

    private final boolean intersect(UniformTileMap map) {
        return (this.position.getY() + this.boundingBoxExtends.getY()) / map.getTileHeight() >= map.getHeight()//
                || (this.position.getX() + this.boundingBoxExtends.getX()) / map.getTileWidth() >= map.getWidth()//
                || this.position.getY() < 0//
                || this.position.getX() < 0//
                || map.getTile((int) (this.position.getX() + this.boundingBoxExtends.getX()) / map.getTileWidth(),
                        (int) (this.position.getY() + this.boundingBoxExtends.getY()) / map.getTileHeight()).getClass()
                        .equals(AnimatedSprite.class)
                || map.getTile((int) (this.position.getX() / map.getTileWidth()),
                        (int) (this.position.getY() / map.getTileHeight())).getClass().equals(AnimatedSprite.class)
                || map.getTile((int) (this.position.getX() / map.getTileWidth()),
                        (int) (this.position.getY() + this.boundingBoxExtends.getY()) / map.getTileHeight()).getClass()
                        .equals(AnimatedSprite.class)
                || map.getTile((int) (this.position.getX() + this.boundingBoxExtends.getX()) / map.getTileWidth(),
                        (int) (this.position.getY() / map.getTileHeight())).getClass().equals(AnimatedSprite.class);

    }

    /**
     * @param x
     *            x distance to move
     * @param y
     *            y distance to move
     * @return true, if the entity has moved the given distance
     */
    public final boolean move(float x, float y) {
        if (Math.abs(x) > 0.0001f || Math.abs(y) > 0.0001f) {
            this.move.setX(x);
            this.move.setY(y);
            if (this.move.length() >= this.boundingBoxExtends.length()) {
                return this.move(x / 2, y / 2) && this.move(x / 2, y / 2);
            }
            this.position.add(x, y);
            if (this.collisionEnabled) {
                if (this.blockStatic) {
                    if (this.intersect(this.scene.getMap())) {
                        this.position.add(-x, -y);
                        boolean result = false;
                        if (y != 0) {
                            result = this.move(x / 2, 0);
                        }
                        if (x != 0) {
                            return result || this.move(0, y / 2);
                        }
                        this.currentCollidedEntity = null;
                        return false;
                    }
                }
                if (this.blockDynamic) {
                    if (this.lastCollidedEntity.isDestroyed()) {
                        this.lastCollidedEntity = this;
                    } else if (this.intersect(this.lastCollidedEntity)) {
                        this.position.add(-x, -y);
                        boolean result = false;
                        if (y != 0) {
                            result = this.move(x / 2, 0);
                        }
                        if (x != 0) {
                            return result || this.move(0, y / 2);
                        }
                    }
                    for (final GameEntity e : this.currentChunk.entities.values()) {
                        if (this.intersect(e)) {
                            this.lastCollidedEntity = e;
                            this.currentCollidedEntity = e;
                            this.position.add(-x, -y);
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        return false;

    }

    public Color getColor() {
        return this.color;
    }

    /**
     * @return The size of the collision box
     */
    public final Vector2f getBoundingBoxExtends() {
        return this.boundingBoxExtends;
    }

    /**
     * @return Returns the sprite of the entity.
     */
    public final Sprite getSprite() {
        return this.sprite;
    }

    /**
     * @param tag
     *            The tag that is looked for
     * @return true, if this entity contains the given tag
     */
    public final boolean containsTag(final String tag) {
        return this.tags.contains(tag);
    }

    /**
     * @param tag
     *            The tag that should be added to this object
     */
    public final void addTag(final String tag) {
        this.tags.add(tag);
    }

    /**
     * @param tag
     *            The tag should be removed from this object
     */
    public final void removeTag(final String tag) {
        this.tags.remove(tag);
    }

    /**
     * @param type
     *            The type by which components are searched
     * @return all components found of this type
     */
    @SuppressWarnings("unchecked")
    public <T extends Component> List<T> getComponents(final Class<T> type) {
        final List<T> comps = new ArrayList<>();
        for (final Component component : this.components) {
            if (component.getClass().equals(type)) {
                comps.add((T) component);
            }
        }
        return comps;
    }

    /**
     * @param component
     *            The component, that should be attached to this entity
     */
    public final void addComponent(final Component component) {
        this.removeComponent(component);
        if (component instanceof Sprite) {
            this.sprite = (Sprite) component;
        }
        this.components.add(component);
        component.attachTo(this);
    }

    /**
     * @param component
     *            The component that should be removed from this entity
     */
    public final void removeComponent(final Component component) {
        this.components.removeIf(c -> c.getName().equals(component.getName()));
    }

    /**
     * @param type
     *            The type of searched component
     * @return the reference to a component, that is of the given type or null
     *         if there is non
     */
    @SuppressWarnings("unchecked")
    public final <T extends Component> Optional<T> getComponent(final Class<T> type) {
        return (Optional<T>) this.components.stream().filter(c -> c.getClass() == type).findFirst();
    }

    void addToScene(final Scene scene) {
        this.scene = scene;
        this.setCurrentChunk((int) this.position.getX() / 1400, (int) this.position.getY() / 800);
    }

    private final void setCurrentChunk(final int x, final int y) {
        if (this.chunkX != x || this.chunkY != y) {
            if (this.currentChunk != null) {
                this.currentChunk.entities.remove(this);
            }
            this.chunkX = Math.max(0, Math.min(this.numberOfChunks - 1, x));
            this.chunkY = Math.max(0, Math.min(this.numberOfChunks - 1, y));
            this.currentChunk = this.scene.getChunk(this.chunkX, this.chunkY);
            this.currentChunk.entities.put(this.getName(), this);
        }
    }

    public <T extends GameEntity> T spawn(Class<T> type, float x, float y, boolean spawnIfBlocked) {
        return null;
    }

    /**
     * @param x
     *            The x spawn position
     * @param y
     *            The y spawn position
     */
    public void construct(final int x, final int y) {
        this.position.add(x, y);
        this.velocity.add(32 * (float) Math.random(), 32 * (float) Math.random());
    }

    @Override
    protected void onDestroy() {
        this.currentChunk.entities.remove(this.getName());
    }
}
