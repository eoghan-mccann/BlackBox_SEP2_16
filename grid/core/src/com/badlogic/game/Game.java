package com.badlogic.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.List;

//we need to comment more and separate classes
public class Game {

    public static int windowWidth = 1600;
    public static int windowHeight = 900;
    public static float hexRadius = 50;


    //used for game logic
    public static boolean debugMode = false;
    private boolean lastRound;

    private final OrthographicCamera camera;

    public SpriteBatch batch;
    private final Stage stage;
    private final Skin skin;
    private final UserMessage userMessage;
    private final Button viewToggle;
    private Button atomConfirmButton;
    private Button guessConfirmButton;
    private Button newGameButton;
    private Label scoreLabel;
    private Label guessLabel;
    private Label winLabel;
    private final ShapeRenderer shape;

    int[] playerScores;

    private enum GamePhase {
        DEBUG_VIEW,
        PLACING_ATOMS,
        PLACING_RAYS,
        NEW_GAME,
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
        lastRound = false;

        playerScores = new int[2];
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
                    hexagonGrid.setHexState(Hexagon.State.PLACING);

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
                        currentPhase = GamePhase.NEW_GAME;
                        guessConfirmButton = null;

                        winLabel = new Label(batch, 1150, 700);
                        winLabel.setFontSize(1.2f);
                        String guessString = "";

                        boolean[] guessAnswers = guesses.getGuessAnswers();

                        if (!lastRound) {
                            playerScores[0] = calculateScore(guessAnswers, hexagonGrid.rays);
                        } else {
                            playerScores[1] = calculateScore(guessAnswers, hexagonGrid.rays);
                        }

                        for (boolean guess : guesses.getGuessAnswers()) {
                            guessString += guess ? "CORRECT\n" : "INCORRECT\n";
                        }

                        winLabel.setText(guessString);
                    }
                    break;

                case NEW_GAME:
                    prevPhase = GamePhase.NEW_GAME;
                    atomMessDisp = false;
                    rayMessDisp = false;

                    hexagonGrid.setRayVisible(true);
                    hexagonGrid.setAtomsVisible(true);

                    if (newGameButton == null) {
                        newGameButton = new Button(batch, 1350, 50, 200, 100);
                        newGameButton.setText(lastRound ? "End Game" : "Start Second Round");
                        newGameButton.setFontSize(1.15f);
                    }

                    if (newGameButton != null && newGameButton.isClicked()) {
                        newGameButton = null;
                        winLabel = null;
                        guessLabel = null;

                        if (!lastRound) {
                            guesses = new Guess();

                            lastRound = true;

                            hexagonGrid.resetAllRays();
                            hexagonGrid.resetAllAtoms();
                            currentPhase = GamePhase.PLACING_ATOMS;
                        } else {
                            scoreLabel = new Label(batch, 1150, 750);
                            scoreLabel.setText(getWinMessage() + "\n" +
                                    "Player 1 : " + playerScores[0] + "\n" +
                                    "Player 2 : " + playerScores[1]);
                            scoreLabel.setFontSize(2.5f);
                        }
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

        if (newGameButton != null) {
            newGameButton.Draw(shape);
        }

        if (scoreLabel != null) {
            scoreLabel.Draw(shape);
        }

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    private int calculateScore(boolean[] guesses, List<Ray2> rays) {
        int score = 0;

        for (boolean guess : guesses) {
            if (guess) { score += 5; }
        }

        score += rays.size();

        return score;
    }

    private String getWinMessage() {
        int playerOneScore = playerScores[0];
        int playerTwoScore = playerScores[1];

        if (playerOneScore == playerTwoScore) { return "TIE GAME!"; }
        else if (playerOneScore > playerTwoScore) {
            return "Player 1 Wins!";
        } else {
            return "Player 2 Wins!";
        }
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
