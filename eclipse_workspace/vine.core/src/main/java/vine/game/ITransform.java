package vine.game;

import vine.math.matrix.Mat3f;
import vine.math.vector.Vec2f;

/**
 * <h3>Transform</h3> Describes a transformation of location(rotation +
 * position) in a two dimensional room (world).
 * <h3>Properties</h3>
 * <p>
 * A Transform can have one parent transformation and multiple child
 * transformations.
 * </p>
 * If there is a parent transform, then the described transformation is relative
 * to the transformation described by the parent. If the parent is null, then
 * the transform describes a transformation in worldspace.
 * <h3>Units</h3>
 * <p>
 * The unit for rotation is always Degrees (0 degrees rotation means a line
 * parallel to the x-Axis). The unit for translation is the used world-unit.
 * </p>
 * 
 * @author Steffen Kromm, first created on 03.05.2016
 */
public interface ITransform
{
    /**
     * The transformation that is used to describe the relative transformation
     * of this transform to the world. (this.worldToLocal = parent.worldToLocal
     * this.transformation);
     * 
     * It's guaranteed that this transform is a child transform of the parent
     * transform.
     * 
     * @return The parent transform.
     */
    ITransform getParent();

    /**
     * Returns all children of this transform. It's guaranteed that the parent
     * of each of the children transform is this transform.
     * 
     * @return The child transforms of this transform.
     */
    Iterable<ITransform> getChildren();

    /**
     * Adds the transform to the child transforms of this object.
     * 
     * @param transform
     *            The transform, that should be added as a child.
     * @return True, if the transform was successfully added as a child.
     */
    boolean addChild(ITransform transform);

    /**
     * Sets the parent of this transformation.
     * 
     * @param transform
     *            The new parent transformation. A valid parent transform must
     *            not be a transform of the children transformation
     *            graphs(child, child of child and so on) or this transform.
     * @return True, if the parent could be successfully set.
     */
    boolean setParent(ITransform transform);

    /**
     * @return The local to world transformation matrix.
     */
    Mat3f getLocalToWorld();

    /**
     * @return The world to local transformation matrix.
     */
    Mat3f getWorldToLocal();

    /**
     * @return The position of this transform relative to its parent.
     */
    Vec2f getLocalPosition();

    /**
     * @return The position of this transform relative to the world.
     */
    Vec2f getWorldPosition();

    /**
     * @return The rotation of this transform in degree relative to its parent.
     */
    float getLocalRotation();

    /**
     * @return The rotation of this transform in degree relative to the world.
     */
    float getWorldRotation();

    /**
     * Translates the transform by the by x and y defined direction.
     * 
     * @param x
     *            The translation in x dimension.
     * @param y
     *            The translation in y dimension.
     */
    void translate(float x, float y);

    /**
     * Rotates the transform by the given angle in degrees.
     * 
     * @param radians
     *            Angle of rotation in degree.
     */
    void rotate(float degrees);

    void setDirty();
}
