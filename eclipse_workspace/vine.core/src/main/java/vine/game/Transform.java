/**
 * 
 */
package vine.game;

import java.util.HashSet;
import java.util.Set;

import vine.math.matrix.Mat3f;
import vine.math.matrix.MutableMat3f;
import vine.math.vector.MutableVec2f;
import vine.math.vector.Vec2f;

/**
 * @author Steffen Kromm, first created on 03.05.2016
 *
 */
public class Transform implements ITransform
{
    private ITransform            parent;
    private final Set<ITransform> children      = new HashSet<>();

    private final MutableMat3f    localToWorld  = new MutableMat3f();
    private final MutableMat3f    worldToLocal  = new MutableMat3f();

    private final MutableVec2f    localPosition = new MutableVec2f();
    private final MutableVec2f    worldPosition = new MutableVec2f();
    private float                 worldRotation;
    private float                 localRotation;

    private boolean               dirty;

    @Override
    public final ITransform getParent()
    {
        return parent;
    }

    @Override
    public final Iterable<ITransform> getChildren()
    {
        return children;
    }

    @Override
    public final Mat3f getLocalToWorld()
    {
        recalculateLocalToWorld();
        return localToWorld;
    }

    @Override
    public final Mat3f getWorldToLocal()
    {
        recalculateLocalToWorld();
        return worldToLocal;
    }

    @Override
    public final Vec2f getLocalPosition()
    {
        return localPosition;
    }

    @Override
    public final Vec2f getWorldPosition()
    {
        if (parent == null)
        {
            return localPosition;
        } else
        {
            recalculateLocalToWorld();
            return worldPosition;
        }
    }

    @Override
    public final float getLocalRotation()
    {
        return localRotation;
    }

    @Override
    public final float getWorldRotation()
    {
        recalculateLocalToWorld();
        return worldRotation;
    }

    @Override
    public final boolean addChild(final ITransform transform)
    {
        children.add(transform);
        transform.setParent(this);
        return true;
    }

    @Override
    public final boolean setParent(final ITransform transform)
    {
        if (transform.equals(parent))
        {
            return true;
        }
        parent = transform;
        transform.addChild(this);
        setDirty();
        return true;
    }

    @Override
    public final void translate(final float x, final float y)
    {
        localPosition.add(x, y);
        setDirty();
    }

    @Override
    public final void rotate(final float degrees)
    {
        localRotation += degrees;
        setDirty();
    }

    private final void recalculateLocalToWorld()
    {
        if (!dirty)
        {
            return;
        }

        // Local to world
        localToWorld.setRotation(localRotation);
        localToWorld.setTranslation(localPosition.getX(), localPosition.getY());
        if (parent != null)
        {
            final Mat3f parentToWorld = parent.getLocalToWorld();
            localToWorld.rightMultiply(parentToWorld);
            worldPosition.set(parent.getWorldPosition());
            worldPosition.add(localPosition);
            worldRotation = parent.getWorldRotation();
            worldRotation += localRotation;
        }
        // World to local
        worldToLocal.setRotation(-localRotation);
        worldToLocal.setTranslation(-localPosition.getX(), -localPosition.getY());

        if (parent != null)
        {
            worldToLocal.leftMultiply(parent.getWorldToLocal());
        }
        //
        dirty = false;
    }

    @Override
    public final void setDirty()
    {
        dirty = true;
        for (final ITransform t : children)
        {
            t.setDirty();
        }
    }
}
