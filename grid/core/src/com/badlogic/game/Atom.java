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


    // Checks if given coordinates are inside the atom
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
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    // Sets atom's position to x, y
    public void setAtomPoints(float x, float y){
        atomCentreX = x;
        atomCentreY = y;
    }


    // accessor methods
    @Override
    public float[] getCentre() { return new float[]{atomCentreX, atomCentreY}; }

    public float getCentreRadius(){
        return this.atomCentreRadius;
    }

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

