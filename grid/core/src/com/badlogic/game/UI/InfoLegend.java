package com.badlogic.game.UI;

import com.badlogic.game.RayMarker;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class InfoLegend {
    float xPos;
    float yPos;
    float width;
    float height;

    Label title;
    Label header;

    public InfoLegend(float x, float y) {
        width = 175;
        height = 140;

        title = new Label(null, 0,0);
        title.setFontSize(18);
        title.setColor(Color.WHITE);

        header = new Label(null,0,0);
        header.setFontSize(30);
        header.setColor(Color.WHITE);
        header.setText("Legend");

        xPos = x;
        yPos = y;
    }

    public void Draw(ShapeRenderer shape, SpriteBatch batch) {
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(Color.PINK);
        shape.box(xPos,yPos,0,width,height,0);
        shape.end();

        RayMarker.Result result[] = RayMarker.Result.values();
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
