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
    private OrthographicCamera camera;
    ShapeRenderer shape;
    HexagonGrid hex;

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
        float w = Gdx.graphics.getWidth();
        float h =  Gdx.graphics.getHeight();

        //cam = new PerspectiveCamera();
        //viewport = new FitViewport(windowWidth, windowHeight, cam);
        //cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
        //cam.update();

        camera = new OrthographicCamera(800, 800 * (h / w));
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        batch = new SpriteBatch();
        shape = new ShapeRenderer();

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);


        hex = new HexagonGrid();
        hex.buildHexBoard();
    }

    @Override
    public void render () {

        // ------ Update ------ //test
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //cam.update();


        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shape.setProjectionMatrix(camera.combined);

        hex.Draw(shape);
        hex.update();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose () {
        shape.dispose();
        batch.dispose();
        font.dispose();
        stage.dispose();
        skin.dispose();
    }

}