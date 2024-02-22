package com.badlogic.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


/*
        Make a toggle button for atoms in the bottom left.
        When clicked, hides / shows atoms that's kinda it tbh


 */


public class Main extends ApplicationAdapter {


    ShapeRenderer shape;
    HexagonGrid hex;


    public static int windowWidth = 1600;
    public static int windowHeight = 900;
    public static float hexRadius = 50;


    private Viewport viewport;
    private OrthographicCamera camera;

    BitmapFont font;
    SpriteBatch batch;
    private Stage stage;
    private Skin skin;
    private UserMessage userMessage;
    private Label outputLabel;

    Button atomTog;

    @Override
    public void create () { // on start
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        //cam = new PerspectiveCamera();
        camera = new OrthographicCamera(800, 800 * (h / w));
        //viewport = new FitViewport(windowWidth, windowHeight, cam);
        //cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
        //cam.update();
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        batch = new SpriteBatch();
        shape = new ShapeRenderer();

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("rainbow/skin/rainbow-ui.json"));

        hex = new HexagonGrid();
        hex.buildHexBoard();
        hex.initAtoms();

        userMessage = new UserMessage(stage, skin);

        userMessage.showMessage("Welcome, time traveller!",
                "The Pookies welcome you to a refreshing game of BlackBox. " +
                        "\n Press Enter on your keyboard to start the game :) ");

        atomTog = new Button(50, 50, 100, 75);
    }

    @Override
    public void render () {

        // ------ Update ------
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shape.setProjectionMatrix(camera.combined);
        hex.update();
        atomTog.update();
        if(atomTog.isClicked())
        {
            hex.toggleAtom();
        }

        // ------ Render ------
        hex.Draw(shape);
        atomTog.Draw(shape);
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
        stage.dispose();
        skin.dispose();
    }

}