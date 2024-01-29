package com.badlogic.game;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Hexagon {
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


}
