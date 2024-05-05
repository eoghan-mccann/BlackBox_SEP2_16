package com.badlogic.game.UI;

import com.badlogic.game.RayMarker;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class InfoLegend implements Renderable {
    float xPos;
    float yPos;
    float width;
    float height;
    boolean isVisible;

    Label title;
    Label header;

    public InfoLegend(float x, float y) {
        width = 175;
        height = 140;

        title = new Label(null, 0,0);
        title.setFontSize(18);
        title.setVisible(true);
        title.setColor(Color.WHITE);

        header = new Label(null,0,0);
        header.setVisible(true);
        header.setFontSize(30);
        header.setColor(Color.WHITE);
        header.setText("Legend");

        isVisible = false;

        xPos = x;
        yPos = y;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void Draw(ShapeRenderer shape, SpriteBatch batch) {
        if (isVisible) {
            shape.begin(ShapeRenderer.ShapeType.Line);
            shape.setColor(Color.PINK);
            shape.box(xPos, yPos, 0, width, height, 0);
            shape.end();

            RayMarker.Result[] result = RayMarker.Result.values();
            for (int i = 0; i < result.length; i++) {
                shape.begin(ShapeRenderer.ShapeType.Filled);
                shape.setColor(result[i].getColor());
                shape.circle(xPos + 20, yPos + 30 * (i + 1), 10);
                shape.end();

                title.batch = batch;
                title.setPos(xPos + 40, yPos + 30 * (i + 1) + title.getTextHeight() / 2);
                title.setText(String.valueOf(result[i]));
                title.Draw(shape);

                header.batch = batch;
                header.setPos(xPos + width / 2 - header.getTextWidth() / 2, yPos + height + 40);
                header.Draw(shape);
            }
        }
    }
}
