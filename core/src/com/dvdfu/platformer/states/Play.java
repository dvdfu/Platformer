package com.dvdfu.platformer.states;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.dvdfu.platformer.entities.Block;
import com.dvdfu.platformer.entities.Player;
import com.dvdfu.platformer.handlers.Input;
import com.dvdfu.platformer.handlers.InputProcessor;

public class Play implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch sb;
	private ShapeRenderer sr;
	private Player p;
	private BitmapFont f;
	private Array<Block> blockArray;
	private OrthogonalTiledMapRenderer renderer;
	private TiledMap map;

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
		
		blockArray = new Array<Block>();map = new TmxMapLoader().load("data/untitled.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 1f);
		camera.setToOrtho(false, 640, 480);
		camera.update();
		
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("Tile Layer 2");
		
		for(int row = 0; row < layer.getHeight(); row++) {
			for(int col = 0; col < layer.getWidth(); col++) {
				Cell cell = layer.getCell(col, row);
				if(cell == null) continue;
				if (cell.getTile() == null) continue;
				blockArray.add(new Block(col*16, row*16));
				
			}
		}
	}

	public void dispose() {
		sb.dispose();
		sr.dispose();
	}

	public void render() {
		Gdx.gl.glClearColor(0, 0.2f, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		p.update(Gdx.graphics.getDeltaTime());
		renderer.setView(camera);
		renderer.render();
		camera.position.x = p.getx();
		camera.update();
		Input.update();
		sb.setProjectionMatrix(camera.combined);
		sb.begin();
		//f.draw(sb, "" + 1/Gdx.graphics.getDeltaTime(), 320, 240 + 1/Gdx.graphics.getDeltaTime());
		sb.end();
		sr.setProjectionMatrix(camera.combined);
		p.render(sb);
		p.render(sr);
		for (Block b : blockArray) {
			b.render(sr);
		}
	}

	public void resize(int width, int height) {
	}

	public void pause() {
	}

	public void resume() {
	}
}
