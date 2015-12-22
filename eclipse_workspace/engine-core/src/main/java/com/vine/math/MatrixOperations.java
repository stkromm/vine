package com.vine.math;

public interface MatrixOperations<M> {
    /**
     * Transforms this matrix into its inversed matrix. Does nothin, if the
     * matrix is not invertable.
     */
    void inverse();

    /**
     * Transposes this matrix.
     */
    void transpose();

    /**
     * Adds the entries of the given matrix to the elements of this matrix.
     */
    void add(M matrix);

    /**
     * Multiplies every element of the matrix with the given scale value.
     */
    void scale(float scale);

    /**
     * Calculates the determinant of this matrix and returns it.
     */
    float determinant();

    /**
     * Multiplies the given matrix from the right with this Matrix3f.
     */
    void rightMultiply(M matrix);

    /**
     * Returns true, if every element of this matrix is equal to the equivalent
     * element of the given matrix.
     */
    boolean isEqualTo(M matrix);
}