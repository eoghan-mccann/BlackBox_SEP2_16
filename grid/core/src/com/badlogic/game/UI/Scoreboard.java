package com.badlogic.game.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Scoreboard {
    float xPos;
    float yPos;
    float width;
    float height;

    int playerOneScore;
    int playerTwoScore;

    Label winMessage;
    Label playerNames;
    Label scoreLabel;

    public Scoreboard(float x, float y, int playerOneScore, int playerTwoScore) {
        width = 199;
        height = 175;

        this.playerOneScore = playerOneScore;
        this.playerTwoScore = playerTwoScore;

        winMessage = new Label(null, 0, 0);
            winMessage.setText(getWinMessage());
            winMessage.setFontSize(30);
        playerNames = new Label(null, 0,0);
            playerNames.setText("Player One\n\nPlayer Two");
            playerNames.setFontSize(23);
        scoreLabel = new Label(null, 0,0);
            scoreLabel.setText(playerOneScore+ "\n\n" + playerTwoScore);
            scoreLabel.setFontSize(23);

        xPos = x;
        yPos = y;
    }

    private String getWinMessage() {
        if (playerOneScore == playerTwoScore) {
            return "Tie Game!";
        }
        return playerOneScore < playerTwoScore ? "Player 1 Wins!" : "Player 2 Wins!";
    }


    public void Draw(ShapeRenderer shape, SpriteBatch batch) {
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.box(xPos,yPos,0,width,height,0);
        shape.end();

        float middleX = xPos + width / 2;

        winMessage.batch = batch;
        winMessage.setPos(middleX - winMessage.getTextWidth() / 2, yPos + height + 45);
        winMessage.Draw(shape);

        playerNames.batch = batch;
        playerNames.setPos(middleX - playerNames.getTextWidth() / 2, yPos + height - 30);

        playerNames.Draw(shape);

        scoreLabel.batch = batch;
        scoreLabel.setPos(middleX - scoreLabel.getTextWidth() / 2, yPos + height - 55);
        scoreLabel.Draw(shape);
    }
}
