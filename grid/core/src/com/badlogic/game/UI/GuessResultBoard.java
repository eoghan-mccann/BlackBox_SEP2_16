package com.badlogic.game.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GuessResultBoard {
    float xPos;
    float yPos;
    float width;
    float height;

    boolean[] results;
    Label score;

    public GuessResultBoard(float x, float y, boolean[] results) {
        width = 199;
        height = 175;

        score = new Label(null, 0,0);
        score.setFontSize(20);
        score.setColor(Color.WHITE);

        this.results = results;

        xPos = x;
        yPos = y;
    }


    public void Draw(ShapeRenderer shape, SpriteBatch batch) {
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.box(xPos,yPos,0,width,height,0);
        shape.end();

        for (int i = 0; i < results.length; i++) {
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.setColor(results[i] ? Color.GREEN : Color.RED);
            shape.circle(xPos + 20, yPos + 30 * (i + 1), 10);
            shape.end();

            score.batch = batch;
            score.setPos(xPos + 40, yPos + 30 * (i + 1) + score.getTextHeight() / 2);
            score.setText(results[i] ? "Correct" : "Incorrect");
            score.Draw(shape);
        }
    }
}
