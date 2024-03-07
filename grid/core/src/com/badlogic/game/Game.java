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


    //used for game logic
    public static boolean debugMode = false;

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
        userMessage.showWelcomeMessage("Welcome, time traveller!",
                "The Pookies welcome you to a refreshing game of BlackBox. " +
                        "\n Press Enter on your keyboard to start the game :) ");

    }

    boolean atomMessDisp = false;
    boolean rayMessDisp = false;
    public void update() {
        if (!userMessage.isWaitingForInput()) {
            switch (currentPhase) {
                case PLACING_ATOMS:
                    rayMessDisp = false;
                    // Display message for the atom phase only once when transitioning to this phase
                    if (!userMessage.isWaitingForInput() && !atomMessDisp){
                        userMessage.showWelcomeMessage("\t\t\t Atom Phase", "You are now in the atom placement phase. \n\n \t\tPress ENTER to start.");
                        atomMessDisp = true;
                    }
                    for (Atom atoms : placedAtoms) {
                        atoms.update();
                    }
                    break;
                case PLACING_RAYS:
                    atomMessDisp = false;
                    // Display message for the ray phase only once when transitioning to this phase
                    if (!userMessage.isWaitingForInput() && !rayMessDisp){
                        userMessage.showWelcomeMessage("Ray Phase", "You are now in the ray phase. Place rays to solve the puzzle.");
                        rayMessDisp = true;
                    }
                    break;
            }
        }

        if(viewToggle.isClicked()) {
            debugMode = !debugMode;
            if (currentPhase == GamePhase.PLACING_ATOMS){
                currentPhase = GamePhase.PLACING_RAYS;
                debugMode = true;
                //System.out.println(debugMode);
            }
            else {
                currentPhase = GamePhase.PLACING_ATOMS;
                debugMode = false;
                //System.out.println(debugMode);
            }
        }

        debugUpdate();
    }

    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shape.setProjectionMatrix(camera.combined);

        hexagonGrid.update();
        hexagonGrid.Draw(shape);

        viewToggle.update();
        viewToggle.Draw(shape);

        for (Atom atom : placedAtoms) {
            atom.Draw(shape);
        }

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

    private void debugUpdate() {
            for (Atom atoms : hexagonGrid.atoms) {
                atoms.debug = debugMode;
            }

            for (Hexagon hexagon : hexagonGrid.getHexBoard()) {
                for (Border border : hexagon.borders) {
                    border.debug = debugMode;
                }
            }
            for (Ray2 ray : hexagonGrid.rays) {
                ray.debug = debugMode;
            }
        }
}
