package com.badlogic.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Main extends ApplicationAdapter {

    ShapeRenderer shape;
    HexagonGrid hex;
    public Atom[] atoms;

    public static int windowWidth = 1600;
    public static int windowHeight = 900;
    public static float hexRadius = 50;


    private Viewport viewport;
    private Camera cam;

    BitmapFont font;
    SpriteBatch batch;
    private Stage stage;
    private Skin skin;

    @Override
    public void create () { // on start

        cam = new PerspectiveCamera();
        viewport = new FitViewport(windowWidth, windowHeight, cam);
        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
        cam.update();

        batch = new SpriteBatch();
        shape = new ShapeRenderer();

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);


        hex = new HexagonGrid();
        hex.buildHexBoard();
        hex.initAtoms();


    }

    @Override
    public void render () {

        // ------ Update ------
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        cam.update();
        hex.update();


        hex.Draw(shape);

    }

    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose () {
        shape.dispose();
    }

}