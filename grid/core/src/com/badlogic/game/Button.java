package com.badlogic.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class Button implements Clickable, Entities{

    float X, Y;   // bottom left point
    float width, height;
    float textWidth, textHeight;
    boolean toggle = false;
    Color color;

    private BitmapFont font;
    private String text;

    private SpriteBatch batch;
    GlyphLayout layout = new GlyphLayout();


    public Button(SpriteBatch batch,float x, float y, float w, float h) {
        this.batch = batch;

        X = x;
        Y = y;
        width = w;
        height = h;

        color = Color.PINK;
        font = new BitmapFont();

        setText("Button");
        setFontSize(1f);
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

    public void Draw(ShapeRenderer shape) {
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(color);
        shape.rect(X, Y, width, height);
        shape.end();

        batch.begin();
            font.setColor(Color.BLACK);
            font.draw(batch, text, X + width / 2 - textWidth / 2, Y + height / 2 + textHeight / 2);
        batch.end();
    }

    public void setText(String newText) {
        this.text = newText;

        layout.setText(font, text);
        textWidth = layout.width;// contains the width of the current set text
        textHeight = layout.height; // contains the height of the current set text
    }

    public void setFontSize(float scale) {
        font.getData().setScale(scale);
        setText(text); // adjusts the text height/width post-scale
    }

    public boolean getClicked() {
        return toggle;
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


    @Override
    public void getCollision() {

    }

}
