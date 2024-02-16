package com.badlogic.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Ray implements Entities{
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


    public Ray(float enterPointX, float enterPointY, float exitPointX, float exitPointY){
        this.enterPointX = enterPointX;
        this.enterPointY = enterPointY;
        this.exitPointX = exitPointX;
        this.exitPointY = exitPointY;
    }

    //get Centre not used as it is a ray
    @Override
    public float[] getCentre() {
        return new float[0];
    }


    //returns enter point and exit point coordinates
    @Override
    public float[] getCoordinates() {
        return new float[0];
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

