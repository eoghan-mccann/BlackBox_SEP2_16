package com.badlogic.game;

import com.badlogic.game.UI.Renderable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.lang.reflect.Array;
import java.util.List;

/**
 * Class for rendering the game elements. In HLD, this is represented by View.
 */
public class GameRenderer {

    private final OrthographicCamera camera;
    private final SpriteBatch batch;
    private final ShapeRenderer shapeRenderer;
    private final Stage stage;
    private final Skin skin;

    private final Game game;

    public GameRenderer(Game game) {
        this.game = game;

        camera = game.getCamera();
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);

        batch = game.getBatch();
        shapeRenderer = game.getShapeRenderer();
        stage = game.getStage();

        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("rainbow/skin/rainbow-ui.json"));
    }

    /**
     * Renders all objects to the screen on each frame.
     */
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        renderGameElements();
        renderUIElements();

        camera.update();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    /**
     * Helper method of the {@code render} method. Renders all Game elements.
     */
    private void renderGameElements() {
        List<Object> EntityObjects = game.getEntityObjects();

        for (Object entity : EntityObjects) {
            if (entity instanceof Entities) {
                ((Entities) entity).Draw(shapeRenderer);
            } else if (entity instanceof HexagonGrid) {
                ((HexagonGrid) entity).Draw(shapeRenderer);
            }
        }
    }

    /**
     * Helper method of the {@code render} method. Renders all UI elements of the game.
     */
    private void renderUIElements() {
        List<Object> UIElements = game.getUIObjects();

        for (Object ui : UIElements) {
            if (ui instanceof Renderable) {
                ((Renderable) ui).Draw(shapeRenderer, batch);
            } else if (ui instanceof Entities) {
                ((Entities) ui).Draw(shapeRenderer);
            }
        }
    }

    /**
     * Method to get the width of the player's screen.
     * @return Width of player's screen.
     */
    public static int getWindowWidth() {
        return Gdx.graphics.getWidth();
    }

    /**
     * Method to get the height/length of the player's screen
     * @return Height of the player's screen.
     */
    public static int getWindowHeight() {
        return Gdx.graphics.getHeight();
    }

    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        stage.dispose();
        skin.dispose();
    }
}
