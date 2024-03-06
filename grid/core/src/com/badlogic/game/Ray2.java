package com.badlogic.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Ray2 implements Entities, Clickable{
    public boolean debug = false;

    public enum Direction  { // enum of ray's directions,
        NE(new float[]{-5.2F, -9}), // NE -> SW // 12, -2
        E(new float[]{-5,0}), // E -> W // -5, 0
        SE(new float[]{-5.2F, 9}), // SE -> NW // -12,2
        SW(new float[]{5.2F,9}), // SW -> NE // 2,-12
        W(new float[]{5,0}), // W -> E // 5,0
        NW(new float[]{5.2F,-9}); // NW -> SE // -2,12

        // vectors for directions are misslabelled as i'm using directions from borders which are the opposite direction

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
    boolean isInside;
    boolean hitAtom;

    // Potentially take chosen side coords, determine direction, on update displace
    public Ray2(float x1, float y1, Direction dir) {
        enterPos = new float[]{x1,y1};
        headPos = new float[]{x1,y1};
        direction = dir;
    }

    @Override
    public void Draw(ShapeRenderer shape)
    {
        shape.begin(ShapeRenderer.ShapeType.Line);
        if(debug) {
            shape.setColor(Color.GREEN);
            shape.line(enterPos[0], enterPos[1], headPos[0], headPos[1]);
        }
        shape.end();
    }

    @Override
    public void update() {
        if(isInside && !hitAtom)
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
        return headPos;
    }

    @Override
    public void getCollision() {

    }
}
