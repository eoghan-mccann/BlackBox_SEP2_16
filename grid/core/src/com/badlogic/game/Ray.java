package com.badlogic.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.List;


/**
 * Rays are stored as a sequence of lines. Lines consist of a starting coordinate and an end coordinate.
 *         All lines are stored in the {@code lines} List, except for the line currently moving through the grid.
 *         The current lines start coordinate will be the end coordinate of the previous line, and its head is stored in {@code headPos}.
 *         Extra utility methods for the Ray class are in the {@code RayUtil} class.
 */

public class Ray extends RayUtil implements Entities{
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

    List<List<Float>> lines; // List of lists of coordinates of each line
    HexagonGrid grid;

    Hexagon currHex; // If ray is inside the grid, this is the hexagon it is currently inside
    Hexagon startHex; // Stores hexagon that ray started in

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
        else if(isInside && currHex.isNeighbour && currHex.atom == null) { // if currHex = neighbour and DOESN'T have an atom in it

            // Move ray head to center of hex, change direction, move
            setHeadPos(new float[]{currHex.getCenterX(), currHex.getCenterY()});

            setCurrDirection(currHex, this);
            headPos[0] += currDirection.getXSpeed()*6;
            headPos[1] += currDirection.getYSpeed()*6;

            grid.rayCheck(this);

        }

        if (!isMoving()) {
            spawnRayMarker();
        }
    }


    /**
     * Spawn a {@code RayMarker} for the associated Ray, in a position relative to its status and position on the board.
     *
     */
    private void spawnRayMarker() {
        RayMarker startMarker;
        RayMarker endMarker;
        RayMarker.Result result;

        int productOffset = 8; // offset product for how far marker will be from hexagon

        float[] startMarkerPos;
        float[] endMarkerPos;

        startMarkerPos = new float[] {
                this.startPos[0] - startDirection.getXSpeed() * productOffset,
                this.startPos[1] - startDirection.getYSpeed() * productOffset
        };

        // If ray hit atom on the border = HIT
        if(currHex.isBorder && hitAtom) {
            // Accounts for hexagons on border hit from ray on other side of board
            if (!lines.isEmpty()) {
                startMarkerPos = new float[]{
                        lines.get(0).get(0) - startDirection.getXSpeed() * productOffset,
                        lines.get(0).get(1) - startDirection.getYSpeed() * productOffset
                };
            }

            endMarkerPos = null;
            result = RayMarker.Result.HIT;
        }

        // If the ray is only 1 line = MISS
        else if (lines.isEmpty()) {
            endMarkerPos = new float[] {
                    this.headPos[0] + currDirection.getXSpeed() * productOffset,
                    this.headPos[1] + currDirection.getYSpeed() * productOffset
            };

            result = RayMarker.Result.MISS;
        }

        // If the ray is still inside the hexagon, could have deflections but got swallowed = HIT
        else if (isInside) {
            startMarkerPos = new float[]{
                    lines.get(0).get(0) - startDirection.getXSpeed() * productOffset,
                    lines.get(0).get(1) - startDirection.getYSpeed() * productOffset
            };

            endMarkerPos = startMarkerPos;
            result = RayMarker.Result.HIT;
        }

        // Ray has bounced = REFLECTION or DEFLECTION
        else {
            startMarkerPos = new float[]{
                    lines.get(0).get(0) - startDirection.getXSpeed() * productOffset,
                    lines.get(0).get(1) - startDirection.getYSpeed() * productOffset
            };
            endMarkerPos = new float[]{
                    headPos[0] + currDirection.getXSpeed() * productOffset,
                    headPos[1] + currDirection.getYSpeed() * productOffset
            };

            result = RayMarker.Result.DEFLECTION;

            // if start hexagon == end hexagon and the end direction is the start direction reflected back
            if (currHex == startHex && currDirection == getReflectionDirection(startDirection)) {
                result = RayMarker.Result.REFLECTION;
                endMarkerPos = null;
            }
        }

        startMarker = new RayMarker(startMarkerPos, markerRadius, result);
        endMarker = endMarkerPos == null ? startMarker : new RayMarker(endMarkerPos, markerRadius, result);
        rayMarkers = new RayMarker[] {startMarker, endMarker};
    }

    /**
     * Determines whether a Ray object is moving.
     *
     * @return {@code true} if Ray is moving, {@code false} if not
     */
    private boolean isMoving() {return !(hitAtom || !isInside);}


    // ----        Getter/setter methods       ----
    @Override
    public float[] getCoordinates() { return headPos;}

    /**
     * Get the coordinates of the entry point of the associated Ray object to the Grid.
     *
     * @return The coordinates of entry as a float array, where index 0 is the x coordinate and index 1 is the y coordinate.
     */
    public float[] getEnterPos(){ return enterPos;}

    /**
     * Get the coordinates of the head of the associated Ray object.
     *
     * @return The coordinates of the head as a float array, where index 0 is the x coordinate and index 1 is the y coordinate.
     */
    public float[] getHeadPos() { return headPos;}

    /**
     * Set the visibility status of the associated Ray object.
     *
     * @param visible {@code true} to make Ray visibile, {@code false} to make Ray invisible
     */
    public void setVisible(boolean visible) { this.visible = visible;}

    /**
     * Set the associated Rays point of entry into the Grid.
     *
     * @param arr array where index 0 is the x coordinate, index 1 is the y coordinate
     */
    public void setEnterPos(float[] arr) {
        enterPos[0] = arr[0];
        enterPos[1] = arr[1];
    }

    /**
     * Set the head coordinates of the associated Ray.
     *
     * @param arr array where index 0 is the x coordinate, index 1 is the y coordinate
     */
    public void setHeadPos(float[] arr) {
        headPos[0] = arr[0];
        headPos[1] = arr[1];
    }
    /**
     * Gets the Direction of the ray
     *
     * @return Direction of Ray
     */
    public Direction getCurrDirection() {
        return currDirection;
    }
    /**
     * Sets the hex that the ray is currently in
     *
     * @param currHex hexagon it's in
     */
    public void setCurrHex(Hexagon currHex) {
        this.currHex = currHex;
    }

    @Override
    public float[] getCentre() {return new float[0];}

}
