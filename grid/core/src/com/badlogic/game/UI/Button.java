package com.badlogic.game.UI;

import com.badlogic.game.Clickable;
import com.badlogic.game.Entities;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class Button implements Clickable, Entities {

    float X, Y;   // bottom left point
    float width, height;
    boolean toggle = false;
    Color color;

    private final Label label;


    public Button(SpriteBatch batch,float x, float y, float w, float h) {
        X = x;
        Y = y;
        width = w;
        height = h;

        color = Color.PINK;

        label = new Label(batch, X, Y);
        label.setColor(Color.BLACK);
    }


    @Override
    public boolean isClicked() {
        return Gdx.input.justTouched() && isHoveredOver();
    }

    @Override
    public boolean isHoveredOver() {
        float curX = Gdx.input.getX();
        float curY = Gdx.graphics.getHeight() - Gdx.input.getY();

        return contains(curX, curY);
    }

    private boolean contains(float x, float y) {
        boolean isInside = false;

        if((x > X  && x < X+width )  &&  (y > Y  &&  y < Y+height)) {
            isInside = !isInside;
        }

        return isInside;

    }

    public void Draw(ShapeRenderer shape) {
        shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.setColor(color);
            shape.rect(X, Y, width, height);
        shape.end();

        label.Draw(shape);
    }

    public void setText(String newText) {
        label.setText(newText);
        label.setPos(X + width / 2 - label.getTextWidth() / 2, Y + height / 2 + label.getTextHeight() / 2);
    }

    public void setFontSize(int scale) {
        label.setFontSize(scale);
        label.setPos(X + width / 2 - label.getTextWidth() / 2, Y + height / 2 + label.getTextHeight() / 2);
    }

    @Override
    public void update() {

        if(isClicked())
        {
            toggle = !toggle;
        }
        if(toggle)
        {
            color = Color.CYAN;
        }
        else { color = Color.PINK; }

    }

    @Override
    public float[] getCentre() {
        return new float[0];
    }

    @Override
    public float[] getCoordinates() {
        return new float[0];
    }

}
