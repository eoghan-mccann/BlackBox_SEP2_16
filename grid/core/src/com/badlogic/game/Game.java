package com.badlogic.game;

import com.badlogic.game.UI.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.ArrayList;
import java.util.List;

//we need to comment more and separate classes
public class Game {
    //necessary for displaying and correct rendering
    public final OrthographicCamera camera;
    public SpriteBatch batch;
    public final Stage stage;
    private final Skin skin;
    public final ShapeRenderer shape;

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

    GameRenderer gameRenderer;

    /**
     * this enum is used to manage the phases of the game
     * debug view - used for debugging
     * placing atoms - atom placement phae
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

        hexagonGrid = new HexagonGrid((float)(w * 0.03));
        hexagonGrid.buildHexBoard(hexagonGrid);
        hexagonGrid.getBorderHexagons(hexagonGrid);
        hexagonGrid.initAtoms(hexagonGrid);
        hexagonGrid.setAtomsVisible(true);
        hexagonGrid.activateBorders(hexagonGrid);


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

        info = new InfoLegend(45,Game.getWindowHeight() - Game.getWindowHeight() * 0.2f);
        gameRenderer = new GameRenderer(this);
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
                    guessLabel.setPos(getWindowWidth() / 2f - guessLabel.getTextWidth() / 2, getWindowHeight() * 0.95f);

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
        gameRenderer.render();
    }

    public static int getWindowWidth() {
        return Gdx.graphics.getWidth();
    }

    public static int getWindowHeight() {
        return Gdx.graphics.getHeight();
    }

    public List<Object> getEntityObjects() {
        List<Object> entityObjects = new ArrayList<>();
        entityObjects.add(hexagonGrid);
        return entityObjects;
    }

    public List<Object> getUIObjects() {
        List<Object> uiObjects = new ArrayList<>();
        uiObjects.add(info);
        uiObjects.add(guessResultBoard);
        uiObjects.add(scoreboard);
        uiObjects.add(userMessage);
        uiObjects.add(viewToggle);
        uiObjects.add(atomConfirmButton);
        uiObjects.add(guessConfirmButton);
        uiObjects.add(newGameButton);
        uiObjects.add(guessLabel);
        uiObjects.add(winLabel);
        uiObjects.add(guesses);
        return uiObjects;
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
