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
	//SpriteBatch batch;
	//Texture img;
	ShapeRenderer shape;
	HexGrid grid;
	Hexagon[] Hexagons;





	float startX = 250F;
	float startY = 750F;
	// initially set to the upper left most hexagon's upper left side point



	@Override
	public void create () { // on start
		shape = new ShapeRenderer();
		//img = new Texture("badlogic.jpg");
		//batch = new SpriteBatch();

		grid = new HexGrid(startX, startY);


	}

	@Override
	public void render () {

		// ------ Update ------


		//batch.begin();
		//batch.end();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


		// ------ Render ------




		shape.begin(ShapeRenderer.ShapeType.Line);

		// draw grid
		grid.drawGrid(shape);

		shape.end();


	}

	@Override
	public void dispose () {
		shape.dispose();
		//batch.dispose();
		//img.dispose();
	}


	//
}