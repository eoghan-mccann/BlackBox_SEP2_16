package com.badlogic.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.List;

public class Hexagon implements Entities, Clickable {

    private float centerX, centerY;
    private final float radius; // Radius (centre to any corner)

    public float[] hexPoints; // Array of Hexagon's corner coordinates
    private final double angle = Math.toRadians(60);

    boolean clickToggle;
    boolean clickable;
    HexagonGrid grid;

    boolean isBorder; // If hexagon is on the edge of the grid
    int[] sideBorders; // Sides of the hexagon which are on the border
    List<Border> borders;

    Color color;

    Atom atom; // related atom - null if no atom placed
    public boolean isNeighbour; // True if a Hexagon beside this one holds an atom
    public int neighbCount; // Count of the number of neighbour Atoms


    // Enum for tracking what "type" of Neighbour Hexagon this is: If west of the atom, etc.
    // neighbourPos mirrors Ray direction. Bottom left hexagon is NE instead of SW.
    public enum neighourPos {
        NorE(0), East(1), SouE(2),
        NorW(3), West(4), SouW(5);

        public final int ind;
        neighourPos(int i) {
            this.ind = i;
        }
    }

    public enum State {
        PLACING,
        GUESSING
    }

    State state;
    neighourPos neighbDir;


    Hexagon(float x, float y, float r, HexagonGrid hGrid) {
        this.centerX = x;
        this.centerY = y;
        this.radius = r;
        this.grid = hGrid;
        color = Color.WHITE;


        setHexagonPos(this.centerX,this.centerY);
        this.atom = null;
        this.isBorder = false;
        this.state = State.PLACING;
        sideBorders = new int[]{0, 0, 0, 0, 0, 0}; // for rays: starting in the top right, each side gets number index// clockwise
        borders = new ArrayList<>();
        neighbCount = 0;
        clickable = true;
        clickToggle = false;
    }





    // Sets all points on the Hexagon
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

    // Generates the x coordinates of each corner of the Hexagon
    private float[] calculateXpoints(float x) {
        float[] tempX = new float[6];
        for (int i = 0; i < 6; i++) {
            tempX[i] = (float)(x + (this.radius * Math.sin(angle * i)));
        }
        return tempX;
    }

    // Generates the y coordinates of each corner of the Hexagon
    private float[] calculateYpoints(float y) {
        float[] tempY = new float[6];
        for (int i = 0; i < 6; i++) {
            tempY[i] = (float)(y + (this.radius * Math.cos(angle * i)));
        }
        return tempY;
    }


    @Override
    public boolean isClicked() {
        if(Gdx.input.justTouched() && isHoveredOver()) {
            if(state == State.PLACING) {
                if ((atom == null) && (!Game.debugMode)) { // If adding an atom
                    grid.moveAtom(this);
                    return !clickToggle;
                }
                else if ((atom != null) && (!Game.debugMode)) { // If removing an atom
                    grid.resetAtom(this);
                }
            }
            else {return !clickToggle;}
        }
        return clickToggle;
    }

    public boolean isHoveredOver()
    {
        float curX = Gdx.input.getX();
        float curY = Gdx.graphics.getHeight() - Gdx.input.getY();

        return isInside(curX, curY);
    }


    // Point in polygon algorithm to determine if point x, y is inside Hexagon
    public boolean isInside(float x, float y) {
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
    public void Draw(ShapeRenderer shape) {

        shape.begin(ShapeRenderer.ShapeType.Line);
        color = Color.WHITE;

        shape.setColor(color);
        shape.polygon(hexPoints);
        shape.end();

        for (int i = 0; i < borders.size()-1; i++) {
            borders.get(i).Draw(shape);
        }
    }

    @Override
    public void update() {
        isClicked();
        isHoveredOver();

        for (Border border : borders) {
            border.update();
        }

    }

    public float getCenterX() {return this.centerX;}
    public float getCenterY() {return this.centerY;}
    public float getWidth() {return (hexPoints[2] - centerX) * 2;}
    public float getHeight() {return (hexPoints[1] - centerY) * 2;}
    public Atom getAtom() {return this.atom;}

    public boolean hasAtom() {return atom != null;}
    public void setAtom(Atom at) {this.atom = at;}
    public void setClickable(boolean clickable) {this.clickable = clickable;}
    public void setState(State state) {
        this.state = state;
        clickToggle = false;
    }

    @Override
    public float[] getCentre() {return new float[] {this.centerX, this.centerY};}
    @Override
    public float[] getCoordinates() {return this.hexPoints;}


}