package com.badlogic.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public interface Entities {
    float[] getCentre();
    float[] getCoordinates();
    void getCollision();
    void Draw(ShapeRenderer shape);
    void update();
}
