package com.badlogic.game.UI;

import com.badlogic.game.Clickable;
import com.badlogic.game.Entities;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class Label implements Clickable, Entities {

    float X, Y;   // bottom left point
    float textWidth;
    float textHeight;
    int size;
    Color color;

    private BitmapFont font;
    private String text;

    public SpriteBatch batch;
    GlyphLayout layout = new GlyphLayout();


    public Label(SpriteBatch batch,float x, float y) {
        this.batch = batch;
        this.size = 10;

        X = x;
        Y = y;

        color = Color.WHITE;
        font = generateFont();

        setText("Label");
    }

    private BitmapFont generateFont() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Nexa-Heavy.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        BitmapFont fontNew = generator.generateFont(parameter);
        generator.dispose();

        return fontNew;
    }

    public void setColor(Color color) {
        this.color = color;
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

    public void setFontSize(int size) {
        this.size = size;
        font = generateFont();
    }

    public void setPos(float x, float y) {
        X = x;
        Y = y;
    }

    public float getTextWidth() {
        layout.setText(font, text);
        return layout.width;
    }

    public float getTextHeight() {
        layout.setText(font, text);
        return layout.height;
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
