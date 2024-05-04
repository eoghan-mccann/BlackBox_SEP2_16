package com.badlogic.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;


public class Atom implements Entities{

    private float atomCentreX, atomCentreY;
    private float atomCentreRadius;
    private float atomLayerRadius; // Radius of atom's aura

    public boolean isPlaced;
    public boolean visible;


    Atom(float x, float y, float r1, float r2) {
        this.atomCentreX = x;
        this.atomCentreY = y;
        this.atomCentreRadius = r1;
        this.atomLayerRadius = r2;




        this.isPlaced = false;
        this.visible = false;
    }


    /**
     * Checks if the given coordinates are inside the atom.
     *
     * @param rayPos An array containing the X and Y coordinates of the point to be checked.
     *               The X coordinate is at index 0, and the Y coordinate is at index 1.
     * @return {@code true} if the given coordinates are inside the atom, {@code false} otherwise.
     */
    public boolean isInside(float[] rayPos) {
        float rayX = rayPos[0];
        float rayY = rayPos[1];

        float atomX = getCentre()[0];
        float atomY = getCentre()[1];

        // Distance between two points function
        return Math.sqrt(Math.pow(atomX - rayX, 2)) + Math.pow(atomY - rayY, 2) <= getCentreRadius();
    }


    @Override
    public void Draw(ShapeRenderer shape) {
        if(visible || Game.debugMode) // If toggle is off (set to atom)
        {
            shape.begin(ShapeRenderer.ShapeType.Line);
            // Drawing aura
            shape.setColor(Color.WHITE);
            shape.circle(getCentre()[0], getCentre()[1], getLayerRadius());
            shape.end();
            // Drawing atom
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.setColor(Color.PINK);
            shape.circle(getCentre()[0], getCentre()[1], getCentreRadius());


            shape.end();
        }


    }


    // ----     Setter methods     ----

    /**
     * Sets the visibility of an atom.
     *
     * @param vis bool for visible to be set to
     */
    public void setVisible(boolean vis) {
        this.visible = vis;
    }


    /**
     * Sets atom's position to x, y
     *
     * @param x horizontal coordinate
     * @param y vertical coordinate
     *
     */
    public void setAtomPoints(float x, float y){
        atomCentreX = x;
        atomCentreY = y;
    }


    // ----      Accessor methods      ----

    /**
     * Get the center coordinate of an Atom
     *
     * @return center coordinates as a float array, where index 0 is the x coordinate and index 1 is the y coordinate
     */
    @Override
    public float[] getCentre() { return new float[]{atomCentreX, atomCentreY}; }

    /**
     * Get the radius of an atom
     *
     * @return atom's center radius as a float
     */
    public float getCentreRadius(){
        return this.atomCentreRadius;
    }

    /**
     * Get the radius of an atom's aura.
     *
     * @return radius of atom's aura as a float
     */
    public float getLayerRadius(){
        return this.atomLayerRadius;
    }




    @Override
    public float[] getCoordinates() {
        return null;
    }

    @Override
    public void update() {}


}

