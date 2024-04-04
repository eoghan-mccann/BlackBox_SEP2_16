package com.badlogic.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game {

    public static int windowWidth = 1600;
    public static int windowHeight = 900;
    public static float hexRadius = 50;


    //used for game logic
    public static boolean debugMode = false;

    private final OrthographicCamera camera;

    public SpriteBatch batch;
    private final Stage stage;
    private final Skin skin;
    private final UserMessage userMessage;
    private final Button viewToggle;
    private Button atomConfirmButton;
    private final ShapeRenderer shape;

    private enum GamePhase {
        PLACING_ATOMS,
        PLACING_RAYS,
    }

    private final HexagonGrid hexagonGrid;
    private GamePhase currentPhase;

    public Game() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        hexagonGrid = new HexagonGrid();
        hexagonGrid.buildHexBoard();
        hexagonGrid.getBorderHexagons();
        hexagonGrid.initAtoms();
        hexagonGrid.setAtomsVisible(true);
        hexagonGrid.activateBorders();

        currentPhase = GamePhase.PLACING_ATOMS;

        camera = new OrthographicCamera(800, 800 * (h / w));
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        viewToggle = new Button(batch, 50, 50, 175, 75);
        viewToggle.setText("Debug View WIP");
        viewToggle.setFontSize(1.1f);

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
                    // Display message for the atom phase
                    if (!userMessage.isWaitingForInput() && !atomMessDisp){
                        userMessage.showWelcomeMessage("\t\t\t Atom Phase", "You are now in the atom placement phase. \n\n \t\tPress ENTER to start.");
                        atomMessDisp = true;
                    }

                    // Spawn confirm selection button if all atoms placed, remove if an atom gets removed
                    if (hexagonGrid.allAtomsPlaced() && atomConfirmButton == null) {
                        atomConfirmButton = new Button(batch, 1350, 50, 200, 100);
                        atomConfirmButton.setText("Confirm Atom Placement");
                        atomConfirmButton.setFontSize(1.15f);

                    } else if (!hexagonGrid.allAtomsPlaced()) {
                        atomConfirmButton = null;
                    }

                    // if clicked remove button and move to next phase
                    if (atomConfirmButton != null && atomConfirmButton.isClicked()) {
                        atomConfirmButton = null;
                        currentPhase = GamePhase.PLACING_RAYS;
                    }

                    break;
                case PLACING_RAYS:
                    hexagonGrid.setAtomsVisible(false);
                    hexagonGrid.setHexClickable(false);
                    hexagonGrid.setRayVisible(true); // rays visible for now until markings made
                    hexagonGrid.setBorderClickable(true);

                    atomMessDisp = false;
                    // Display message for the ray phase
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

        if (atomConfirmButton != null) {
            atomConfirmButton.update();
            atomConfirmButton.Draw(shape);
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
}
