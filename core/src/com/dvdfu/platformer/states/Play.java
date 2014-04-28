package com.dvdfu.platformer.states;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.dvdfu.platformer.entities.Player;
import com.dvdfu.platformer.handlers.Input;
import com.dvdfu.platformer.handlers.InputProcessor;

public class Play implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch sb;
	private ShapeRenderer sr;
	private Player p;
	private BitmapFont f;

	public static AssetManager am;

	public void create() {

		Gdx.input.setInputProcessor(new InputProcessor());

		// am.load(fileName, type);

		camera = new OrthographicCamera(640, 480);
		camera.position.set(camera.viewportWidth / 2f,
				camera.viewportHeight / 2f, 0f);
		sb = new SpriteBatch();
		sr = new ShapeRenderer();

		p = new Player();
		f = new BitmapFont();
	}

	public void dispose() {
		sb.dispose();
		sr.dispose();
	}

	public void render() {
		Gdx.gl.glClearColor(0, 0.2f, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		p.update(Gdx.graphics.getDeltaTime());
		camera.update();
		Input.update();
		sb.setProjectionMatrix(camera.combined);
		sb.begin();
		//f.draw(sb, "" + 1/Gdx.graphics.getDeltaTime(), 320, 240 + 1/Gdx.graphics.getDeltaTime());
		sb.end();
		p.render(sb);
		p.render(sr);
	}

	public void resize(int width, int height) {
	}

	public void pause() {
	}

	public void resume() {
	}
}
