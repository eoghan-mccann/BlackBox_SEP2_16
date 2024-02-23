package com.badlogic.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Ray2 implements Entities, Clickable{
    /*
    Alternate ray idea
     */

    public boolean toggle;


    enum Direction  { // enum of ray's directions
        NE,
        E,
        SE,
        SW,
        W,
        NW,
    }

    Direction direction;

    float[] enterPos; // coords of start of ray
    float[] headPos; // coords of the head of the ray

    float[] endPoint; // where the ray ends (future thing prob if even used)
    boolean isInside;

    float xSpeed;
    float ySpeed;

    // Potentially take chosen side coords, determine direction, on update displace
    public Ray2(float x1, float y1, float x2, float y2, int dir) {
        enterPos = midPoint(x1, y1, x2, y2);
        headPos = midPoint(x1, y1, x2, y2);

        setSpeed(dir);
        toggle = false;

    }

    public void setSpeed(int dir)
    {
        switch(dir)
        {
            case 1:
                direction = Direction.NE;
                break;

            case 2:
                direction = Direction.E;

                xSpeed = 5;
                ySpeed = 0;
                break;
            case 3:
                direction = Direction.SE;
                break;

            case 4:
                direction = Direction.SW;
                break;

            case 5:
                direction = Direction.W;
                xSpeed = -5;
                ySpeed = 0;
                break;
            case 6:
                direction = Direction.NW;
                break;
            default:

        }
    }

    public float[] midPoint(float x1, float y1, float x2, float y2)
    {
        float midX = (x1 + x2)/2;
        float midY = (y1 + y2)/2;

        return new float[]{midX, midY};
    }

    @Override
    public void Draw(ShapeRenderer shape)
    {
        if(toggle)
        {
            shape.begin(ShapeRenderer.ShapeType.Line);
            shape.setColor(Color.GREEN);
            shape.line(enterPos[0], enterPos[1], headPos[0], headPos[1]);
            shape.end();
        }


    }

    @Override
    public void update() {
        if(isInside)
        {
            headPos[0] += xSpeed;
            headPos[1] += ySpeed;
        }
    }







    @Override
    public void onClick() {

    }

    @Override
    public boolean isClicked() {
        return false;
    }

    @Override
    public boolean isHoveredOver() {
        return false;
    }



    @Override
    public float[] getCentre() {
        return new float[0];
    }

    @Override
    public float[] getCoordinates() {
        return new float[0];
    }

    @Override
    public void getCollision() {

    }
}
