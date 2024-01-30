package com.badlogic.game;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Hexagon implements Entities{
    float startX;
    float startY;
    private static final float RADIUS = 50;

    public Hexagon(float x, float y) {
        this.startX = x;
        this.startY = y;

    }

    public float getStartX()
    {
        return this.startX;
    }

    public float getStartY()
    {
        return this.startY;
    }

    protected void setStartX(float x){
        this.startX = x;
    }

    protected void setStartY (float y){
        this.startY = y;
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
    public void Draw() {

    }
}
