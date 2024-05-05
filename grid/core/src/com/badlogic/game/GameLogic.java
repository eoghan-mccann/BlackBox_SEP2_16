package com.badlogic.game;

import com.badlogic.game.UI.*;

import java.util.List;

/**
 * The class responsible for game logic.
 * It handles phases of the game.
 * In HLD, it would represent Controller.
 */
public class GameLogic {

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
     * Calls the update() method for {@code HexagonGrid}.
     * @param infoLegend legend containing ray output information
     * @param viewToggle Button object which toggles player view
     */
    public void HexagonGridUpdate(InfoLegend infoLegend, Button viewToggle) {
        infoLegend.setVisible(true);
        viewToggle.setVisible(true);
        hexagonGrid.update();
    }

    /**
     * Updates all object variables which need to be updated in order to progress to the Ray Placing phase of the Game.
     * @param atomConfirmButton Button which confirms user Atom placements.
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
     * Method to handle game logic in the Ray Placement phase of the Game.
     * @param guessLabel The label attributed to each Guess
     * @param guessConfirmButton Button to confirm guesses
     * @param guessResultBoard Board of results
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
     * Method to handle the "New Game" phase of the Game; when the second round is played.
     * @param newGameButton Button to enter New Game
     * @param winLabel Label of correct guess
     * @param guessLabel Label of each guess
     * @param guessResultBoard Board of results of the guesses made by a Player.
     * @param scoreboard Board storing the scores of each Player.
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
     *  Method to set {@code HexagonGrid} to debug mode
     */
    public void updateDebugPhase() {
        hexagonGrid.setAtomsVisible(true);
        hexagonGrid.setRayVisible(true);
        hexagonGrid.setBorderClickable(false);
        hexagonGrid.setBorderBoundingBoxVisible(true);
    }

    /**
     * Method for calculating the player scores.
     * @param guesses boolean array of correct and incorrect guesses
     * @param rays List of rays used to make guesses.
     * @return The calculated score.
     */
    private int calculateScore(boolean[] guesses, List<Ray> rays) {
        int score = 0;

        for (boolean guess : guesses)
        {
            if (!guess) { score += 5; }
        }

        score += rays.size();

        return score;
    }

    /**
     * Method to get the {@code HexagonGrid} object.
     * @return The HexagonGrid
     */
    public HexagonGrid getHexagonGrid() {
        return hexagonGrid;
    }

    /**
     * Gets the Guess object associated with this object.
     * @return The associated Guess.
     */
    public Guess getGuessObject() {
        return guesses;
    }

}
