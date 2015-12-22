package com.vine.math;

import com.vine.util.BufferUtils;

import java.nio.FloatBuffer;

public class Transformation {
    private Vector3f translation;
    private Matrix3f rotation;
    private Vector3f projection;

    public Vector3f getTranslation() {
        return translation;
    }

    public Matrix3f getRotation() {
        return rotation;
    }

    public Vector3f getProjection() {
        return projection;
    }

    /**
     * Creates an orthographic transformation matrix.
     */
    public void orthographic(float left, float right, float bottom, float top, float near, float far) {
        rotation = rotation.getIdentity();
        rotation.setA11(2.0f / (right - left));
        rotation.setA22(2.0f / (right - left));
        rotation.setA33(2.0f / (near - far));
        //
        projection = new Vector3f(-(left + right) / (left - right), -(bottom + top) / (bottom - top),
                -(far + near) / (far - near));
    }

    /**
     * Sets the translation of the transformation.
     */
    public void translate(Vector3f vector) {
        translation = vector;
    }

    /**
     * Sets the angle of the rotation of the transformation.
     */
    public void setRotation(float angle) {
        float rot = (float) Math.toRadians(angle);
        float cos = (float) Math.cos(rot);
        float sin = (float) Math.sin(rot);
        rotation.setA11(cos);
        rotation.setA12(sin);
        rotation.setA21(-sin);
        rotation.setA22(cos);
    }

    /**
     * Multiplies the given matrix from the right and returns the result.
     */
    public void compose(Transformation matrix) {
        translation.add(matrix.getTranslation());
        rotation.rightMultiply(matrix.getRotation());
    }

    public FloatBuffer toFloatBuffer() {
        return BufferUtils.createFloatBuffer(new float[] { rotation.getA11(), rotation.getA12(), rotation.getA13(),
                translation.getX(), rotation.getA21(), rotation.getA22(), rotation.getA23(), translation.getY(),
                rotation.getA31(), rotation.getA32(), rotation.getA33(), translation.getZ(), projection.getX(),
                projection.getY(), projection.getZ(), 1 });
    }
}