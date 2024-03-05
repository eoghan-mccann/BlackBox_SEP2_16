package com.badlogic.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Ray2 implements Entities, Clickable{
    public boolean toggle;


    public enum Direction  { // enum of ray's directions
        NE(new float[]{2, -12}),
        E(new float[]{5, 0}),
        SE(new float[]{-2, 12}),
        SW(new float[]{12,2}),
        W(new float[]{-5, 0}),
        NW(new float[]{-12, 2});

        public final float[] direction;

        public float getXSpeed() {return direction[0];}
        public float getYSpeed() {return direction[1];}

        Direction(float[] direction) {
            this.direction = direction;
        }
    }

    Direction direction;

    float[] enterPos; // coords of start of ray
    float[] headPos; // coords of the head of the ray

    float[] endPoint; // where the ray ends (future thing prob if even used)
    boolean isInside;

    float xSpeed;
    float ySpeed;

    // Potentially take chosen side coords, determine direction, on update displace
    public Ray2(float x1, float y1, Direction dir) {
        enterPos = new float[]{x1,y1};
        headPos = new float[]{x1,y1};
        direction = dir;

        toggle = false;
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
            headPos[0] += direction.getXSpeed();
            headPos[1] += direction.getYSpeed();
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
