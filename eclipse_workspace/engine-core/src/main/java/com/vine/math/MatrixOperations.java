package com.vine.math;

public interface MatrixOperations<M> {

    void inverse();

    void transpose();

    void add(M matrix);

    void scale(float scale);

    float determinant();

    void rightMultiply(M matrix);

    boolean isEqualTo(M matrix);
}