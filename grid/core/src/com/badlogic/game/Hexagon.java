package com.badlogic.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.game.Game.hasBeenChanged;

public class Hexagon implements Entities, Clickable {

    private float centerX;
    private float centerY;
    private float radius;

    public float[] hexPoints;
    private final double angle = Math.toRadians(60);

    boolean clickToggle = false;
    HexagonGrid grid;

    boolean isBorder;
    int[] sideBorders;
    List<Border> borders;


    Color color;

    Atom atom; // related atom - null if no atom placed
    public boolean isNeighbour;
    //if an atom is placed down and to the left of a hexagon, that hexagon will be NorE (NorthEast hex from the atom)
    public enum neighourPos {
        NorE(0), East(1), SouE(2),
        NorW(3), West(4), SouW(5);

        public final int ind;
        neighourPos(int i) {
            this.ind = i;
        }
    }

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
        sideBorders = new int[]{0, 0, 0, 0, 0, 0}; // for rays: starting in the top right, each side gets number index// clockwise
        borders = new ArrayList<>();
    }




    private float[] calculateXpoints(float x) {

        float[] tempX = new float[6];

        for (int i = 0; i < 6; i++) {
            tempX[i] = (float)(x + (this.radius * Math.sin(angle * i)));
        }

        return tempX;

    }

    private float[] calculateYpoints(float y) {

        float[] tempY = new float[6];

        for (int i = 0; i < 6; i++) {
            tempY[i] = (float)(y + (this.radius * Math.cos(angle * i)));
        }

        return tempY;
    }

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
    }

    public float getHeight() {
        return (hexPoints[1] - centerY) * 2;
    }

    public Atom getAtom()
    {
        return this.atom;
    }

    public void setAtom(Atom at)
    {
        this.atom = at;
    }

    @Override
    public void onClick() {

    }

    @Override
    public boolean isClicked()
    {
        if(Gdx.input.justTouched() && isHoveredOver())
        {

            if((atom == null)&&(!Game.debugMode)&&(!hasBeenChanged)) // if adding an atom
            {
                grid.moveAtom(this);
                return !clickToggle;
            }
            else if ((atom!=null)&&(!Game.debugMode)&&(!hasBeenChanged))// if removing an atom
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

        return isInside(curX, curY);
    }

    public boolean isInside(float x, float y) { // Point in polygon algorithm.
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

    public float getCenterX()
    {
        return this.centerX;
    }
    public float getCenterY()
    {
        return this.centerY;
    }


    @Override
    public float[] getCentre() {
        return new float[] {this.centerX, this.centerY};
    }

    @Override
    public float[] getCoordinates() {
        return this.hexPoints;
    }

    @Override
    public void getCollision() {

    }

    public boolean containsVertex(float[] vertex) {
        for (int i = 0; i < hexPoints.length; i += 2) {
            if (vertex[0] == hexPoints[i] && vertex[1] == hexPoints[i + 1]) {
                return true;
            }
        }

        return false;

    }

    @Override
    public void Draw(ShapeRenderer shape) {

        shape.begin(ShapeRenderer.ShapeType.Line);;
        color = Color.WHITE;
        if(isBorder)
        {
            color = Color.VIOLET;
        }
        if(isNeighbour)
        {
            color = Color.GREEN;
        }
        if(atom != null)
        {
            color = Color.WHITE;
        }
        shape.setColor(color);
        shape.polygon(hexPoints);
        shape.end();

        for (int i = 0; i < borders.size()-1; i++)
        {
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
}