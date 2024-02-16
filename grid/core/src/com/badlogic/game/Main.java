package com.badlogic.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.GL20;

public class Main extends ApplicationAdapter {

    ShapeRenderer shape;
    HexagonGrid hex;

    public static int windowWidth = 1600;
    public static int windowHeight = 900;
    public static float hexRadius = 50;


    @Override
    public void create () { // on start
        shape = new ShapeRenderer();
        hex = new HexagonGrid();
        hex.buildHexBoard();
    }

    @Override
    public void render () {

        // ------ Update ------
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        hex.Draw(shape);
        hex.update();
    }

    @Override
    public void dispose () {
        shape.dispose();
    }

}