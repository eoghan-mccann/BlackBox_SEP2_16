package com.badlogic.game;

import com.badlogic.game.UI.*;
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


    //necessary for displaying and correct rendering
    public static int windowWidth = 1920;
    public static int windowHeight = 1000;
    private final OrthographicCamera camera;
    public SpriteBatch batch;
    private final Stage stage;
    private final Skin skin;
    private final ShapeRenderer shape;
    public static float hexRadius = 55;

    //used for game logic
    public static boolean debugMode = false;
    private boolean lastRound;
    int[] playerScores;

    //displaying messages, user interaction
    private final InfoLegend info;
    private GuessResultBoard guessResultBoard;
    private Scoreboard scoreboard;

    private final UserMessage userMessage;
    private final Button viewToggle;
    private Button atomConfirmButton;
    private Button guessConfirmButton;
    private Button newGameButton;
    private Label guessLabel;
    private Label winLabel;

    /**
     * this enum is used to manage the phases of the game
     * debug view - used for debugging
     * placing atoms - atom placement phase
     * placing rays - ray shooting phase
     * new game - starting new game
     */
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

        camera = new OrthographicCamera(1080, 800 * (h / w));
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        hexagonGrid = new HexagonGrid();
        hexagonGrid.buildHexBoard();
        hexagonGrid.getBorderHexagons();
        hexagonGrid.initAtoms();
        hexagonGrid.setAtomsVisible(true);
        hexagonGrid.activateBorders();


        //starting the game on the placing atoms phase
        currentPhase = GamePhase.PLACING_ATOMS;


        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        viewToggle = new Button(batch, 50, 50, 175, 75);
        viewToggle.setText("Debug View");
        viewToggle.setFontSize(20);

        skin = new Skin(Gdx.files.internal("rainbow/skin/rainbow-ui.json"));
        userMessage = new UserMessage(stage, skin);
        userMessage.showWelcomeMessage("Welcome, time traveller!",
                "The Pookies welcome you to a refreshing game of BlackBox. " +
                        "\n Press Enter on your keyboard to start the game :) ");

        guesses = new Guess();

        //game logic
        lastRound = false;

        playerScores = new int[2];

        info = new InfoLegend(50,Game.getWindowHeight() - Game.getWindowHeight() * 0.2f);
    }

    //booleans for correct message displaying
    boolean atomMessage = false;
    boolean rayMessage = false;
    GamePhase prevPhase;
    public void update() {
        float rightButtonX = Game.getWindowWidth() - Game.getWindowWidth() * 0.125f;
        float allButtonY = Game.getWindowHeight() * 0.05f;

        hexagonGrid.update();

        //logic for correct user message displaying
        if (!userMessage.isWaitingForInput()) {

            switch (currentPhase)
            {
                case PLACING_ATOMS:
                    prevPhase = GamePhase.PLACING_ATOMS;
                    hexagonGrid.setBorderBoundingBoxVisible(false);
                    hexagonGrid.setAtomsVisible(true);
                    hexagonGrid.setHexClickable(true);
                    hexagonGrid.setRayVisible(false);
                    hexagonGrid.setBorderClickable(false);
                    hexagonGrid.setHexState(Hexagon.State.PLACING);

                    rayMessage = false;
                    if (!userMessage.isWaitingForInput() && !atomMessage)
                    {
                        userMessage.showWelcomeMessage("\t\t\t Atom Phase", "You are now in the atom placement phase. \n\n \t\tPress ENTER to start.");
                        atomMessage = true;
                    }

                    if (hexagonGrid.allAtomsPlaced() && atomConfirmButton == null)
                    {
                        atomConfirmButton = new Button(batch, rightButtonX, allButtonY, 200, 100);
                        atomConfirmButton.setText("Confirm Atom Placement");
                        atomConfirmButton.setFontSize(15);

                    } else if (!hexagonGrid.allAtomsPlaced())
                    {
                        atomConfirmButton = null;
                    }

                    if (atomConfirmButton != null && atomConfirmButton.isClicked())
                    {
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

                    atomMessage = false;
                    if (!userMessage.isWaitingForInput() && !rayMessage)
                    {
                        userMessage.showWelcomeMessage("Ray Phase", "You are now in the ray phase. Place rays to solve the puzzle.");
                        rayMessage = true;
                    }

                    for (Hexagon hexagon : hexagonGrid.getHexBoard())
                    {
                        if (hexagon.isClicked())
                        {
                            guesses.handleGuess(hexagon);
                        }
                    }

                    if (guessLabel == null) {
                        guessLabel = new Label(batch, 0, 0);
                        guessLabel.setFontSize(40);
                    }

                    guessLabel.setText("Guesses Remaining: " + guesses.getRemainingGuesses());
                    guessLabel.setPos(windowWidth / 2f - guessLabel.getTextWidth() / 2, getWindowHeight() * 0.95f);

                    if (guesses.getRemainingGuesses() == 0 && guessConfirmButton == null)
                    {
                        guessConfirmButton = new Button(batch, rightButtonX, allButtonY, 200, 100);
                        guessConfirmButton.setText("Confirm Guesses");
                        guessConfirmButton.setFontSize(20);

                    } else if (!(guesses.getRemainingGuesses() == 0))
                    {
                        guessConfirmButton = null;
                    }

                    if (guessConfirmButton != null && guessConfirmButton.isClicked())
                    {
                        currentPhase = GamePhase.NEW_GAME;
                        guessConfirmButton = null;

                        boolean[] guessAnswers = guesses.getGuessAnswers();

                        if (!lastRound)
                        {
                            playerScores[0] = calculateScore(guessAnswers, hexagonGrid.rays);
                        } else {
                            playerScores[1] = calculateScore(guessAnswers, hexagonGrid.rays);
                        }

                        if (guessResultBoard == null) {
                            guessLabel = null;
                            guessResultBoard = new GuessResultBoard(rightButtonX, 155, guessAnswers);
                        }

                    }
                    break;

                case NEW_GAME:
                    prevPhase = GamePhase.NEW_GAME;
                    atomMessage = false;
                    rayMessage = false;

                    hexagonGrid.setRayVisible(true);
                    hexagonGrid.setAtomsVisible(true);

                    if (newGameButton == null)
                    {
                        newGameButton = new Button(batch, rightButtonX, allButtonY, 200, 100);
                        newGameButton.setText(lastRound ? "End Game" : "Start Second Round");
                        newGameButton.setFontSize(15);
                    }

                    if (newGameButton != null && newGameButton.isClicked())
                    {
                        newGameButton = null;
                        winLabel = null;
                        guessLabel = null;
                        guessResultBoard = null;

                        if (!lastRound)
                        {
                            guesses = new Guess();

                            lastRound = true;

                            hexagonGrid.resetAllRays();
                            hexagonGrid.resetAllAtoms();
                            currentPhase = GamePhase.PLACING_ATOMS;
                        } else {
                            if (scoreboard == null) {
                                scoreboard = new Scoreboard(rightButtonX, allButtonY * 3, playerScores[0], playerScores[1]);
                            }
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

        if (viewToggle.isClicked())
        {
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

        info.Draw(shape,batch);

        if (atomConfirmButton != null)
        {
            atomConfirmButton.update();
            atomConfirmButton.Draw(shape);
        }

        if (guessConfirmButton != null)
        {
            guessConfirmButton.update();
            guessConfirmButton.Draw(shape);
        }

        if (guessLabel != null) {guessLabel.Draw(shape);}

        if(winLabel != null) {winLabel.Draw(shape);}

        if(guesses != null) {guesses.draw(shape,batch);}

        if (newGameButton != null) {newGameButton.Draw(shape);}

        if (guessResultBoard != null) {
            guessResultBoard.Draw(shape, batch);
        }

        if (scoreboard != null) {
            scoreboard.Draw(shape,batch);
        }
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    public static int getWindowWidth() {
        return Gdx.graphics.getWidth();
    }

    public static int getWindowHeight() {
        return Gdx.graphics.getHeight();
    }

    private int calculateScore(boolean[] guesses, List<Ray> rays) {
        int score = 0;

        for (boolean guess : guesses)
        {
            if (guess) { score += 5; }
        }

        score += rays.size();

        return score;
    }

    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        stage.getViewport().update(width, height, false);
    }

    public void dispose () {
        shape.dispose();
        stage.dispose();
        skin.dispose();
    }
}
