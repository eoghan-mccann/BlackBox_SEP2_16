package com.badlogic.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Main extends ApplicationAdapter {

    Game game;

    @Override
    public void create () { // on start
        game = new Game();

    }

    @Override
    public void render () {
        game.render();
        game.update();
    }

    @Override
    public void resize(int width, int height) {
        game.resize(width, height);
    }

    @Override
    public void dispose () {
        game.dispose();
    }

}