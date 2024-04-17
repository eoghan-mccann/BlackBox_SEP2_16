package com.badlogic.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class RayMarker {

    float[] coordinates;
    float[] centerPos;
    float width;
    float height;
    float angle;

    Color color;

    RayMarker(float[] centerPos, float width, float height, float angle) {
        this.centerPos = centerPos;
        this.width = width;
        this.height = height;
        this.angle = angle;

        this.coordinates = initRectangleCoordinates(centerPos[0],centerPos[1], width, height, angle);
        color = Color.WHITE;
    }

    public static float[] initRectangleCoordinates(float centerX, float centerY, float width, float height, float angle) {
        float[] coordinates = new float[8];

        float halfWidth = width / 2;
        float halfHeight = height / 2;

        float cosAngle = (float) Math.cos(Math.toRadians(angle));
        float sinAngle = (float) Math.sin(Math.toRadians(angle));

        // Calculate coordinates of the corners of the rectangle
        coordinates[0] = centerX - halfWidth * cosAngle - halfHeight * sinAngle;
        coordinates[1] = centerY - halfWidth * sinAngle + halfHeight * cosAngle;

        coordinates[2] = centerX + halfWidth * cosAngle - halfHeight * sinAngle;
        coordinates[3] = centerY + halfWidth * sinAngle + halfHeight * cosAngle;

        coordinates[4] = centerX + halfWidth * cosAngle + halfHeight * sinAngle;
        coordinates[5] = centerY + halfWidth * sinAngle - halfHeight * cosAngle;

        coordinates[6] = centerX - halfWidth * cosAngle + halfHeight * sinAngle;
        coordinates[7] = centerY - halfWidth * sinAngle - halfHeight * cosAngle;

        return coordinates;
    }

    private float[] getCoordinates() {
        return this.coordinates;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void Draw(ShapeRenderer shape) {
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(color);
        shape.polygon(coordinates);
        shape.end();
    }


}
