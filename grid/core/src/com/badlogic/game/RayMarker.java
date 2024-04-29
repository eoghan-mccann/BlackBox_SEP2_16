package com.badlogic.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class RayMarker {
    public enum Result {
        HIT(Color.GREEN),
        REFLECTION(Color.CYAN),
        MISS(Color.RED),
        DEFLECTION(Color.YELLOW);

        private final Color color;

        Result(Color color) {
            this.color = color;
        }

        public Color getColor() {
            return color;
        }
    }

    float[] centerPos;
    float radius;
    Color color;
    Result result;

    RayMarker(float[] centerPos, float radius, Result result) {
        this.centerPos = centerPos;
        this.radius = radius;
        this.result = result;

        color = result.getColor();
    }

    public void Draw(ShapeRenderer shape) {
        shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.setColor(color);
            shape.circle(centerPos[0], centerPos[1], radius);
        shape.end();
    }
}
