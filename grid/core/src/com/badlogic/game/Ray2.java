package com.badlogic.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.List;

public class Ray2 implements Entities, Clickable{
    public boolean debug = false;

    public enum Direction  { // enum of ray's directions,
        NE(new float[]{-5.2F, -9}), // NE -> SW // 12, -2
        E(new float[]{-5,0}), // E -> W // -5, 0
        SE(new float[]{-5.2F, 9}), // SE -> NW // -12,2
        SW(new float[]{5.2F,9}), // SW -> NE // 2,-12
        W(new float[]{5,0}), // W -> E // 5,0
        NW(new float[]{5.2F,-9}); // NW -> SE // -2,12

        // vectors for directions are mislabelled as i'm using directions from borders which are the opposite direction

        public final float[] direction;

        public float getXSpeed() {return direction[0];}
        public float getYSpeed() {return direction[1];}

        Direction(float[] direction) {
            this.direction = direction;
        }
    }

    Direction direction;

    float[] startPos; // coords of the ray's start

    float[] enterPos; // coords of start of ray (linE)
    float[] headPos; // coords of the head of the ray

    boolean isInside;
    boolean hitAtom;
    List<List<Float>> lines;
    int numLines;

    Hexagon currHex; // if ray is inside the grid, this is the hexagon it is currently inside

    // Potentially take chosen side coords, determine direction, on update displace
    public Ray2(float x1, float y1, Direction dir) {
        enterPos = new float[]{x1,y1};
        startPos = enterPos;
        headPos = new float[]{x1,y1};
        direction = dir;
        lines = new ArrayList<>();
    }
    /*
    Ray moving idea:
    <List<List>> where each row is one line making up the ray path, with 4 vals; start point, head point
    when !isInside, that line must be done. Add current line to list, set direction, new line vals?

     */
    public void setDirection() // called when ray hits border (atm) - changes ray direction and continues moving
    {

        numLines = lines.size();
        lines.add(new ArrayList<>());

        // add curr line to lines
        lines.get(numLines).add(enterPos[0]);
        lines.get(numLines).add(enterPos[1]);
        lines.get(numLines).add(headPos[0]);
        lines.get(numLines).add(headPos[1]);



        // line now starts at head, curr head is also there
        enterPos[0] = headPos[0];
        enterPos[1] = headPos[1];

        // set direction of new line
        switch(direction) // DIRECTIONS ARE FLIPPED - NE MEANS RAY IS GOING TO SW
        {
            case NE:
                direction = Direction.W;
                break;
            case E:
                direction = Direction.NW;
                break;
            case SE:
                direction = Direction.SW;
                break;
            case SW:
                direction = Direction.NE;
                break;
            case W:
                direction = Direction.SE;
                break;
            case NW:
                direction = Direction.E;
                break;
        }

    }

    @Override
    public void Draw(ShapeRenderer shape)
    {
        shape.begin(ShapeRenderer.ShapeType.Line);
        if(debug) {
            shape.setColor(Color.GREEN);

            if(!lines.isEmpty()) // if there's been a reflection
            {
                for(int i=0;i< lines.size();i++) // print all lines
                {
                    shape.line(lines.get(i).get(0), lines.get(i).get(1), lines.get(i).get(2), lines.get(i).get(3));
                }
            }

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
        if(!isInside) // move back, set direction, set isinside back
        { // change to when ray hits atom aura
            headPos[0] = currHex.getCenterX(); // move line to centre of current hexagon
            headPos[1] = currHex.getCenterY();
            setDirection();
            isInside = true;
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
