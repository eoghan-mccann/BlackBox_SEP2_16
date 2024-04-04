package com.badlogic.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Button implements Clickable, Entities{

    float X, Y;   // bottom left point
    float width, height;
    boolean toggle = false;
    Color color, color2;
    int order;

    public Button(float x, float y, float w, float h, int order) {
        X = x;
        Y = y;
        width = w;
        height = h;
        this.order =order;


        if (this.order == 0){
            color = Color.PINK;
        }
        else if (this.order == 1){
            color = Color.LIME;
        }
        else {
            color = Color.PINK;
        }
    }

    @Override
    public void onClick() {

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


    @Override
    public void Draw(ShapeRenderer shape) {
        shape.begin(ShapeRenderer.ShapeType.Filled);
        if (order == 1){
            shape.setColor(color2);
        }
        else {
            shape.setColor(color);
        }
        shape.rect(X, Y, width, height);
        shape.end();
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
            color2 = Color.MAROON;
        }
        else { color = Color.PINK; color2 = Color.LIME;}

    }

    @Override
    public float[] getCentre() {
        return new float[0];
    }

    @Override
    public float[] getCoordinates() {
        return new float[0];
    }


    @Override
    public void getCollision() {

    }

}
