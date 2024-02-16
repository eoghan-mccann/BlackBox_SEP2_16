package com.badlogic.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Atom implements Entities, Clickable{

    private float atomCentreX;
    private float atomCentreY;
    private float atomCentreRadius;
    private float atomLayerRadius;

    //a point on the layer that is directly to the right of the center
    private float[] layerPoint = new float[2];

    private float[] centrePoint = new float[2];
    //maybe this could be made into a 2D array since storing point coordinates?
    private float[] atomPoints;

    Atom(float x, float y, float r1, float r2) {
        this.atomCentreX = x;
        this.atomCentreY = y;
        this.atomCentreRadius = r1;
        this.atomLayerRadius = r2;
        //the X coordinate of the point is center + the radius
        this.layerPoint[0] = (this.atomCentreX + atomCentreRadius);
        //the Y coordinate of the point is the same
        this.layerPoint[1] = this.atomCentreY;
        this.centrePoint[0] = this.atomCentreX;
        this.centrePoint[1] = this.atomCentreY;

    }

    public float[] setAtomPoints(){
        float[] atomPoints = {
                atomCentreX, atomCentreY,
                this.layerPoint[0], this.layerPoint[1]
        };
        return atomPoints;
    }

    @Override
    public float[] getCentre() {
        return this.centrePoint;
    }

    @Override
    public float[] getCoordinates() {
        return this.atomPoints;
    }

    @Override
    public void getPosition() {

    }

    @Override
    public void getCollision() {

    }

    @Override
    public void Draw(ShapeRenderer shape) {
        shape.begin(ShapeRenderer.ShapeType.Line);

        // Set color for the layer (default color)
        shape.setColor(Color.WHITE);

        // Draw the layer circle unfilled
        shape.circle(this.getCentre()[0], this.getCentre()[1], atomLayerRadius);

        shape.end();

        // Begin a new batch for filled shapes
        shape.begin(ShapeRenderer.ShapeType.Filled);

        // Set color for the center (red in this case)
        shape.setColor(Color.PINK);

        // Draw the center circle filled with red color
        shape.circle(atomCentreX, atomCentreY, atomCentreRadius);

        shape.setColor(Color.WHITE);

        shape.end();

    }

    @Override
    public void update() {

    }

    public float getCentreRadius(){
        return this.atomCentreRadius;
    }

    public float getLayerRadius(){
        return this.atomLayerRadius;
    }

    public void atomRow(int n, ShapeRenderer shape){
        //the number of atoms we want for the game - 5 or 6
        int num = n;
        float centreX = this.getCentre()[0];
        float centreY = this.getCentre()[1];
        if (num<5 || num>6){
            throw new IllegalArgumentException("You can only play with 5 or 6 atoms");
        }
        for(int i=0; i<num; i++){
            Atom a = new Atom(centreX, centreY, this.getCentreRadius(), this.getLayerRadius());
            a.Draw(shape);
            centreX+=this.getCentreRadius()+(this.getLayerRadius()*2F)+5;
        }

    }

    @Override
    public void onClick() {

    }

    @Override
    public boolean isClicked() {
        return true;
    }

    @Override
    public boolean isHoveredOver() {
        return false;
    }
}

