package com.badlogic.game.UI;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public interface Renderable {
    void Draw(ShapeRenderer shapeRenderer, SpriteBatch batch);
}
