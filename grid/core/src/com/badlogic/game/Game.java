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
    public static boolean debugMode = false; //false = atom mode, true = ray mode
    public static boolean hasBeenChanged = false;
    public static boolean newGame = false;
    public static boolean playerDone = false;

    public static boolean playerWasChanged = false;
    public static int numberOfPlayersDone = 0;
    //goes up to 2
    //counter for checking if both players are done with the game

    private final OrthographicCamera camera;

    SpriteBatch batch;
    private final Stage stage;
    private final Skin skin;
    private final UserMessage userMessage;
    private final Button viewToggle;

    private final Button playerPhase;
    private final ShapeRenderer shape;

    private enum GamePhase {
        PLACING_ATOMS,
        PLACING_RAYS,
        DONE_GUESSING,
        //displays user score
        //if both players are done guessing display final scoring
        NEW_GAME
        //resets the atoms to their original positions
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

        viewToggle = new Button(50, 50, 100, 75, 0);
        playerPhase = new Button(1250, 50, 100, 75, 1);



        skin = new Skin(Gdx.files.internal("rainbow/skin/rainbow-ui.json"));

        userMessage = new UserMessage(stage, skin);
        userMessage.showWelcomeMessage("\t\t\t\tWelcome, time traveller!",
                "The Pookies welcome you to a refreshing game of BlackBox. " +
                        "\n Press Enter on your keyboard to start the game :) ");

    }

    boolean atomMessDisp = false;
    boolean rayMessDisp = false;
    boolean doneGuessingMessDisp = false;
    boolean newGameMessDisp = false;


    public void update() {

        if (!userMessage.isWaitingForInput()) { //needed for message displaying

            switch (currentPhase) {

                case PLACING_ATOMS: //first phase of the game
                    rayMessDisp = false;
                    newGame = false;
                    // Display message for the atom phase
                    if (!userMessage.isWaitingForInput() && !atomMessDisp && !hasBeenChanged){
                        userMessage.showWelcomeMessage("\t\t\t Atom Phase", "You are now in the atom placement phase. \n\nPress ENTER to start.\n\nPlease note that if you click the left hand-side button again, \nyou will not be able to place atoms.");
                        atomMessDisp = true;
                    }
                    if (!userMessage.isWaitingForInput() && !atomMessDisp && hasBeenChanged){
                        userMessage.showWelcomeMessage("\t\t\t Atom Phase", "You are now in the atom placement phase,\n\nbut you are not be able to place atoms.");
                        atomMessDisp = true;
                    }
                    for (Atom atoms : placedAtoms) { //placing the atoms
                        atoms.update();
                    }
                    break;
                case PLACING_RAYS:
                    atomMessDisp = false;
                    // Display message for the ray phase
                    if (!userMessage.isWaitingForInput() && !rayMessDisp){
                        userMessage.showWelcomeMessage("\t\t\t\t\t  Ray Phase", "You are now in the ray phase. Place rays to solve the puzzle. \nYou are not able to place atoms.");
                        rayMessDisp = true;
                        hasBeenChanged = true;
                    }
                    break;
                case DONE_GUESSING:
                    atomMessDisp = false;
                    rayMessDisp = false;
                    // Display message for the done guessing phase
                    if (!userMessage.isWaitingForInput() && !doneGuessingMessDisp){
                        userMessage.showWelcomeMessage("\t\tDone Guessing", " You are now finished guessing. ");
                        doneGuessingMessDisp = true;
                    }
                    break;
                case NEW_GAME:
                    if (!userMessage.isWaitingForInput() && !newGameMessDisp){
                        userMessage.showWelcomeMessage("\tNew Game", " New Game Starts ");
                        newGameMessDisp = true;
                    }
                    atomMessDisp = false;
                    rayMessDisp = false;
                    doneGuessingMessDisp = false;
                    hasBeenChanged = false;
                    debugMode = false;
//                    playerWasChanged = true;

                    currentPhase = GamePhase.PLACING_ATOMS;
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

        if (playerPhase.isClicked()) {
            playerDone = !playerDone; // Toggle the playerDone variable
            if (playerDone) {
                currentPhase = GamePhase.DONE_GUESSING; // Set the currentPhase to DONE_GUESSING if playerDone is true
                newGame = false;
                numberOfPlayersDone+=1;
            } else {
                if (numberOfPlayersDone>2){
                    throw new IllegalArgumentException("too many players"); //>2 as we need to display  scoring for 2nd user too
                }
                currentPhase = GamePhase.NEW_GAME; // Set the currentPhase to NEW_GAME if playerDone is false
                newGame = true; // Set the newGame variable to true
                playerWasChanged = true;
                for(Ray2 ray: hexagonGrid.rays)
                {
                    ray.dead = true;
                }
            }
            System.out.println(playerWasChanged);
            System.out.println("player was changed");
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

        playerPhase.update();
        playerPhase.Draw(shape);

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
