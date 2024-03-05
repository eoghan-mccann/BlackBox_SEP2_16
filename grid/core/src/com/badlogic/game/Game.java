package com.badlogic.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.ArrayList;
import java.util.List;

public class Game {

    public static int windowWidth = 1600;
    public static int windowHeight = 900;
    public static float hexRadius = 50;

    private final OrthographicCamera camera;

    SpriteBatch batch;
    private final Stage stage;
    private final Skin skin;
    private final UserMessage userMessage;
    private final Button viewToggle;
    private final ShapeRenderer shape;

    private enum GamePhase {
        PLACING_ATOMS,
        PLACING_RAYS,
    }

    private final HexagonGrid hexagonGrid;
    private List<Atom> placedAtoms;
    private List<Ray> placedRays;
    private GamePhase currentPhase;
    private Ray2 ray;


    public Game() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        hexagonGrid = new HexagonGrid();
        hexagonGrid.buildHexBoard();
        hexagonGrid.getBorderHexagons();
        hexagonGrid.initAtoms();
        hexagonGrid.activateBorders();

        placedAtoms = new ArrayList<>();
        placedRays = new ArrayList<>();
        currentPhase = GamePhase.PLACING_ATOMS;

        camera = new OrthographicCamera(800, 800 * (h / w));
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        viewToggle = new Button(50, 50, 100, 75);

        skin = new Skin(Gdx.files.internal("rainbow/skin/rainbow-ui.json"));

        userMessage = new UserMessage(stage, skin);
        userMessage.showMessage("Welcome, time traveller!",
                "The Pookies welcome you to a refreshing game of BlackBox. " +
                        "\n Press Enter on your keyboard to start the game :) ");




        // temp (just showing how rays work)
        ray = new Ray2(400, 450,  Ray2.Direction.E);
        hexagonGrid.rays.add(ray);
    }

    public void update() {
        switch (currentPhase) {
            case PLACING_ATOMS:
                for (Atom atoms : placedAtoms) {
                    atoms.update();
                }
                break;
            case PLACING_RAYS:
                break;
        }
    }

    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shape.setProjectionMatrix(camera.combined);

        hexagonGrid.update();
        viewToggle.update();
        viewToggle.Draw(shape);

        if(viewToggle.isClicked())
        {
            debugState();
        }

        hexagonGrid.Draw(shape);

        for (Atom atom : placedAtoms) {
            atom.Draw(shape);
        }

        hexagonGrid.rayCheck(ray);
        ray.update();
        ray.Draw(shape);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        stage.getViewport().update(width, height, true);
    }

    public void dispose () {
        shape.dispose();
        stage.dispose();
        skin.dispose();
    }

    private void debugState() {
        hexagonGrid.toggleAtom();

        for (Hexagon hexagon : hexagonGrid.getHexBoard()) {
            for (Border border : hexagon.borders) {
                border.debug = !border.debug;
            }
        }
    }


}
