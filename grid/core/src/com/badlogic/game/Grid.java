package com.badlogic.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.GL20;

public class Grid extends ApplicationAdapter {

	ShapeRenderer shape;
	HexGrid grid;
	float startX, startY;
	// initially set to the upper left most hexagon's upper left side point


	@Override
	public void create () { // on start
		shape = new ShapeRenderer();
		grid = new HexGrid(startX, startY);
		startX = 250f;
		startY = 750f;



	}

	@Override
	public void render () {

		// ------ Update ------
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


		// ------ Render ------
		shape.begin(ShapeRenderer.ShapeType.Line);
		// draw grid
		grid.drawGrid(shape);

		shape.circle(grid.hexagons[5][3].centreX, grid.hexagons[5][3].centreY, 35);
		shape.end();

	}

	@Override
	public void dispose () {
		shape.dispose();
	}


	//
}