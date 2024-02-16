package com.badlogic.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.GL20;


import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Main extends ApplicationAdapter {

    ShapeRenderer shape;
    Hexagon hex;
    public static float windowWidth;
    public static float windowHeight;
    public static float hexRadius;
    private Stage stage;
    private Skin skin;

    @Override
    public void create () { // on start
        shape = new ShapeRenderer();
        hex = new Hexagon(300,300,250);

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("rainbow/skin/rainbow-ui.json"));
        // Create a dialog with a message
        Dialog dialog = new Dialog("Message", skin);
        dialog.text("Welcome, time traveller!");
        dialog.button("fuck off fr", true); // Add an OK button
        dialog.show(stage);
    }

    @Override
    public void render () {

        // ------ Update ------
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        hex.Draw(shape);

    }

    @Override
    public void dispose () {
        shape.dispose();
    }

}