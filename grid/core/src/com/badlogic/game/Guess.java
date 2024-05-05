package com.badlogic.game;

import com.badlogic.game.UI.Label;
import com.badlogic.game.UI.Renderable;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * The Controller {@code Guess} class handles the guessing mechanic of the Game. A guess ir represented on screen by an "x".
 */
public class Guess implements Renderable {
    private static final int MAX_GUESSES = 5;

    private int guessCount;
    private final List<Hexagon> guessList;
    private final Label marker;  // Declare as final to ensure it's not reassigned
    private boolean answeredRevealed;


    public Guess() {
        guessCount = 0;
        guessList = new ArrayList<>();
        marker = new Label(null, 0, 0);  // Initialize with null batch, will be set during rendering
        marker.setVisible(true);
        marker.setText("X");
        marker.setFontSize(20);
        answeredRevealed = false;
    }

    /**
     * Method to either add or remove a guess from the list of guesses, depending on if the given Hexagon already has been guessed.
     * @param hexagon The Hexagon which the guess is being added to / removed from.
     */
    public void handleGuess(Hexagon hexagon) {
        if (guessList.contains(hexagon)) {
            removeGuess(hexagon);
        } else if (guessCount < MAX_GUESSES) {
            addGuess(hexagon);
        }
    }

    /**
     * Method to add a new guess to the {@code guessList} List.
     * @param hexagon The Hexagon being guessed.
     */
    private void addGuess(Hexagon hexagon) {
        guessList.add(hexagon);
        guessCount++;
    }

    /**
     * Method to remove a guess from the {@code guessList} List.
     * @param hexagon The Hexagon being removed.
     */
    private void removeGuess(Hexagon hexagon) {
        guessList.remove(hexagon);
        guessCount--;
    }

    /**
     * Gets the remaining number of guesses the user has.
     * @return The integer number of guesses left.
     */
    public int getRemainingGuesses() {
        return MAX_GUESSES - guessCount;
    }

    /**
     * Gets the answers to each guess.
     * @return A boolean array of answers, where {@code True} is correct and {@code False} incorrect.
     */
    public boolean[] getGuessAnswers() {
        boolean[] guesses = new boolean[MAX_GUESSES];

        for (int i = 0; i < guessList.size(); i++) {
            guesses[i] = guessList.get(i).hasAtom();
        }

        answeredRevealed = true;
        return guesses;
    }

    public void Draw(ShapeRenderer shape, SpriteBatch batch) {
        marker.batch = batch;

        for (Hexagon hex : guessList) {
            marker.setPos(hex.getCenterX() - marker.getTextWidth() / 2, hex.getCenterY() + marker.getTextHeight() /2);

            if (answeredRevealed) {
                marker.setColor(hex.hasAtom() ? Color.GREEN : Color.RED);
            }

            marker.Draw(shape);
        }
    }
}

