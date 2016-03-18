package vine.game.scene;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import vine.application.GameLifecycle;
import vine.game.Component;
import vine.game.GameObject;
import vine.game.Group;
import vine.game.Transform;
import vine.graphics.Sprite;
import vine.math.Vector2f;
import vine.math.geometry.Rectangle;

/**
 * @author Steffen
 *
 */
public class GameEntity extends GameObject { // NOSONAR
    /**
     * 
     */
    protected final Vector2f velocity = new Vector2f(0, 0);
    /**
     * 
     */
    protected final Vector2f position = new Vector2f(0, 0);
    protected float zOrder = 0.2f;
    protected final Rectangle boundingBox = new Rectangle(position.getX(), position.getY(), 1, 1);
    protected Scene scene;
    private Sprite sprite;

    private Group group;
    private final List<String> tags = new ArrayList<>();
    private Chunk currentChunk;
    private int chunkX = -1;
    private int chunkY = -1;
    private int numberOfChunks = 10;

    float time = 0;
    /**
     * 
     */
    protected final Transform transform = new Transform();
    /**
     * 
     */
    private final List<Component> components = new ArrayList<>(5);

    /**
     * @return
     */
    public float getX() {
        return position.getX();
    }

    /**
     * @return
     */
    public float getY() {
        return position.getY();
    }

    /**
     * @return
     */
    public float getZ() {
        return zOrder;
    }

    /**
     * @return The group this entity is a member of
     */
    public final Group getGroup() {
        return group;
    }

    /**
     * @param group
     *            Sets the group of this entity
     */
    public final void setGroup(final Group group) {
        this.group = group;
    }

    @Override
    public void update(final float delta) {
        super.update(delta);

    }

    public void updatePhysics(final float delta) {
        move(velocity.getX() * delta, velocity.getY() * delta);
    }

    private final void move(final float x, final float y) {
        if (Math.abs(x) > 0.00000001f || Math.abs(y) > 0.00000001f) {
            position.add(x, y);
            updateActiveChunk((int) position.getX() / 1400, (int) position.getY() / 800);
        }
    }

    private final void updateActiveChunk(int x, int y) {
        if (chunkX != x || chunkY != y) {
            x = numberOfChunks <= x ? numberOfChunks - 1 : x;
            y = numberOfChunks <= y ? numberOfChunks - 1 : y;
            x = x < 0 ? 0 : x;
            y = y < 0 ? 0 : y;
            currentChunk.entities.remove(this);
            chunkX = x;
            chunkY = y;
            currentChunk = scene.getChunk(x, y);
            currentChunk.entities.put(this.getName(), this);
        }
    }

    @Override
    public void begin() {
        components.stream().forEach(c -> c.begin());
    }

    /**
     * @param tag
     *            The tag that is looked for
     * @return true, if this entity contains the given tag
     */
    public final boolean containsTag(final String tag) {
        return tags.contains(tag);
    }

    /**
     * @param tag
     *            The tag that should be added to this object
     */
    public final void addTag(final String tag) {
        tags.add(tag);
    }

    /**
     * @param tag
     *            The tag should be removed from this object
     */
    public final void removeTag(final String tag) {
        tags.remove(tag);
    }

    /**
     * @param type
     *            The type by which components are searched
     * @return all components found of this type
     */
    @SuppressWarnings("unchecked")
    public <T extends Component> List<T> getComponents(final Class<T> type) {
        List<T> comps = new ArrayList<>();
        for (Component component : components) {
            if (component.getClass().equals(type)) {
                comps.add((T) component);
            }
        }
        return comps;
        // return (T[]) components.stream().filter(c -> c.getClass() ==
        // type).toArray();
    }

    /**
     * @param component
     *            The component, that should be attached to this entity
     */
    public final void addComponent(final Component component) {
        removeComponent(component);
        if (component instanceof Sprite) {
            sprite = (Sprite) component;
        }
        components.add(component);
        component.attachTo(this);
    }

    /**
     * @param component
     *            The component that should be removed from this entity
     */
    public final void removeComponent(final Component component) {
        components.removeIf(c -> c.getName().equals(component.getName()));
    }

    /**
     * @param type
     *            The type of searched component
     * @return the reference to a component, that is of the given type or null
     *         if there is non
     */
    public final <T extends Component> Optional<T> getComponent(final Class<T> type) {
        return (Optional<T>) components.stream().filter(c -> c.getClass() == type).findFirst();
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void addToScene(final Scene scene) {
        this.scene = scene;
        int xx = (int) position.getX() / 1400;
        int yy = (int) position.getY() / 800;
        xx = numberOfChunks <= xx ? numberOfChunks - 1 : xx;
        yy = numberOfChunks <= yy ? numberOfChunks - 1 : yy;
        xx = position.getX() < 0 ? 0 : xx;
        yy = position.getY() < 0 ? 0 : yy;
        if (currentChunk != null) {
            currentChunk.entities.remove(this);
        }
        chunkX = xx;
        chunkY = yy;
        currentChunk = scene.getChunk(xx, yy);
        currentChunk.entities.put(this.getName(), this);
    }

    /**
     * @param x
     * @param y
     */
    public void construct(final int x, final int y) {
        this.position.add(x, y);
        this.velocity.setX(16 * (float) Math.random());
        this.velocity.setY(16 * (float) Math.random());
    }
}