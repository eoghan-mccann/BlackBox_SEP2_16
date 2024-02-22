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

    public void showMessage(String title, String message) {
        //Dialog dialog = new Dialog(title, skin);
        Dialog dialog = new Dialog(title, skin);
        dialog.text(message);
        //dialog.button("OK");

        //libGDX dialog method inherited from class com.badlogic.gdx.scenes.scene2d.Actor
        //input listener for low level actions, enter click in this case
        dialog.addListener(new InputListener()
        {

            //keyDown is called when a key goes down
            @Override
            public boolean keyDown(InputEvent event, int key)
            {
                if (key == Keys.ENTER)
                {
                    //make the dialog window disappear
                    dialog.hide();
                    //returns true if the key has been pressed
                    return true;
                }
                //false otherwise
                return false;
            }
        });

        // Show the dialog
        dialog.show(stage);
    }

}
