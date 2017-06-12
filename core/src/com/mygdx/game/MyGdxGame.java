package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends Game {

	// A texture represents a loaded image that is stored in video ram,
	// it is loaded by passing a FileHandle to an asset file to its constructor.
	// Such FileHandle instances are obtained through one of the methods provided by Gdx.files
	// Internal files are located in the assets directory of the Android project
	public SpriteBatch batch;
	public BitmapFont font;

	// assets are usually loaded in the ApplicationAdapter.create() method
	@Override
	public void create () {

		batch = new SpriteBatch();

		// By default, font is Arial
		font = new BitmapFont();
		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render(){

		super.render();

	}

	@Override
	public void dispose() {
		batch.dispose();
		font.dispose();
	}
}