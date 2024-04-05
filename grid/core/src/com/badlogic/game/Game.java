package com.badlogic.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

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
    private Button guessConfirmButton;
    private Label guessLabel;
    private Label winLabel;
    private final ShapeRenderer shape;

    private enum GamePhase {
        DEBUG_VIEW,
        PLACING_ATOMS,
        PLACING_RAYS,
    }

    private final HexagonGrid hexagonGrid;
    private GamePhase currentPhase;
    Guess guesses;

    public Game() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        hexagonGrid = new HexagonGrid();
        hexagonGrid.buildHexBoard();
        hexagonGrid.getBorderHexagons();
        hexagonGrid.initAtoms();
        hexagonGrid.setHexClickable(false);
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
        viewToggle.setText("Debug View");
        viewToggle.setFontSize(1.1f);

        skin = new Skin(Gdx.files.internal("rainbow/skin/rainbow-ui.json"));

        userMessage = new UserMessage(stage, skin);
        userMessage.showWelcomeMessage("Welcome, time traveller!",
                "The Pookies welcome you to a refreshing game of BlackBox. " +
                        "\n Press Enter on your keyboard to start the game :) ");

        guesses = new Guess();

    }

    boolean atomMessDisp = false;
    boolean rayMessDisp = false;
    GamePhase prevPhase;
    public void update() {
        hexagonGrid.update();

        if (!userMessage.isWaitingForInput()) {
            switch (currentPhase) {
                case PLACING_ATOMS:
                    prevPhase = GamePhase.PLACING_ATOMS;
                    hexagonGrid.setBorderBoundingBoxVisible(false);
                    hexagonGrid.setAtomsVisible(true);
                    hexagonGrid.setHexClickable(true);
                    hexagonGrid.setRayVisible(false);
                    hexagonGrid.setBorderClickable(false);

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
                    prevPhase = GamePhase.PLACING_RAYS;
                    hexagonGrid.setAtomsVisible(false);
                    hexagonGrid.setHexClickable(false);
                    hexagonGrid.setRayVisible(false);
                    hexagonGrid.setBorderClickable(true);
                    hexagonGrid.setBorderBoundingBoxVisible(false);
                    hexagonGrid.setHexState(Hexagon.State.GUESSING);

                    atomMessDisp = false;
                    // Display message for the ray phase
                    if (!userMessage.isWaitingForInput() && !rayMessDisp){
                        userMessage.showWelcomeMessage("Ray Phase", "You are now in the ray phase. Place rays to solve the puzzle.");
                        rayMessDisp = true;
                    }

                    for (Hexagon hexagon : hexagonGrid.getHexBoard()) {
                        if (hexagon.isClicked()) {
                            guesses.handleGuess(hexagon);
                            System.out.println(hexagon);
                        }
                    }

                    guessLabel = new Label(batch,1150, 750);
                    guessLabel.setText("Guesses Remaining: " + guesses.getRemainingGuesses());
                    guessLabel.setFontSize(2.5f);

                    if (guesses.getRemainingGuesses() == 0 && guessConfirmButton == null) {
                        guessConfirmButton = new Button(batch, 1350, 50, 200, 100);
                        guessConfirmButton.setText("Confirm Guesses");
                        guessConfirmButton.setFontSize(1.15f);

                    } else if (!(guesses.getRemainingGuesses() == 0)) {
                        guessConfirmButton = null;
                    }

                    // if clicked remove button and move to next phase
                    if (guessConfirmButton != null && guessConfirmButton.isClicked()) {
                        guessConfirmButton = null;
                        currentPhase = GamePhase.DEBUG_VIEW;

                        winLabel = new Label(batch, 1150, 700);
                        winLabel.setFontSize(1.2f);
                        String guessString = "";

                        for (boolean guess : guesses.getGuessAnswers()) {
                            guessString += guess ? "CORRECT\n" : "INCORRECT\n";
                        }

                        winLabel.setText(guessString);

                    }

                    break;

                case DEBUG_VIEW:
                    hexagonGrid.setAtomsVisible(true);
                    hexagonGrid.setHexClickable(false);
                    hexagonGrid.setRayVisible(true);
                    hexagonGrid.setBorderBoundingBoxVisible(true);
            }
        }

        if (viewToggle.isClicked()) {
            currentPhase = currentPhase == GamePhase.DEBUG_VIEW ? prevPhase : GamePhase.DEBUG_VIEW;
        }

    }

    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shape.setProjectionMatrix(camera.combined);

        hexagonGrid.Draw(shape);

        viewToggle.update();
        viewToggle.Draw(shape);

        if (atomConfirmButton != null) {
            atomConfirmButton.update();
            atomConfirmButton.Draw(shape);
        }

        if (guessConfirmButton != null) {
            guessConfirmButton.update();
            guessConfirmButton.Draw(shape);
        }

        if (guessLabel != null) {
            guessLabel.Draw(shape);
        }

        if(winLabel != null) {
            winLabel.Draw(shape);
        }

        if(guesses != null) {
            guesses.Draw(shape,batch);
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
