package com.badlogic.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.List;

/*
        Rays are stored as a sequence of lines. Lines consist of a starting coordinate and an end coordinate.
        All lines are stored in the lines List, except for the line currently moving through the grid.
        The current lines start coordinate will be the end coordinate of the previous line, and its head is stored in headPos[]

        Extra utility methods for the Ray class are in the RayUtil class.
*/

public class Ray extends RayUtil implements Entities, Clickable{
    // Enum of ray's directions - "direction the ray is coming from" - a NE ray is travelling SW
    public enum Direction  {
        NE(new float[]{-5.2F, -9}), // NE -> SW
        E(new float[]{-10,0}), // E -> W
        SE(new float[]{-5.2F, 9}), // SE -> NW
        SW(new float[]{5.2F,9}), // SW -> NE
        W(new float[]{10,0}), // W -> E
        NW(new float[]{5.2F,-9}); // NW -> SE

        public final float[] direction;

        public float getXSpeed() {return direction[0];}
        public float getYSpeed() {return direction[1];}

        Direction(float[] direction) {
            this.direction = direction;
        }
    }


    public boolean visible; // Display visibility toggle
    boolean isInside; // True if ray is inside an atom
    boolean hitAtom; // True if ray has hit an atom

    Direction startDirection; // Ray's starting direction
    Direction currDirection; // Ray's head's direction

    float[] startPos; // Coordinate of ray's start

    float[] enterPos; // Coordinate of start of current line
    float[] headPos; // Coordinate of the head of the current line

    float markerRadius;

    List<List<Float>> lines; // list of lists of coordinates of each line
    HexagonGrid grid;

    Hexagon currHex; // if ray is inside the grid, this is the hexagon it is currently inside

    RayMarker[] rayMarkers;



    public Ray(float x1, float y1, Direction dir, HexagonGrid gr) {
        enterPos = new float[]{x1,y1};
        startPos = enterPos;
        headPos = new float[]{x1,y1};

        currDirection = dir;
        startDirection = dir;

        lines = new ArrayList<>();
        markerRadius = 10;

        visible = false;

        grid = gr;

    }


    @Override
    public void Draw(ShapeRenderer shape) {
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(Color.GREEN);

        if(visible) {
            if(!lines.isEmpty()) {
                for (List<Float> line : lines) {
                    shape.line(line.get(0), line.get(1), line.get(2), line.get(3));
                }
            }
            // draw current line
            shape.line(enterPos[0], enterPos[1], headPos[0], headPos[1]);
        }

        shape.end();



        if (rayMarkers != null) {
            for (RayMarker rayMarker : rayMarkers) {
                rayMarker.Draw(shape);
            }
        }
    }

    @Override
    public void update() {
        grid.rayCheck(this);

        if(currHex.atom != null)
        {
            hitAtom = true;
        }


        if(isInside && !hitAtom && !currHex.isNeighbour) { // if in the grid and hasn't hit atom/aura, update
            headPos[0] += currDirection.getXSpeed();
            headPos[1] += currDirection.getYSpeed();
        }
        else if(currHex.isNeighbour && currHex.atom == null) { // if currHex = neighbour and DOESN'T have an atom in it

            // Move ray head to center of hex, change direction, move
            setHeadPos(new float[]{currHex.getCenterX(), currHex.getCenterY()});

            setCurrDirection(currHex, this);
            headPos[0] += currDirection.getXSpeed()*5;
            headPos[1] += currDirection.getYSpeed()*5;

            grid.rayCheck(this);

        }

        if (!isMoving()) {
            spawnRayMarker();
        }

    }



    private void spawnRayMarker() {
        RayMarker startMarker;
        RayMarker endMarker;
        RayMarker.Result result;

        int productOffset = 7;

        float[] startMarkerPos;
        float[] endMarkerPos;

        if(currHex.isBorder && hitAtom) // if hit border atom
        {
            startMarkerPos = new float[] {
                    this.startPos[0] - startDirection.getXSpeed() * productOffset,
                    this.startPos[1] - startDirection.getYSpeed() * productOffset
            };
            endMarkerPos = null;


            result = RayMarker.Result.HIT;
        }
        // If the ray is only 1 line = MISS
        else if (lines.isEmpty()) {
            startMarkerPos = new float[] {
                    this.startPos[0] - startDirection.getXSpeed() * productOffset,
                    this.startPos[1] - startDirection.getYSpeed() * productOffset
            };
            endMarkerPos = new float[] {
                    this.headPos[0] + currDirection.getXSpeed() * productOffset,
                    this.headPos[1] + currDirection.getYSpeed() * productOffset
            };

            result = RayMarker.Result.MISS;
        // If the ray is still inside the hexagon, maybe has reflections but got swallowed = HIT
        } else if (isInside) {
            startMarkerPos = new float[] {
                    lines.get(0).get(0) - startDirection.getXSpeed() * productOffset,
                    lines.get(0).get(1) - startDirection.getYSpeed() * productOffset
            };

            endMarkerPos = startMarkerPos;

            result = RayMarker.Result.HIT;
        }
        else { // Deflected rays, no swallowed = REFLECTION
            startMarkerPos = new float[]{
                    lines.get(0).get(0) - startDirection.getXSpeed() * productOffset,
                    lines.get(0).get(1) - startDirection.getYSpeed() * productOffset
            };
            endMarkerPos = new float[]{
                    headPos[0] + currDirection.getXSpeed() * productOffset,
                    headPos[1] + currDirection.getYSpeed() * productOffset
            };

            result = RayMarker.Result.REFLECTION;
        }

        startMarker = new RayMarker(startMarkerPos, markerRadius, result);
        if(endMarkerPos == null)
        {
            endMarker = startMarker;
        }
        else
        {
            endMarker = new RayMarker(endMarkerPos, markerRadius, result);
        }


        rayMarkers = new RayMarker[] {startMarker, endMarker};
    }

    private boolean isMoving() {return !(hitAtom || !isInside);}


    // ----       getter/setter methods      ----
    @Override
    public float[] getCoordinates() { return headPos;}
    public float[] getEnterPos(){ return enterPos;}
    public float[] getStartPos() { return startPos;}
    public float[] getHeadPos() { return headPos;}

    public void setVisible(boolean visible) { this.visible = visible;}
    public void setEnterPos(float[] arr) {
        enterPos[0] = arr[0];
        enterPos[1] = arr[1];
    }
    public void setHeadPos(float[] arr) {
        headPos[0] = arr[0];
        headPos[1] = arr[1];
    }




    // ----       redundant (unused) methods       ----
    @Override
    public void onClick() {}

    @Override
    public boolean isClicked() {return false;}

    @Override
    public boolean isHoveredOver() {return false;}

    @Override
    public float[] getCentre() {return new float[0];}

}
