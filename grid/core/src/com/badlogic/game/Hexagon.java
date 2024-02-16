package com.badlogic.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Hexagon implements Entities {

    private float centerX;
    private float centerY;
    private float radius;

    public boolean isClickToggled = false;

    private float[] hexPoints;
    private final double angle = Math.toRadians(60);

    Atom atom = null;

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
        centerX = x;
        centerY = y;

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

    public float getWidth() {
        return (hexPoints[2] - centerX) * 2;
    }

    public float getHeight() {
        return (hexPoints[1] - centerY) * 2;
    }

    private void CheckClicked()
    {
        if(Gdx.input.justTouched() && isHoveredOver())
        {
            isClickToggled = !isClickToggled;
        }
    }

    public boolean isClicked() { return isClickToggled; }

    public boolean isHoveredOver()
    {
        float curX = Gdx.input.getX();
        float curY = Gdx.graphics.getHeight() - Gdx.input.getY();

        return contains(curX, curY);
    }

    private boolean contains(float x, float y) { // Point in polygon algorithm.
        int i, j;
        boolean isInside = false;
        float[] vertices = this.getCoordinates();

        for (i = 0, j = vertices.length - 2; i < vertices.length; j = i, i += 2) {
            if ((vertices[i + 1] > y) != (vertices[j + 1] > y) && (x < (vertices[j] - vertices[i]) * (y - vertices[i + 1]) / (vertices[j + 1] - vertices[i + 1]) + vertices[i])) {
                isInside = !isInside;
            }
        }

        return isInside;

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

        shape.begin(ShapeRenderer.ShapeType.Line);
            shape.setColor(Color.WHITE);
            shape.polygon(hexPoints);
        shape.end();
    }

    @Override
    public void update() {
        CheckClicked();

        if (isClicked()) {
            atom = new Atom(getCentre()[0], getCentre()[1], 10, 25);
        } else {
            atom = null;
        }
    }
}