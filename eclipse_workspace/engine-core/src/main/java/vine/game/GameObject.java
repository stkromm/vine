package vine.game;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import vine.application.GamePlayer;
import vine.event.KeyEvent;
import vine.event.MouseButtonEvent;
import vine.reflection.VineClass;
import vine.reflection.VineMethodUtils;

/**
 * Base game class. Every gameplay class inherits this. Classes, that inherit
 * this class can be displayed in the editor.
 * 
 * @author Steffen
 *
 */
public abstract class GameObject {
    /**
     * @author Steffen
     *
     */
    @FunctionalInterface
    public interface GameObjectDestroyCallback {
        /**
         * @param gameObject
         *            The gameobject, that states callback changed.
         */
        void changedState(GameObject gameObject);
    }

    private Set<GameObjectDestroyCallback> onDestroyCallbacks = new HashSet<>();
    static final String UPDATE_METHOD = "update";
    static final String CONSTRUCT_METHOD = "construct";
    /**
     * If this flag is set, the gameobject gets not rendered.
     */
    public static final byte HIDE_FLAG = 1;
    /**
     * If this flag is set, the update method of this gameobject gets executed.
     */
    public static final byte ACTIVE_FLAG = 2;
    private static final byte DESTROYED_FLAG = 4;
    private static final byte PERSISTENCE_FLAG = 8;

    private String name;
    private byte flags = ACTIVE_FLAG;

    /**
     * @param name
     *            The name of the gameobject. Id in the game.
     */
    protected void setName(final String name) {
        this.name = name;
    }
    /**
     * 
     */

    /**
     * @param flags
     *            The flags that should be enabled for this gameobject.
     */
    private final void enableFlags(final byte newFlags) {
        if (!isDestroyed()) {
            this.flags |= newFlags;
        }
    }

    /**
     * Sets the given flags (that is 1's in the byte) and disables them for this
     * gameobject.
     * 
     * @param flags
     *            The flags, that should be disabled for this gameobject.
     */
    private final void disableFlags(final byte newFlags) {
        final int effectiveFlags = ~newFlags;
        this.flags &= effectiveFlags;
    }

    /**
     * Activates rendering of this gameobject.
     */
    public final void show() {
        disableFlags(HIDE_FLAG);
    }

    /**
     * @return True, if the corresponding flag is set.
     */
    public final boolean isHidden() {
        return (this.flags & HIDE_FLAG) == HIDE_FLAG;
    }

    /**
     * @return True, if the corresponding flag is set.
     */
    public final boolean isDestroyed() {
        return (this.flags & DESTROYED_FLAG) == DESTROYED_FLAG;
    }

    /**
     * @return True, if the corresponding flag is set.
     */
    public boolean isLevelPersistent() {
        return (this.flags & PERSISTENCE_FLAG) == PERSISTENCE_FLAG;
    }

    /**
     * @return The name identifier of this object. The name is the unique
     *         identifier to get a gameobject throughout the game. As said, the
     *         name has to be unique. If you try to create an object, that
     *         correspond to the name of an existing object, the instantiation
     *         fails.
     */
    public final String getName() {
        return this.name;
    }

    /**
     * @param callback
     *            Callback for destruction
     */
    public final void addDestroyCallback(GameObjectDestroyCallback callback) {
        if (!isDestroyed() && callback != null) {
            this.onDestroyCallbacks.add(callback);
        }
    }

    /**
     * @param callback
     *            Callback for destruction
     */
    public final void removeDestroyCallback(GameObjectDestroyCallback callback) {
        if (!isDestroyed() && callback != null) {
            this.onDestroyCallbacks.remove(callback);
        }
    }

    /**
     * @param delta
     *            Time that passed since the last update call.
     */
    public void update(final float delta) {
        if ((this.flags & ACTIVE_FLAG) != ACTIVE_FLAG || (this.flags & DESTROYED_FLAG) != DESTROYED_FLAG) {
            return;
        }
    }

    /**
     * 
     */
    public void begin() {
        // Doesn't need to be overridden
    }

    /**
     * Use this method to destroy a GameObject that you don't need anymore. If
     * you don't use it, the GameObject remains in memory, causing a memory
     * leak.
     */
    public final void destroy() {
        enableFlags(DESTROYED_FLAG);
        onDestroy();
        synchronized (this) {
            for (final GameObjectDestroyCallback callback : this.onDestroyCallbacks) {
                callback.changedState(this);
            }
            // Remove from event listener
            final Game game = GamePlayer.getRunningGame();
            game.removeList.add(this);

            // Remove the hardreference of the gameobject
            ReferenceManager.OBJECTS.remove(this.name);
        }
    }

    /**
     * Method, that is called, when this gameobject gets destroyed. If you
     * override this method, be sure to call the super method at begin. In order
     * to destroy an object, use the destroy method, not this method!
     */
    protected void onDestroy() {
        if (!((this.flags & DESTROYED_FLAG) == DESTROYED_FLAG)) {
            return;
        }
    }

    /**
     * @param event
     *            the event to react to
     * @return true if the event is consumned
     */
    @SuppressWarnings("static-method")
    public boolean onKeyEvent(final KeyEvent event) {
        return false;
    }

    /**
     * @param keyEvent
     *            The key event reacted to
     * @return true, if the event is consumed by this handler
     */
    @SuppressWarnings("static-method")
    public boolean onMouseButtonEvent(final MouseButtonEvent keyEvent) {
        return false;
    }

    /**
     * 
     */
    protected void construct() {
        // Only override to add functionality.
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) {
            return false;
        }
        if (object instanceof GameObject) {
            return this.name.equals(((GameObject) object).getName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    /**
     * @author Steffen
     *
     */
    static class ReferenceManager {
        /**
         * 
         */
        protected static final String ID_QUALIFIER = "?id";

        private static long nameGenCount;
        /**
         * All independent gameobjects that are currently in the game.
         */
        protected static final Map<String, GameObject> OBJECTS = new WeakHashMap<>(1000);

        private ReferenceManager() {
        }

        /**
         * @param type
         *            The type of the instantiate gameobject.
         * @return A valid gameobject name id.
         */
        protected static final synchronized <T extends GameObject> String generateObjectName(final Class<T> type) {
            final String name = type.getName() + ID_QUALIFIER + nameGenCount;
            nameGenCount++;
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
        protected static final <T extends GameObject> T instantiate(final Class<T> type, final String name,
                final Object... params) {
            final VineClass<T> objectClass = new VineClass<>(type);
            final T object = objectClass.instantiateType();
            if (object != null) {
                object.setName(name);
                OBJECTS.put(name, object);
                objectClass.getMethodByName(CONSTRUCT_METHOD)
                        .ifPresent(method -> VineMethodUtils.invokeMethodOn(method, object, params));
                if (objectClass.hasMethodImplemented("update", float.class)) {
                    GamePlayer.getRunningGame().addList.add(object);
                }
            }
            return object;
        }
    }

}
