package com.vine.math.geometry;

import com.vine.math.Vector2f;

public class Rectangle extends Shape {
    private static final float SKIN_WIDTH = -0.000001f;
    private final Vector2f origin;
    private final Vector2f diagonal;
    private double diagonalLength;
    private final Vector2f distanceVector = new Vector2f(0, 0);

    public Rectangle(float x, float y, float width, float height) {
        origin = new Vector2f(x, y);
        diagonal = new Vector2f(width, height);
        diagonalLength = diagonal.length();
    }

    @Override
    final boolean contains(float x, float y) {
        distanceVector.setX(x - origin.getX());
        distanceVector.setY(y - origin.getY());
        return distanceVector.length() - diagonalLength > SKIN_WIDTH;
    }
}
