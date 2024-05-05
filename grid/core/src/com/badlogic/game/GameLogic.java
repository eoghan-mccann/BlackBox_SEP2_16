package com.badlogic.game;

import com.badlogic.game.UI.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.List;

/**
 * The class responsible for game logic.
 * It handles phases of the game.
 * In HLD, it would represent the controller.
 */
public class GameLogic {
    //used for game logic
    int[] playerScores;

    //displaying messages, user interaction

    Game game;
    HexagonGrid hexagonGrid;
    Guess guesses;
    boolean isLastRound;

    GameLogic(Game game) {
        this.game = game;

        hexagonGrid = new HexagonGrid((float)  (GameRenderer.getWindowWidth() * 0.03));
        hexagonGrid.buildHexBoard(hexagonGrid);
        hexagonGrid.getBorderHexagons(hexagonGrid);
        hexagonGrid.initAtoms(hexagonGrid);
        hexagonGrid.setAtomsVisible(true);
        hexagonGrid.activateBorders(hexagonGrid);

        guesses = new Guess();
        isLastRound = false;

        playerScores = new int[2];
    }

    /**
     *
     * @param infoLegend
     * @param viewToggle
     */
    public void HexagonGridUpdate(InfoLegend infoLegend, Button viewToggle) {
        infoLegend.setVisible(true);
        viewToggle.setVisible(true);
        hexagonGrid.update();
    }

    /**
     *
     * @param atomConfirmButton
     */
    public void updatePlacingAtomsPhase(Button atomConfirmButton) {
        hexagonGrid.setBorderBoundingBoxVisible(false);
        hexagonGrid.setAtomsVisible(true);
        hexagonGrid.setRayVisible(false);
        hexagonGrid.setBorderClickable(false);
        hexagonGrid.setHexState(Hexagon.State.PLACING);

        if (hexagonGrid.allAtomsPlaced() && !atomConfirmButton.isVisible())
        {
            atomConfirmButton.setVisible(true);
            atomConfirmButton.setText("Confirm Atom Placement");
            atomConfirmButton.setFontSize(15);

        } else if (!hexagonGrid.allAtomsPlaced()) {
            atomConfirmButton.setVisible(false);
        }

        if (atomConfirmButton.isVisible() && atomConfirmButton.isClicked())
        {
            atomConfirmButton.setVisible(false);
            game.setCurrentPhase(Game.GamePhase.PLACING_RAYS);
        }
    }

    /**
     *
     * @param guessLabel
     * @param guessConfirmButton
     * @param guessResultBoard
     */
    public void updatePlacingRayPhase(Label guessLabel, Button guessConfirmButton, GuessResultBoard guessResultBoard) {
        hexagonGrid.setAtomsVisible(false);
        hexagonGrid.setRayVisible(false);
        hexagonGrid.setBorderClickable(true);
        hexagonGrid.setBorderBoundingBoxVisible(false);
        hexagonGrid.setHexState(Hexagon.State.GUESSING);

        for (Hexagon hexagon : hexagonGrid.getHexBoard())
        {
            if (hexagon.isClicked()) {
                guesses.handleGuess(hexagon);
            }
        }

        guessLabel.setVisible(true);
        guessLabel.setFontSize(40);
        guessLabel.setText("Guesses Remaining: " + guesses.getRemainingGuesses());
        guessLabel.setPos(GameRenderer.getWindowWidth() / 2f - guessLabel.getTextWidth() / 2, GameRenderer.getWindowHeight() * 0.95f);

        if (guesses.getRemainingGuesses() == 0) {
            guessConfirmButton.setVisible(true);
            guessConfirmButton.setText("Confirm Guesses");
            guessConfirmButton.setFontSize(20);

        } else if (!(guesses.getRemainingGuesses() == 0)) {
            guessConfirmButton.setVisible(false);
        }

        if (guessConfirmButton.isVisible() && guessConfirmButton.isClicked())
        {
            game.setCurrentPhase(Game.GamePhase.NEW_GAME);
            guessConfirmButton.setVisible(false);

            boolean[] guessAnswers = guesses.getGuessAnswers();

            if (!isLastRound) {
                playerScores[0] = calculateScore(guessAnswers, hexagonGrid.rays);
            } else {
                playerScores[1] = calculateScore(guessAnswers, hexagonGrid.rays);
            }

            if (!guessResultBoard.isVisible()) {
                guessLabel.setVisible(false);
                guessResultBoard.setResults(guessAnswers);
                guessResultBoard.setVisible(true);
            }
        }
    }

    /**
     *
     * @param newGameButton
     * @param winLabel
     * @param guessLabel
     * @param guessResultBoard
     * @param scoreboard
     */
    public void updateNewGamePhase(Button newGameButton,
                                   Label winLabel,
                                   Label guessLabel,
                                   GuessResultBoard guessResultBoard,
                                   Scoreboard scoreboard) {

        hexagonGrid.setRayVisible(true);
        hexagonGrid.setAtomsVisible(true);

        if (!newGameButton.isVisible())  {
            newGameButton.setVisible(true);
            newGameButton.setText(isLastRound ? "End Game" : "Start Second Round");
            newGameButton.setFontSize(15);
        }

        if (newGameButton.isClicked()) {

            newGameButton.setVisible(false);
            winLabel.setVisible(false);
            guessLabel.setVisible(false);
            guessResultBoard.setVisible(false);

            if (!isLastRound) {
                guesses = new Guess();

                isLastRound = true;

                hexagonGrid.resetAllRays();
                hexagonGrid.resetAllAtoms();
                game.setCurrentPhase(Game.GamePhase.PLACING_ATOMS);
            } else {
                if (!scoreboard.isVisible()) {
                    scoreboard.setScore(playerScores[0], playerScores[1]);
                    scoreboard.setVisible(true);
                }
            }
        }
    }

    /**
     *
     */
    public void updateDebugPhase() {
        hexagonGrid.setAtomsVisible(true);
        hexagonGrid.setRayVisible(true);
        hexagonGrid.setBorderClickable(false);
        hexagonGrid.setBorderBoundingBoxVisible(true);
    }

    /**
     * Method for calculating the player scores.
     * @param guesses
     * @param rays
     * @return
     */
    private int calculateScore(boolean[] guesses, List<Ray> rays) {
        int score = 0;

        for (boolean guess : guesses)
        {
            if (guess) { score += 5; }
        }

        score += rays.size();

        return score;
    }

    public HexagonGrid getHexagonGrid() {
        return hexagonGrid;
    }

    public Guess getGuessObject() {
        return guesses;
    }

}
