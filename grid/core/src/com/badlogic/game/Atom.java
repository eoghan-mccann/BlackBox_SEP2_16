package com.badlogic.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;


//Atom implements Entities since it is an entity that is going to draw
//implements Clickable since it can be clicked (maybe not? mostly because of the toggle really)
public class Atom implements Entities, Clickable{

    private float atomCentreX, atomCentreY; // atom centre
    private float atomCentreRadius; // atom radius
    private float atomLayerRadius; // aura radius

    public boolean isPlaced;
    public boolean debug;

    private float[] centrePoint = new float[2]; // coords of atom centre

    //initialise atom
    Atom(float x, float y, float r1, float r2) {
        this.atomCentreX = x;
        this.atomCentreY = y;
        this.atomCentreRadius = r1;
        this.atomLayerRadius = r2;


        this.centrePoint[0] = this.atomCentreX;
        this.centrePoint[1] = this.atomCentreY;


        this.isPlaced = false;
        this.debug = false;
    }

    // setter methods
    public float setCenterX(float x)
    {
        return atomCentreX = x;
    }
    public float setCenterY(float y)
    {
        return atomCentreY = y;
    }
    public void setAtomPoints(float x, float y){ // sets all values relative to the given center
        this.atomCentreX = x;
        this.atomCentreY = y;

        this.centrePoint[0] = x;
        this.centrePoint[1] = y;
    }

    // checks if given ray coords are inside atom coords
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
        if(!Game.debugMode) // if toggle is off (set to atom)
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


    // accessor methods
    @Override
    public float[] getCentre() {
        return this.centrePoint;
    }

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
    public void getCollision() {

    }

    @Override
    public void update() {

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
}

