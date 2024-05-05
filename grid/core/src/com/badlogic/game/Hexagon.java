package com.badlogic.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * A class representing Hexagons. More complex methods can be found in HexUtil.
 */
public class Hexagon extends HexUtil implements Entities, Clickable {

    private float centerX, centerY;
    private final float radius; // Radius (centre to any corner)
    Color color;

    HexagonGrid grid;
    Atom atom; // related atom - null if no atom placed


    public float[] hexPoints; // Array of Hexagon's corner coordinates
    private final double angle = Math.toRadians(60);


    boolean isBorder; // If hexagon is on the edge of the grid
    int[] sideBorders; // Sides of the hexagon which are on the border
    List<Border> borders;


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
    neighourPos neighbDir; // This Hexagon's position in relation to its Neighbour


    Hexagon(float x, float y, float r, HexagonGrid hGrid) {
        this.centerX = x;
        this.centerY = y;
        this.radius = r;
        this.grid = hGrid;
        color = Color.WHITE;


        setHexagonPos(this, this.centerX,this.centerY);
        this.atom = null;
        this.isBorder = false;
        this.state = State.PLACING;
        sideBorders = new int[]{0, 0, 0, 0, 0, 0}; // for rays: starting in the top right, each side gets number index// clockwise
        borders = new ArrayList<>();
        neighbCount = 0;

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
        if(isClicked()) {
            if(state == State.PLACING) {
                if(atom == null) {
                    grid.moveAtom(this);
                }
                else {
                    grid.resetAtom(this);
                }
            }
        }

        for (Border border : borders) {
            border.update();
        }

    }

    @Override
    public boolean isClicked() {
        return Gdx.input.justTouched() && isHoveredOver();
    }

    @Override
    public boolean isHoveredOver() {
        return isInside(this, Gdx.input.getX(), (Gdx.graphics.getHeight() - Gdx.input.getY()));
    }


    /**
     * Gets the value of {@code centerX}.
     * @return The value of centerX
     */
    public float getCenterX() {return this.centerX;}

    /**
     * Gets the value of {@code centerY}.
     * @return The value of centerY
     */
    public float getCenterY() {return this.centerY;}

    /**
     * Gets the width of a Hexagon, being the distance from side to opposite side.
     * @return The calculated width.
     */
    public float getWidth() {return (hexPoints[2] - centerX) * 2;}

    /**
     * Gets the height of a Hexagon.
     * @return The calculated height.
     */
    public float getHeight() {return (hexPoints[1] - centerY) * 2;}

    /**
     * Gets the Atom associated with this Hexagon, if it exists.
     * @return The Atom object.
     */
    public Atom getAtom() {return this.atom;}

    /**
     * Gets the radius of a Hexagon.
     * @return The radius.
     */
    public float getRadius() {return this.radius;}

    /**
     * Gets the angle of each side of the Hexagon.
     * @return The angle.
     */
    public double getAngle() {return angle;}

    /**
     * Determines if the Hexagon has an associated Atom.
     * @return {@code true} if it does hold an atom, {@code false} otherwise.
     */
    public boolean hasAtom() {return atom != null;}

    /**
     * Sets the associated Atom of this Hexagon to the given Atom.
     * @param at The atom to be added to Hexagon.
     */
    public void setAtom(Atom at) {this.atom = at;}

    /**
     * Sets the state of the Hexagon.
     * @param state The state to be set to
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     * Sets the center coordinates of the Hexagon to the given coordinates.
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public void setCenter(float x, float y) {
        centerX = x;
        centerY = y;
    }

    @Override
    public float[] getCentre() {return new float[] {this.centerX, this.centerY};}
    @Override
    public float[] getCoordinates() {return this.hexPoints;}


}