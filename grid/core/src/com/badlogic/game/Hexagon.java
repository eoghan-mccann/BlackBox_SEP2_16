package com.badlogic.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Hexagon implements Entities, Clickable {

    private float centerX;
    private float centerY;
    private float radius;

    private float[] hexPoints;
    private final double angle = Math.toRadians(60);

    boolean clickToggle = false;
    HexagonGrid grid;

    private Atom atom; // related atom

    Hexagon(float x, float y, float r, HexagonGrid hGrid) {
        this.centerX = x;
        this.centerY = y;
        this.radius = r;
        this.grid = hGrid;

        setHexagonPos(this.centerX,this.centerY);
        this.atom = null;
    }

    // Method calculates the X coordinates of the vertices of the hexagon
    private float[] calculateXpoints(float x) {

        float[] tempX = new float[6];

        for (int i = 0; i < 6; i++) { // calculates the x-coordinate of hexagon points using circle
            tempX[i] = (float)(x + (this.radius * Math.sin(angle * i)));
        }

        return tempX;

    }

    // Method calculates the Y coordinates of the vertices of the hexagon
    private float[] calculateYpoints(float y) {

        float[] tempY = new float[6];

        for (int i = 0; i < 6; i++) { // calculates the x-coordinate of hexagon points using circle
            tempY[i] = (float)(y + (this.radius * Math.cos(angle * i)));
        }

        return tempY;
    }

    // Sets the position of hexagon in the window.
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

    public float getWidth() {
        return (hexPoints[2] - centerX) * 2;
    } // Accessor for hexagon width

    public float getHeight() {
        return (hexPoints[1] - centerY) * 2;
    } // Accessor for hexagon height

    @Override
    public void onClick() {

    }

    @Override
    public boolean isClicked()
    {
        if(Gdx.input.justTouched() && isHoveredOver())
        {
            if(atom == null) // if adding an atom
            {
                grid.moveAtom(this);
                return !clickToggle;
            }
            else // if removing an atom
            {
                grid.resetAtom(this);
            }
        }

        return clickToggle;
    }

    public boolean isHoveredOver()
    {
        float curX = Gdx.input.getX();
        float curY = Gdx.graphics.getHeight() - Gdx.input.getY();

        return contains(curX, curY);
    }

    // Uses point in polygon algorithm to check whether the mouseclick was inside the hexagon
    private boolean contains(float x, float y) {
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

    // Accessor for center coordinates
    @Override
    public float[] getCentre() {
        return new float[] {this.centerX, this.centerY};
    }
    public float getCenterX() {
        return getCentre()[0];
    }

    public float getCenterY() {
        return getCentre()[1];
    }

    // Accessor for coordinates of the hexagon
    @Override
    public float[] getCoordinates() {
        return this.hexPoints;
    }
    @Override
    public void getCollision() {

    }
    public void setAtom(Atom atom) {
        this.atom = atom;
    }

    public Atom getAtom() {
        return atom;
    }

    // Creates a non-filled polygon with coordinates in hexPoints
    @Override
    public void Draw(ShapeRenderer shape) {
        shape.begin(ShapeRenderer.ShapeType.Line);;
            shape.polygon(hexPoints);
        shape.end();
    }

    // Updates whether the hexagon is clicked or hovered over
    @Override
    public void update() {
        isClicked();
        isHoveredOver();
    }
}