package com.badlogic.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.List;

public class Guess {
    private static final int MAX_GUESSES = 5;

    private int guessCount;
    private final List<Hexagon> guessList;
    private final Label marker;  // Declare as final to ensure it's not reassigned

    public Guess() {
        guessCount = 0;
        guessList = new ArrayList<>();
        marker = new Label(null, 0, 0);  // Initialize with null batch, will be set during rendering
        marker.setText("X");
        marker.setFontSize(20);
    }

    public void handleGuess(Hexagon hexagon) {
        if (guessList.contains(hexagon)) {
            removeGuess(hexagon);
        } else if (guessCount < MAX_GUESSES) {
            addGuess(hexagon);
        }
    }

    private void addGuess(Hexagon hexagon) {
        guessList.add(hexagon);
        guessCount++;
    }

    private void removeGuess(Hexagon hexagon) {
        guessList.remove(hexagon);
        guessCount--;
    }

    public int getRemainingGuesses() {
        return MAX_GUESSES - guessCount;
    }

    public boolean[] getGuessAnswers() {
        boolean[] guesses = new boolean[MAX_GUESSES];

        for (int i = 0; i < guessList.size(); i++) {
            guesses[i] = guessList.get(i).hasAtom();
        }

        return guesses;
    }

    public void draw(ShapeRenderer shape, SpriteBatch batch) {
        marker.batch = batch;

        for (Hexagon hex : guessList) {

            marker.setPos(hex.getCenterX() - marker.getTextWidth() / 2, hex.getCenterY() + marker.getTextHeight() /2);
            marker.Draw(shape);
        }
    }
}

