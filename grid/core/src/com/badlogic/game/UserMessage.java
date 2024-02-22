package com.badlogic.game;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.Input.Keys;

public class UserMessage {
    private Stage stage;
    private Skin skin;

    public UserMessage(Stage stage, Skin skin) {
        this.stage = stage;
        this.skin = skin;
    }

}
