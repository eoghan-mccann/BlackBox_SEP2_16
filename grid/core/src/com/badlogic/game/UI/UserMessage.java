package com.badlogic.game.UI;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class UserMessage {
    private final Stage stage;
    private final Skin skin;
    private boolean waitingForInput = true;

    public UserMessage(Stage stage, Skin skin) {
        this.stage = stage;
        this.skin = skin;
    }

    public void showWelcomeMessage(String title, String message) {
        Dialog dialog = new Dialog(title, skin);
        dialog.text(message);

        //libGDX dialog method inherited from class com.badlogic.gdx.scenes.scene2d.Actor
        //input listener for low level actions, enter click in this case
        dialog.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int key) {
                if (key == Keys.ENTER) {
                    dialog.hide();
                    waitingForInput = false;
                    return true;
                }
                return false;
            }
        });
        dialog.show(stage);
    }


    public boolean isWaitingForInput() {
        return waitingForInput;
    }
}
