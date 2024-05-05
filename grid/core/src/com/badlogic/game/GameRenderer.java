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

public class GameRenderer {

    private final OrthographicCamera camera;
    private final SpriteBatch batch;
    private final ShapeRenderer shapeRenderer;
    private final Stage stage;
    private final Skin skin;

    private final Game game;

    public GameRenderer(Game game) {
        this.game = game;

        camera = game.camera;
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        batch = game.batch;
        shapeRenderer = game.shape;
        stage = game.stage;

        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("rainbow/skin/rainbow-ui.json"));
    }

    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        renderGameElements();
        renderUIElements();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

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

    private void renderUIElements() {
        List<Object> UIElements = game.getUIObjects();

        for (Object ui : UIElements) {
            if (ui instanceof Renderable) {
                ((Renderable) ui).Draw(shapeRenderer, batch);
            } else if (ui instanceof Entities) {
                ((Entities) ui).Draw(shapeRenderer);
            } else {
                System.out.println(ui);
            }
        }
    }

    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        stage.dispose();
        skin.dispose();
    }
}
