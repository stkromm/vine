package vine.game;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import vine.event.KeyEvent;
import vine.event.MouseButtonEvent;
import vine.event.MouseMoveEvent;
import vine.event.ScrollEvent;
import vine.event.SensorChangeEvent;
import vine.util.reflection.VineClass;
import vine.util.reflection.VineMethodUtils;
import vine.util.time.TimerManager;

/**
 * Base game class. Every gameplay class inherits this. Classes, that inherit
 * this class can be displayed in the editor.
 * 
 * @author Steffen
 *
 */
public abstract class GameObject
{
    private final Set<GameObjectCallback> onDestroyCallbacks = new HashSet<>();
    static final String                   UPDATE_METHOD      = "onUpdate";
    static final String                   CONSTRUCT_METHOD   = "construct";
    /**
     * If this flag is set, the update method of this gameobject gets executed.
     */
    private static final byte             ACTIVE_FLAG        = 2;
    private static final byte             DESTROYED_FLAG     = 4;
    private static final byte             PERSISTENCE_FLAG   = 8;
    private String                        name;
    private byte                          flags              = GameObject.ACTIVE_FLAG;

    /**
    *
    */
    @FunctionalInterface
    public interface GameObjectCallback
    {
        /**
         * @param gameObject
         *            The gameobject, that states callback changed.
         */
        void changedState(GameObject gameObject);
    }

    /**
     * @param name
     *            The name of the gameobject. Id in the game.
     */
    void setName(final String name)
    {
        this.name = name;
    }

    /**
     * @return The name identifier of this object. The name is the unique
     *         identifier to get a gameobject throughout the game. As said, the
     *         name has to be unique. If you try to create an object, that
     *         correspond to the name of an existing object, the instantiation
     *         fails.
     */
    public final String getName()
    {
        return this.name;
    }

    /**
     * @param flags
     *            The flags that should be enabled for this gameobject.
     */
    protected final void enableFlags(final byte newFlags)
    {
        if (!this.isDestroyed())
        {
            this.flags |= newFlags;
        }
    }

    /**
     * Sets the given flags and disables them for this gameobject.
     * 
     * @param flags
     *            The flags, that should be disabled for this gameobject.
     */
    protected final void disableFlags(final byte newFlags)
    {
        if (!this.isDestroyed())
        {
            this.flags &= ~newFlags;
        }
    }

    public final void activate()
    {
        this.enableFlags(GameObject.ACTIVE_FLAG);
    }

    public final void deactivate()
    {
        this.disableFlags(GameObject.ACTIVE_FLAG);
    }

    public void wait(final float seconds)
    {
        this.disableFlags(GameObject.ACTIVE_FLAG);
        TimerManager.get().createTimer(seconds, 1, () -> this.enableFlags(GameObject.ACTIVE_FLAG));
    }

    public boolean isActive()
    {
        return (this.flags & GameObject.ACTIVE_FLAG) == GameObject.ACTIVE_FLAG;
    }

    /**
     * @return True, if the corresponding flag is set.
     */
    public final boolean isDestroyed()
    {
        return (this.flags & GameObject.DESTROYED_FLAG) == GameObject.DESTROYED_FLAG;
    }

    /**
     * @return True, if the corresponding flag is set.
     */
    public boolean isLevelPersistent()
    {
        return (this.flags & GameObject.PERSISTENCE_FLAG) == GameObject.PERSISTENCE_FLAG;
    }

    public void makePersistent()
    {
        this.enableFlags(GameObject.PERSISTENCE_FLAG);
    }

    public void makeTemporary()
    {
        this.disableFlags(GameObject.PERSISTENCE_FLAG);
    }

    /**
     * @param callback
     *            Callback for destruction
     */
    public final void registerDestructionCallback(final GameObjectCallback callback)
    {
        if (!this.isDestroyed() && callback != null)
        {
            this.onDestroyCallbacks.add(callback);
        }
    }

    /**
     * @param callback
     *            Callback for destruction
     */
    public final void unregisterDestructionCallback(final GameObjectCallback callback)
    {
        if (!this.isDestroyed() && callback != null)
        {
            this.onDestroyCallbacks.remove(callback);
        }
    }

    /**
     * @param delta
     *            Time that passed since the last update call.
     */
    public final void update(final float delta)
    {
        if ((this.flags & GameObject.ACTIVE_FLAG) != GameObject.ACTIVE_FLAG
                || (this.flags & GameObject.DESTROYED_FLAG) == GameObject.DESTROYED_FLAG)
        {
            return;
        }
        this.onUpdate(delta);
    }

    public void onUpdate(final float delta)
    {
        //
    }

    /**
     * 
     */
    public void begin()
    {
        // Doesn't need to be overridden
    }

    protected void onDestroy()
    {
        //
    }

    public void construct()
    {
        //
    }

    /**
     * Use this method to destroy a GameObject that you don't need anymore. If
     * you don't use it, the GameObject remains in memory, causing a memory
     * leak.
     */
    public final void destroy()
    {
        this.enableFlags(GameObject.DESTROYED_FLAG);
        this.onDestroy();
        synchronized (this)
        {
            for (final GameObjectCallback callback : this.onDestroyCallbacks)
            {
                callback.changedState(this);
            }
            // Remove the hardreference of the gameobject
            ReferenceManager.OBJECTS.remove(this.name);
        }
    }

    /**
     * @param event
     *            the event to react to
     * @return true if the event is consumned
     */
    public boolean onKeyEvent(final KeyEvent event)
    {
        return false;
    }

    /**
     * @param keyEvent
     *            The key event reacted to
     * @return true, if the event is consumed by this handler
     */
    public boolean onMouseButtonEvent(final MouseButtonEvent mouseButtonEvent)
    {
        return false;
    }

    public boolean onMouseMoveEvent(final MouseMoveEvent mouseMoveEvent)
    {
        return false;
    }

    public boolean onScrollEvent(final ScrollEvent scrollEvent)
    {
        return false;
    }

    public boolean onSensorEvent(final SensorChangeEvent sensorEvent)
    {
        return false;
    }

    @Override
    public boolean equals(final Object object)
    {
        return object != null && object instanceof GameObject ? //
                this.name.equals(((GameObject) object).getName())//
                : //
                false;
    }

    @Override
    public int hashCode()
    {
        return this.name.hashCode();
    }

    /**
     * @author Steffen
     *
     */
    static class ReferenceManager
    {
        /**
         * 
         */
        protected static final String                  ID_QUALIFIER = "?id";

        private static long                            nameGenCount;
        /**
         * All independent gameobjects that are currently in the game.
         */
        protected static final Map<String, GameObject> OBJECTS      = new WeakHashMap<>(1000);

        private ReferenceManager()
        {
        }

        /**
         * @param type
         *            The type of the instantiate gameobject.
         * @return A valid gameobject name id.
         */
        protected static final synchronized <T extends GameObject> String generateObjectName(final Class<T> type)
        {
            final String name = type.getName() + ReferenceManager.ID_QUALIFIER + ReferenceManager.nameGenCount;
            ReferenceManager.nameGenCount++;
            return name;
        }

        /**
         * @param name
         *            The name used to identify the object in the game.
         * @param type
         *            The type of the instantiate gameobject.
         * @param params
         *            The optional arguments of the construct method of the
         *            instantiated type.
         * @return The instantated gameobject or null on failure
         */
        protected static final <T extends GameObject> T instantiate(
                final World world,
                final Class<T> type,
                final String name,
                final Object... params)
        {
            final VineClass<T> objectClass = new VineClass<>(type);
            final T object = objectClass.instantiate();
            if (object != null)
            {
                object.setName(name);
                ReferenceManager.OBJECTS.put(name, object);
                objectClass.getMethodByName(GameObject.CONSTRUCT_METHOD)
                        .ifPresent(method -> VineMethodUtils.invokeMethodOn(method, object, params));
                if (objectClass.hasMethodImplemented(GameObject.UPDATE_METHOD, float.class))
                {
                    world.addObject(object);
                }
            }
            return object;
        }

        /**
         * @param name
         *            The name of a potential gameObject
         * @return True, if a gameObject can be instantiate with the given name
         */
        static boolean isValidGameObjectName(final String name)
        {
            return name != null && !name.contains(GameObject.ReferenceManager.ID_QUALIFIER)
                    && !ReferenceManager.OBJECTS.containsKey(name);
        }
    }

}
