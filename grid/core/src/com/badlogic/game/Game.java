package com.badlogic.game;

import com.badlogic.game.UI.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
//import sun.jvm.hotspot.opto.Phase;

import java.util.ArrayList;
import java.util.List;

//we need to comment more and separate classes

/**
 * This class represents Model in HLD.
 */
public class Game {
    //necessary for displaying and correct rendering
    private final OrthographicCamera camera;
    private final SpriteBatch batch;
    private final Stage stage;
    private final ShapeRenderer shape;

    //used for game logic
    public static boolean debugMode = false;
    int[] playerScores;

    //displaying messages, user interaction
    private final InfoLegend info;
    private final GuessResultBoard guessResultBoard;
    private final Scoreboard scoreboard;

    private final UserMessage userMessage;
    private final Button viewToggle;
    private final Button atomConfirmButton;
    private final Button guessConfirmButton;
    private final Button newGameButton;
    private final Label guessLabel;
    private final Label winLabel;

    GameRenderer gameRenderer;
    GameLogic gameLogic;

    /**
     * this enum is used to manage the phases of the game
     * debug view - used for debugging
     * placing atoms - atom placement phae
     * placing rays - ray shooting phase
     * new game - starting new game
     */
    enum GamePhase {
        DEBUG_VIEW,
        PLACING_ATOMS,
        PLACING_RAYS,
        NEW_GAME,
    }

    private GamePhase currentPhase;

    public SpriteBatch getBatch() {
        return batch;
    }

    public ShapeRenderer getShapeRenderer() {
        return shape;
    }

    public Stage getStage() {
        return stage;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public Game() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera(1080, 800 * (h / w));
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        //starting the game on the placing atoms phase
        currentPhase = GamePhase.PLACING_ATOMS;

        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        stage = new Stage();

        Gdx.input.setInputProcessor(stage);

        viewToggle = new Button(batch, 50, 50, 175, 75);
        viewToggle.setText("Debug View");
        viewToggle.setFontSize(20);

        Skin skin = new Skin(Gdx.files.internal("rainbow/skin/rainbow-ui.json"));
        userMessage = new UserMessage(stage, skin);
        userMessage.showWelcomeMessage("Welcome, time traveller!",
                "The Pookies welcome you to a refreshing game of BlackBox. " +
                        "\n Press Enter on your keyboard to start the game :) ");

        gameRenderer = new GameRenderer(this);
        gameLogic = new GameLogic(this);

        float rightButtonX = GameRenderer.getWindowWidth() - GameRenderer.getWindowWidth() * 0.125f;
        float allButtonY = GameRenderer.getWindowHeight() * 0.05f;

        info = new InfoLegend(45,GameRenderer.getWindowHeight() - GameRenderer.getWindowHeight() * 0.2f);
        guessLabel = new Label(batch, 0, 0);
        guessConfirmButton = new Button(batch, rightButtonX, allButtonY, 200, 100);
        guessResultBoard = new GuessResultBoard(rightButtonX, 155, null);
        newGameButton = new Button(batch, rightButtonX, allButtonY, 200, 100);
        atomConfirmButton = new Button(batch, rightButtonX, allButtonY, 200, 100);
        scoreboard = new Scoreboard(rightButtonX, allButtonY * 3, 0, 0);
        winLabel = new Label(batch, 100,100);
    }

    //booleans for correct message displaying
    boolean atomMessage = false;
    boolean rayMessage = false;
    GamePhase prevPhase;


    public void update() {
        gameLogic.HexagonGridUpdate(info, viewToggle);

        //logic for correct user message displaying
        switch (currentPhase) {
            case PLACING_ATOMS:
                prevPhase = GamePhase.PLACING_ATOMS;
                rayMessage = false;

                if (!userMessage.isWaitingForInput() && !atomMessage) {
                    userMessage.showWelcomeMessage("\t\t\t Atom Phase", "You are now in the atom placement phase. \n\n \t\tPress ENTER to start.");
                    atomMessage = true;
                }

                gameLogic.updatePlacingAtomsPhase(atomConfirmButton);
                break;
            case PLACING_RAYS:
                prevPhase = GamePhase.PLACING_RAYS;
                atomMessage = false;

                if (!userMessage.isWaitingForInput() && !rayMessage) {
                    userMessage.showWelcomeMessage("Ray Phase", "You are now in the ray phase. Place rays to solve the puzzle.");
                    rayMessage = true;
                }

                gameLogic.updatePlacingRayPhase(guessLabel, guessConfirmButton, guessResultBoard);
                break;
            case NEW_GAME:
                prevPhase = GamePhase.NEW_GAME;
                atomMessage = false;
                rayMessage = false;
                gameLogic.updateNewGamePhase(newGameButton, winLabel, guessLabel, guessResultBoard, scoreboard);
                break;
            case DEBUG_VIEW:
                gameLogic.updateDebugPhase();
        }

        if (viewToggle.isClicked()) {
            currentPhase = currentPhase == GamePhase.DEBUG_VIEW ? prevPhase : GamePhase.DEBUG_VIEW;
        }
    }

    public void render() {
        gameRenderer.render();
    }

    public List<Object> getEntityObjects() {
        // All Entities drawn within hexagonGrid.
        List<Object> entityObjects = new ArrayList<>();
        entityObjects.add(gameLogic.getHexagonGrid());
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
        uiObjects.add(gameLogic.getGuessObject());
        return uiObjects;
    }

    public void setCurrentPhase(GamePhase phase) {
        this.currentPhase = phase;
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
        gameRenderer.dispose();
    }
}
