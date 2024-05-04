package com.badlogic.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public interface Entities {

    /**
     * Get the center coordinates of an Entity. May be equivalent to getCoordinates() if implemented accordingly.
     *
     * @return The center coordinates of an Entity
     */
    float[] getCentre();

    /**
     * Get the coordinates of an Entity. May be equivalent to getCentre() if implemented accordingly.
     *
     * @return Coordinates of an Entity as a float array
     */
    float[] getCoordinates();
    /**
     * Draws Entities using the ShapeRenderer utility to draw simple polygonal objects.
     *
     * @param shape ShapeRenderer utility object.
     */
    void Draw(ShapeRenderer shape);

    /**
     * Runs, on each frame, the contents of its method.
     */
    void update();
}
