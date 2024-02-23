package com.badlogic.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Ray implements Entities, Viewable {
    //left 19
    //top 10
    //technically 10 options per hexagon side

    //shooting an array in a straight line:

    //centre coordinates of the hexagon where the ray starts
    //at least 5 hex in a row at most 9
    float enterPointX;
    float enterPointY;

    //centre coordinates of the hexagon where the ray ends
    float exitPointX;
    float exitPointY;

    float[] rayPoints = new float[4];


    public Ray(float enterPointX, float enterPointY, float exitPointX, float exitPointY) {
        this.enterPointX = enterPointX;
        this.enterPointY = enterPointY;
        this.exitPointX = exitPointX;
        this.exitPointY = exitPointY;

        this.rayPoints[0] = enterPointX;
        this.rayPoints[1] = enterPointY;
        this.rayPoints[2] = exitPointX;
        this.rayPoints[3] = exitPointY;

    }

    //get Centre not used as it is a ray
    @Override
    public float[] getCentre() {
        return new float[0];
    }

    public void setCoordinates(float x1, float y1, float x2, float y2) {
        if (x1 < 0 || x2 < 0 || y1 < 0 || y2 < 0) {
            throw new IllegalArgumentException("Arguments cant be negative");
        } else {
            this.rayPoints[0] = x1;
            this.rayPoints[1] = y1;
            this.rayPoints[2] = x2;
            this.rayPoints[3] = y2;
        }
    }


    //returns enter point and exit point coordinates
    @Override
    public float[] getCoordinates() {
        return this.rayPoints;
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
        shape.setColor(Color.BLUE);
        shape.line(enterPointX, enterPointY, exitPointX, exitPointY);
        shape.setColor(Color.WHITE);
        shape.end();

    }

    @Override
    public void update() {

    }
}

