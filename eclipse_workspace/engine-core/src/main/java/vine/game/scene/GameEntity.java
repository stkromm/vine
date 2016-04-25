package vine.game.scene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import vine.game.GameObject;
import vine.game.World;
import vine.graphics.Color;
import vine.graphics.renderer.SpriteBatch;
import vine.math.MutableVec2f;
import vine.math.Vec2f;
import vine.math.VineMath;
import vine.util.time.TimerManager;

/**
 * @author Steffen
 *
 */
public class GameEntity extends GameObject
{
    // Scene
    /**
     * The scene that contains this entity.
     */
    private Scene                          scene;
    /**
     * The gameplay tags of this entity. It's faster to search after a amount of
     * entity that share the same tag than search after objects of the same
     * class.
     */
    private final List<String>             tags               = new ArrayList<>();
    /**
     * The components of this entity. The components dynamically extend the
     * functionality of this entity.
     */
    private final Set<Component>           components         = new HashSet<>(5);
    private final Map<Class<?>, Component> componentCache     = new HashMap<>(10);
    /**
     * The lifetime of this entity. This value is literally the time the entity
     * will live.
     */
    private float                          lifetime           = GameEntity.LIVE_FOREVER;
    /**
     * Lifetime constant that signals that this entity lives forever.
     */
    public static final float              LIVE_FOREVER       = -1;
    /**
     * The Chunk that the entity exists in.
     */
    private Chunk                          currentChunk;
    private int                            chunkX             = -1;
    private int                            chunkY             = -1;
    private final int                      numberOfChunks     = 10;

    // Position
    /**
     * The world position of this entity. Unit is World-Units.
     */
    private final MutableVec2f             position           = new MutableVec2f(32, 32);
    /**
     * The z order of this entity. A higher value means it will be rendered on
     * top of entity with a lower value.
     */
    private float                          zPosition          = 0.2f;
    /**
     * The extends of the collision box. Origin is the world space position of
     * this entity.
     */
    private final MutableVec2f             boundingBoxExtends = new MutableVec2f(24, 31.9f);

    // Appearance
    private final Color                    color              = new Color(0, 0, 0, 0);

    // Physics
    /**
     * The speed of this entity. Unit is World-Units / second.
     */
    private final MutableVec2f             speed              = new MutableVec2f(0, 0);
    /**
     * The acceleration of this entity. Unit is Speed / second.
     */
    private final MutableVec2f             acceleration       = new MutableVec2f(1, 1);
    // Collision Handling
    private final boolean                  moveable           = true;

    public final boolean isMoveable()
    {
        return this.moveable;
    }

    /**
     * @return The x Coordinate in Worldspace of this entity
     */
    public final float getXPosition()
    {
        return this.position.getX();
    }

    /**
     * @return The y Coordinate in Worldspace of this entity
     */
    public final float getYPosition()
    {
        return this.position.getY();
    }

    /**
     * @return The z order of this entity
     */
    public final float getZPosition()
    {
        return this.zPosition;
    }

    public final Vec2f getPosition()
    {
        return this.position;
    }

    public final void addPosition(final float x, final float y)
    {
        this.position.add(x, y);
    }

    public final Chunk getChunk()
    {
        return this.currentChunk;
    }

    /**
     * Sets the position of this entity in Worldspace Coordinates.
     */
    public final void setPosition(final float x, final float y)
    {
        this.position.setX(x);
        this.position.setY(y);
    }

    public void setZ(final float z)
    {
        this.zPosition = z;
    }

    public final float getXSpeed()
    {
        return this.speed.getX();
    }

    public final float getYSpeed()
    {
        return this.speed.getY();
    }

    public final Vec2f getSpeed()
    {
        return this.speed;
    }

    public final void setSpeed(final float x, final float y)
    {
        this.speed.setX(x);
        this.speed.setY(y);
    }

    public final void setSpeedX(final float x)
    {
        this.speed.setX(x);
    }

    public final void setSpeedY(final float y)
    {
        this.speed.setY(y);
    }

    public final void addSpeed(final float x, final float y)
    {
        this.speed.add(x, y);
    }

    protected final Vec2f getAcceleration()
    {
        return this.acceleration;
    }

    public final float getAccelerationX()
    {
        return this.acceleration.getX();
    }

    public final float getAccelerationY()
    {
        return this.acceleration.getY();
    }

    public final void addAcceleration(final float x, final float y)
    {
        this.acceleration.setX(this.getAccelerationX() + x);
        this.acceleration.setY(this.getAccelerationY() + y);
    }

    public final void setAcceleration(final float x, final float y)
    {
        this.acceleration.setX(x);
        this.acceleration.setY(y);
    }

    // Color
    public final Color getColor()
    {
        return this.color;
    }

    public final void dye(final Color color)
    {
        this.color.setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public final void flash(final Color color, final float duration, final boolean smooth)
    {
        if (smooth)
        {
            // Timer with callback that uses elapsed time to transition to
            // origin color
        } else
        {
            final Color originColor = this.color;
            TimerManager.get().createTimer(duration, 1, () -> this.dye(originColor));
            this.dye(color);
        }
    }

    public final void addTransparency(final float percent)
    {
        this.color.addTransparency(Math.round(percent * 256));
    }

    public final void setVisible()
    {
        this.color.setColor(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), 0);
    }

    public final void setInvisible()
    {
        this.color.setColor(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), 1);
    }

    /**
     * @return The size of the collision box
     */
    public final Vec2f getBoundingBoxExtends()
    {
        return this.boundingBoxExtends;
    }

    public final float getXExtends()
    {
        return this.boundingBoxExtends.getX();
    }

    public final float getYExtends()
    {
        return this.boundingBoxExtends.getY();
    }

    public final void setBoundingBox(final float x, final float y)
    {
        this.boundingBoxExtends.setX(x);
        this.boundingBoxExtends.setY(y);
    }

    /**
     * @return The scene that contains this entity
     */
    public final Scene getScene()
    {
        return this.scene;
    }

    /**
     * Adds this entity to the given scene.
     */
    final void setScene(final Scene scene)
    {
        if (this.scene != null)
        {
            if (this.scene.equals(scene))
            {
                return;
            }
            scene.getEntities().remove(this);
        }
        this.scene = scene;
        this.setCurrentChunk();
    }

    public final World getWorld()
    {
        return this.getScene().getWorld();
    }

    /**
     * @param tag
     *            The tag that is looked for
     * @return true, if this entity contains the given tag
     */
    public final boolean containsTag(final String tag)
    {
        return tag == null ? false : this.tags.contains(tag);
    }

    /**
     * @param tag
     *            The tag that should be added to this object
     */
    public final void addTag(final String tag)
    {
        if (tag != null && !this.containsTag(tag))
        {
            this.tags.add(tag);
        }
    }

    /**
     * @param tag
     *            The tag should be removed from this object
     */
    public final void removeTag(final String tag)
    {
        if (tag != null)
        {
            this.tags.remove(tag);
        }
    }

    /**
     * @param type
     *            The type by which components are searched
     * @return all components found of this type
     */
    public <T extends Component> List<T> getComponents(final Class<T> type)
    {
        final List<T> components = new ArrayList<>(this.components.size());
        for (final Component component : this.components)
        {
            if (type.isInstance(component))
            {
                components.add(type.cast(component));
            }
        }
        return components;
    }

    /**
     * @param type
     *            The type of searched component
     * @return the reference to a component, that is of the given type or null
     *         if there is non
     */
    public final <T extends Component> T getComponent(final Class<T> type)
    {
        final Component comp = this.componentCache.get(type);
        if (comp != null)
        {
            return type.cast(comp);
        }
        for (final Component component : this.components)
        {
            if (type.isInstance(component))
            {
                return type.cast(component);
            }
        }
        return null;
    }

    /**
     * Attaches the component to this entity
     */
    public final void attachComponent(final Component component)
    {
        if (component == null || this.isDestroyed())
        {
            return;
        }
        this.components.add(component);
        component.attachTo(this);
        this.componentCache.put(component.getClass(), component);
        component.onAttach();
    }

    /**
     * Detaches the given component from this entity
     */
    public final void detachComponent(final Component component)
    {
        if (component == null)
        {
            return;
        }
        if (this.components.remove(component) && this.componentCache.containsValue(component))
        {
            this.componentCache.remove(component.getClass());
        }
    }

    /**
     * @return True, if the given Component is attached to this entity.
     */
    public final boolean containsComponent(final Component component)
    {
        return this.components.contains(component);
    }

    /**
     * Clears the cache of this entity.
     */
    public final void clearComponentCache()
    {
        this.componentCache.clear();
    }

    public void setCurrentChunk()
    {
        final int x = (int) this.position.getX() / 1400;
        final int y = (int) this.position.getY() / 800;
        if (this.chunkX != x || this.chunkY != y)
        {
            if (this.currentChunk != null)
            {
                this.currentChunk.remove(this);
            }
            this.chunkX = VineMath.clamp(x, 0, this.numberOfChunks - 1);
            this.chunkY = VineMath.clamp(y, 0, this.numberOfChunks - 1);
            this.currentChunk = this.scene.getChunk(this.chunkX, this.chunkY);
            this.currentChunk.add(this);
        }
    }

    public float getLifetime()
    {
        return this.lifetime;
    }

    /**
     * Sets the lifetime of this entity. Only LIVE_FOREVER or values greater
     * than zero are permitted.
     */
    public void setLifetime(final float lifetime)
    {
        if (lifetime >= 0 || lifetime == GameEntity.LIVE_FOREVER)
        {
            this.lifetime = lifetime;
        }
    }

    /**
     * Adds the lifetime to this entity. If the lifetime counts below zero this
     * entity gets destroyed and aging is stopped.
     */
    public final void addLifetime(final float lifetime)
    {
        if (this.lifetime == GameEntity.LIVE_FOREVER)
        {
            return;
        }
        this.lifetime += lifetime;
        if (this.getLifetime() <= 0)
        {
            this.lifetime = GameEntity.LIVE_FOREVER;
            this.destroy();
        }
    }

    public final boolean isAging()
    {
        return this.lifetime != GameEntity.LIVE_FOREVER;
    }

    @Override
    public void wait(final float seconds)
    {
        this.deactivate();
        for (final Component component : this.components)
        {
            component.onDeactivation();
        }
        TimerManager.get().createTimer(seconds, 1, () ->
        {
            this.activate();
            for (final Component component : this.components)
            {
                component.onActivation();
            }
        });
    }

    @Override
    public void onUpdate(final float delta)
    {
        // Lifetime
        if (this.isAging())
        {
            this.addLifetime(-delta);
            if (this.isDestroyed())
            {
                return;
            }
        }

        // Physics
        for (final Component component : this.components)
        {
            component.onUpdatePhysics(delta / 1000);
            component.onUpdate(delta);
        }
    }

    public void onRender(SpriteBatch batcher)
    {
        if (this.isDestroyed() || this.color.getAlpha() > 0.99f)
        {
            return;
        }
        for (final Component component : this.components)
        {
            component.onRender(batcher);
        }
    }

    @Override
    public void construct()
    {
        super.construct();
    }

    @Override
    protected void onDestroy()
    {
        this.currentChunk.remove(this);
        this.scene.getEntities().remove(this);
    }

    @Override
    public String toString()
    {
        return super.toString() + ":Position(" + this.getXPosition() + "," + this.getYPosition() + ")";
    }
}
