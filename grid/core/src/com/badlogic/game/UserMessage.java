package com.badlogic.game;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class UserMessage {
    private Stage stage;
    private Skin skin;

    public UserMessage(Stage stage, Skin skin) {
        this.stage = stage;
        this.skin = skin;
    }

    public void showMessage(String title, String message) {
        Dialog dialog = new Dialog(title, skin);
        dialog.text(message);
        dialog.button("OK");
        dialog.show(stage);
    }
}

