package com.badlogic.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Hexagon implements Entities {

    private float centerX;
    private float centerY;
    private float radius;

    private float[] hexPoints;
    private final double angle = Math.toRadians(60);

    Hexagon(float x, float y, float r) {
        this.centerX = x;
        this.centerY = y;
        this.radius = r;

        setHexagonPos(this.centerX,this.centerY);
    }

    private float[] calculateXpoints(float x) {

        float[] tempX = new float[6];

        for (int i = 0; i < 6; i++) {
            tempX[i] = (float)(x + (this.radius * Math.sin(angle * i)));
        }

        return tempX;

    }

    private float[] calculateYpoints(float y) {

        float[] tempY = new float[6];

        for (int i = 0; i < 6; i++) {
            tempY[i] = (float)(y + (this.radius * Math.cos(angle * i)));
        }

        return tempY;
    }

    public void setHexagonPos(float x, float y) {
        float[] xPoints = this.calculateXpoints(x);
        float[] yPoints = this.calculateYpoints(y);
        float[] flattenedPoints = new float[12];

        // flattens array to work with polygon [x1,y1,x2,y2,...]
        for (int i = 0; i < xPoints.length; i++) {
            flattenedPoints[2 * i] = xPoints[i];
            flattenedPoints[2 * i + 1] = yPoints[i];
        }

        this.hexPoints = flattenedPoints;
    }

    @Override
    public float[] getCentre() {
        return new float[] {this.centerX, this.centerY};
    }

    @Override
    public float[] getCoordinates() {
        return this.hexPoints;
    }

    @Override
    public void getPosition() {

    }

    @Override
    public void getCollision() {

    }

    @Override
    public void Draw(ShapeRenderer shape) {

        shape.polygon(hexPoints);

    }

    @Override
    public void update() {

    }
}