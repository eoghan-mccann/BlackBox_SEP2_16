package com.badlogic.game;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Hexagon implements Entities{
    float startX, startY;
    float centreX, centreY;
    private static final float RADIUS = 50;
    boolean atomPlaced = false;
    boolean auraActive = false;


    public Hexagon(float x, float y) {
        this.startX = x;
        this.startY = y;
        setCentre(x, y);
    }




    public float getStartX()
    {
        return this.startX;
    }

    public float getStartY()
    {
        return this.startY;
    }

    public void setStartX(float x){
        this.startX = x;
    }

    public void setStartY (float y){
        this.startY = y;
    }

    public void setCentre(float x, float y) {
        centreX = x+43.3f;
        centreY = y-25f;
    }


    @Override
    public void getCentre() {

    }

    @Override
    public float[] getCoordinates() {
        return new float[0];
    }

    @Override
    public void getPosition() {

    }

    @Override
    public void getCollision() {

    }

    @Override
    public void Draw(ShapeRenderer shape) {

    }

    @Override
    public void update() {

    }
}
