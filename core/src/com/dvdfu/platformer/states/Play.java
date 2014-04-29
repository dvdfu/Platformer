package com.dvdfu.platformer.states;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.dvdfu.platformer.entities.Block;
import com.dvdfu.platformer.entities.Player;
import com.dvdfu.platformer.handlers.CameraController;
import com.dvdfu.platformer.handlers.Input;
import com.dvdfu.platformer.handlers.InputProcessor;

public class Play extends Game {
	private SpriteBatch sb;
	private ShapeRenderer sr;
	private Player p;
	private BitmapFont f;
	private static Array<Block> blockArray;
	private OrthogonalTiledMapRenderer level;
	private TiledMap map;
	private TextureRegion bg;
	private CameraController view;
	private OrthographicCamera cam;

	public static AssetManager am;

	public void create() {

		Gdx.input.setInputProcessor(new InputProcessor());

		// am.load(fileName, type);

		view = new CameraController(640, 480);
		cam = new OrthographicCamera();
		sb = new SpriteBatch();
		sr = new ShapeRenderer();

		p = new Player();
		f = new BitmapFont();

		blockArray = new Array<Block>();
		map = new TmxMapLoader().load("data/untitled.tmx");
		level = new OrthogonalTiledMapRenderer(map, 1f);

		bg = new TextureRegion(new Texture(Gdx.files.internal("img/bg.png")));
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("Tile Layer 2");
		int layerWidth = layer.getWidth();
		int layerHeight = layer.getHeight();

		boolean[][] gridCell = new boolean[layerWidth][layerHeight];
		boolean[][] gridVisited = new boolean[layerWidth][layerHeight];

		for (int y = 0; y < layerHeight; y++) {
			for (int x = 0; x < layerWidth; x++) {
				gridVisited[x][y] = false;
				if (layer.getCell(x, y) == null) {
					gridCell[x][y] = false;
				} else {
					gridCell[x][y] = true;
				}
			}
		}

		for (int y = 1; y < layerHeight - 1; y++) {
			for (int x = 1; x < layerWidth - 1; x++) {
				if (gridCell[x - 1][y] && gridCell[x + 1][y] && gridCell[x][y - 1] && gridCell[x][y + 1]) {
					gridVisited[x][y] = true;
				}
			}
		}

		for (int y = 1; y < layerHeight - 1; y++) {
			for (int x = 1; x < layerWidth - 1; x++) {
				if (gridVisited[x][y] && gridCell[x][y]) {
					gridCell[x][y] = false;
				}
			}
		}

		for (int y = 1; y < layerHeight - 1; y++) {
			for (int x = 1; x < layerWidth - 1; x++) {
				gridVisited[x][y] = false;
			}
		}

		for (int y = 0; y < layerHeight; y++) {
			for (int x = 0; x < layerWidth; x++) {
				if (!gridCell[x][y] || gridVisited[x][y]) {
					continue;
				}
				int xChain = 1;
				int yChain = 1;
				boolean chainRows = true;
				gridVisited[x][y] = true;

				while (x + xChain < layerWidth && gridCell[x + xChain][y] && !gridVisited[x + xChain][y]) {
					gridVisited[x + xChain][y] = true;
					xChain++;
				}

				while (chainRows) {
					for (int i = 0; i < xChain; i++) {
						if (!gridCell[x + i][y + yChain] || gridVisited[x + i][y + yChain]) {
							chainRows = false;
						}
					}
					if (chainRows) {
						for (int i = 0; i < xChain; i++) {
							gridVisited[x + i][y + yChain] = true;
						}
						yChain++;
					}
				}

				blockArray.add(new Block(x * 16, y * 16, 16 * xChain, 16 * yChain));
			}
		}
	}

	public void dispose() {
		sb.dispose();
		sr.dispose();
	}

	public void update(float dt) {
		keys();
		Input.update();
		p.update(dt);
		view.follow(p.getx(), p.gety());
		view.update();
		cam = view.getCam();
		level.setView(cam);

		sb.setProjectionMatrix(cam.combined);
		sr.setProjectionMatrix(cam.combined);
	}

	public void render() {
		update(Gdx.graphics.getDeltaTime());
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		sb.begin();
		sb.draw(bg, cam.position.x - 320, 0);
		sb.end();
		

		level.render();

		for (Block b : blockArray) {
			b.render(sr);
		}
		p.render(sr);
		p.render(sb);
		p.rp(sr);
	}

	public static Rectangle blockAt(float x, float y) {
		for (Block b : blockArray) {
			if (b.getBody().contains(x, y)) {
				return b.getBody();
			}
		}
		return null;
	}

	public static Rectangle blockIn(Rectangle r) {
		for (Block b : blockArray) {
			if (b.getBody().overlaps(r)) {
				return b.getBody();
			}
		}
		return null;
	}

	public static boolean spaceFree(float x, float y) {
		for (Block b : blockArray) {
			if (b.getBody().contains(x, y)) {
				return false;
			}
		}
		return true;
	}

	private void keys() {
		if (Input.KeyPressed(Input.ARROW_UP)) {
			p.moveUp();
		}
		if (Input.KeyDown(Input.ARROW_DOWN)) {
			p.moveDown();
		}
		if (Input.KeyDown(Input.ARROW_LEFT)) {
			p.moveLeft();
		}
		if (Input.KeyDown(Input.ARROW_RIGHT)) {
			p.moveRight();
		}
	}

	public void resize(int width, int height) {
	}

	public void pause() {
	}

	public void resume() {
	}
}
