package com.badlogic.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;


//Atom implements Entities since it is an entity that is going to draw
//implements Clickable since it can be clicked (maybe not? mostly because of the toggle really)
public class Atom implements Entities, Clickable{

    //X coordinate for the centre point of the atom
    private float atomCentreX;
    //Y coordinate for the centre point of the atom
    private float atomCentreY;
    //radius for the atom nucleus
    private float atomCentreRadius;
    //radius for the field of influence
    private float atomLayerRadius;

    //a point on the layer that is directly to the right of the center
    //Y coord is the same as for the center
    //X coord is centreX + radius
    private float[] layerPoint = new float[2];

    //array storing the centre of the atom in the sense that centre[X],[Y] is stored here
    private float[] centrePoint = new float[2];
    //maybe this could be made into a 2D array since storing point coordinates?
    //array of all points in the atom
    private float[] atomPoints;


    //initialise atom
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


    //setter method
    public float[] setAtomPoints(float x1, float y1){
        //takes the new centre, sets the centre to be the new centre
        //sets the layer point to be x of new centre + radius, y of new centre
        float[] atomPoints = {
                x1, y1,
                x1+this.atomCentreRadius, y1
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
        //check if the number of atoms we want is correct
        if (num<5 || num>6){
            throw new IllegalArgumentException("You can only play with 5 or 6 atoms");
        }
        //render 5 or 6 atoms to show how many atoms are left to be placed
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

