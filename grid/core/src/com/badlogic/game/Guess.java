package com.badlogic.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.List;

public class Guess {
    private static final int MAX_GUESSES = 5;

    private int guessCount;
    private List<Hexagon> guessList;

    Guess() {
        guessCount = 0;
        guessList = new ArrayList<>();
    }

    public void handleGuess(Hexagon hexagon) {
        if (guessCount > MAX_GUESSES) { return; }

        if (guessList.contains(hexagon)) { ;
            removeGuess(hexagon);
        } else {
            addGuess(hexagon);
        }
    }

    private void addGuess(Hexagon hexagon) {
        guessList.add(hexagon);
        guessCount++;

        System.out.println(guessCount);
    }

    private void removeGuess(Hexagon hexagon) {
        if (guessCount <= 0) { return; }
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


    public void Draw(ShapeRenderer shape, SpriteBatch batch) {
        for (Hexagon hex : guessList) {
            Label marker = new Label(batch, hex.getCenterX(), hex.getCenterY());
            marker.setText("X");
            marker.setFontSize(2);
            marker.Draw(shape);
        }
    }

}
