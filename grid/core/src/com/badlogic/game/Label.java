package com.badlogic.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class Label implements Clickable, Entities{

    float X, Y;   // bottom left point
    float width, height;
    float textWidth, textHeight;
    boolean toggle = false;
    Color color;

    private BitmapFont font;
    private String text;

    private SpriteBatch batch;
    GlyphLayout layout = new GlyphLayout();


    public Label(SpriteBatch batch,float x, float y) {
        this.batch = batch;

        X = x;
        Y = y;

        color = Color.WHITE;
        font = new BitmapFont();

        setText("Label");
        setFontSize(1f);
    }

    @Override
    public void onClick() {

    }

    @Override
    public boolean isClicked() {
        return false;
    }

    @Override
    public boolean isHoveredOver() {
        return false;
    }

    public void Draw(ShapeRenderer shape) {
        batch.begin();
            font.setColor(color);
            font.draw(batch, text, X, Y);
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

    @Override
    public void update() {

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
