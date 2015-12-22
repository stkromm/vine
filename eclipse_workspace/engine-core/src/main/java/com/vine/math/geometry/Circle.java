package com.vine.math.geometry;

import com.vine.math.Vector2f;

public class Circle extends Shape {
    private float x;
    private float y;
    private float radius;
    private final Vector2f distanceVector = new Vector2f(0, 0);

    public Circle(float x, float y, float radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    @Override
    public boolean contains(float x, float y) {
        distanceVector.setX(x - this.x);
        distanceVector.setY(y - this.y);
        return Math.abs(distanceVector.length()) <= radius;
    }
}