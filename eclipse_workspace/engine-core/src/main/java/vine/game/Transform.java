/**
 * 
 */
package vine.game;

import java.util.HashSet;
import java.util.Set;

import vine.math.matrix.Mat3f;
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

    private final Mat3f           parentToLocal = new Mat3f();
    private final Mat3f           localToWorld  = new Mat3f();
    private final Mat3f           worldToLocal  = new Mat3f();

    private final MutableVec2f    localPosition = new MutableVec2f();
    private final MutableVec2f    worldPosition = new MutableVec2f();
    private float                 worldRotation;
    private float                 localRotation;

    private boolean               dirty;

    @Override
    public final ITransform getParent()
    {
        return this.parent;
    }

    @Override
    public final Iterable<ITransform> getChildren()
    {
        return this.children;
    }

    @Override
    public final Mat3f getLocalToWorld()
    {
        recalculateLocalToWorld();
        return this.localToWorld;
    }

    @Override
    public final Mat3f getWorldToLocal()
    {
        recalculateLocalToWorld();
        return this.worldToLocal;
    }

    @Override
    public final Vec2f getLocalPosition()
    {
        return this.localPosition;
    }

    @Override
    public final Vec2f getWorldPosition()
    {
        recalculateLocalToWorld();
        return this.worldPosition;
    }

    @Override
    public final float getLocalRotation()
    {
        return this.localRotation;
    }

    @Override
    public final float getWorldRotation()
    {
        recalculateLocalToWorld();
        return this.worldRotation;
    }

    @Override
    public final boolean addChild(final ITransform transform)
    {
        this.children.add(transform);
        transform.setParent(this);
        return true;
    }

    @Override
    public final boolean setParent(final ITransform transform)
    {
        if (this.parent.equals(transform))
        {
            return true;
        }
        this.parent = transform;
        transform.addChild(this);
        this.dirty = true;
        return true;
    }

    @Override
    public final void translate(final float x, final float y)
    {
        this.localPosition.add(x, y);
    }

    @Override
    public final void rotate(final float degrees)
    {
        this.localRotation += degrees;
    }

    private final void recalculateLocalToWorld()
    {
        if (!this.dirty)
        {
            return;
        }
        // Local to world
        this.localToWorld.setRotation(this.localRotation);
        this.localToWorld.setTranslation(this.localPosition.getX(), this.localPosition.getY());
        final Mat3f parentToWorld = this.parent.getLocalToWorld();
        this.localToWorld.rightMultiply(parentToWorld);

        // World to local
        this.worldToLocal.setRotation(-this.localRotation);
        this.worldToLocal.setTranslation(-this.localPosition.getX(), -this.localPosition.getY());
        final Mat3f worldToParent = this.parent.getWorldToLocal();
        worldToParent.leftMultiply(this.worldToLocal);

        //
        this.dirty = false;
    }
}
