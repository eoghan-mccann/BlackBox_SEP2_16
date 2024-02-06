package com.badlogic.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public interface Entities {
    public float[] getCentre();
    public float[] getCoordinates();
    public void getPosition();
    public void getCollision();
    public void Draw(ShapeRenderer shape);
    public void update();
}
