package com.dvdfu.platformer.states;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.dvdfu.platformer.entities.Block;
import com.dvdfu.platformer.entities.Platform;
import com.dvdfu.platformer.entities.Player;
import com.dvdfu.platformer.entities.Slab;
import com.dvdfu.platformer.handlers.CameraController;
import com.dvdfu.platformer.handlers.Input;
import com.dvdfu.platformer.handlers.InputProcessor;

public class Play extends Game {
	private SpriteBatch sb;
	private ShapeRenderer sr;
	private Player p;
	private static Array<Block> blockArray;
	private OrthogonalTiledMapRenderer level;
	private TiledMap map;
	private TextureRegion bg;
	private CameraController view;
	private OrthographicCamera cam;
	public static AssetManager am;
	public static int screenWidth;
	public static int screenHeight;
	private Slab s;
	private Slab s2;

	public void create() {
		Gdx.input.setInputProcessor(new InputProcessor());
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
		// am.load(fileName, type);
		view = new CameraController(screenWidth, screenHeight);
		view.setPan(20);
		view.setZoom(1f);
		cam = new OrthographicCamera();
		sb = new SpriteBatch();
		sr = new ShapeRenderer();
		p = new Player();
		map = new TmxMapLoader().load("data/untitled.tmx");
		blockArray = new Array<Block>();
		createBlocks();
		createPlatforms();
		bg = new TextureRegion(new Texture(Gdx.files.internal("img/bg.png")));
		level = new OrthogonalTiledMapRenderer(map, 1f);
		s = new Slab(200, 208, 64, 64);
		blockArray.add(s);
		s2 = new Slab(360, 208, 64, 64);
		blockArray.add(s2);
	}
	
	private void createBlocks() {
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("Tile Layer 2");
		int layerWidth = layer.getWidth();
		int layerHeight = layer.getHeight();
		boolean[][] gridCell = new boolean[layerWidth][layerHeight];
		boolean[][] gridMark = new boolean[layerWidth][layerHeight];
		// init grid arrays
		for (int y = 0; y < layerHeight; y++) {
			for (int x = 0; x < layerWidth; x++) {
				gridMark[x][y] = false;
				if (layer.getCell(x, y) == null) {
					gridCell[x][y] = false;
				} else {
					gridCell[x][y] = true;
				}
			}
		}
		// mark inner cells
		for (int y = 1; y < layerHeight - 1; y++) {
			for (int x = 1; x < layerWidth - 1; x++) {
				if (gridCell[x - 1][y] && gridCell[x + 1][y] && gridCell[x][y - 1] && gridCell[x][y + 1]) {
					gridMark[x][y] = true;
				}
			}
		}
		// remove inner cells, reset marked cells 
		for (int y = 1; y < layerHeight - 1; y++) {
			for (int x = 1; x < layerWidth - 1; x++) {
				if (gridMark[x][y] && gridCell[x][y]) {
					gridCell[x][y] = false;
				}
				gridMark[x][y] = false;
			}
		}
		// chain consecutive cells
		for (int y = 0; y < layerHeight; y++) {
			for (int x = 0; x < layerWidth; x++) {
				if (!gridCell[x][y] || gridMark[x][y]) {
					continue;
				}
				int xChain = 1;
				int yChain = 1;
				boolean chainRows = true;
				gridMark[x][y] = true;
				while (x + xChain < layerWidth && gridCell[x + xChain][y] && !gridMark[x + xChain][y]) {
					gridMark[x + xChain][y] = true;
					xChain++;
				}
				while (chainRows) {
					for (int i = 0; i < xChain; i++) {
						if (y + yChain >= layerHeight || !gridCell[x + i][y + yChain] || gridMark[x + i][y + yChain]) {
							chainRows = false;
						}
					}
					if (chainRows) {
						for (int i = 0; i < xChain; i++) {
							if (y + yChain < layerHeight) {
								gridMark[x + i][y + yChain] = true;
							} else {
								break;
							}
						}
						yChain++;
					}
				}
				blockArray.add(new Block(x * 16, y * 16, 16 * xChain, 16 * yChain));
			}
		}
	}

	private void createPlatforms() {
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("Tile Layer 3");
		int layerWidth = layer.getWidth();
		int layerHeight = layer.getHeight();
		boolean[][] gridCell = new boolean[layerWidth][layerHeight];
		boolean[][] gridMark = new boolean[layerWidth][layerHeight];
		// init grid arrays
		for (int y = 0; y < layerHeight; y++) {
			for (int x = 0; x < layerWidth; x++) {
				gridMark[x][y] = false;
				if (layer.getCell(x, y) == null) {
					gridCell[x][y] = false;
				} else {
					gridCell[x][y] = true;
				}
			}
		}
		// chain consecutive cells
		for (int y = 0; y < layerHeight; y++) {
			for (int x = 0; x < layerWidth; x++) {
				if (!gridCell[x][y] || gridMark[x][y]) {
					continue;
				}
				int xChain = 1;
				int yChain = 1;
				boolean chainRows = true;
				gridMark[x][y] = true;
				while (x + xChain < layerWidth && gridCell[x + xChain][y] && !gridMark[x + xChain][y]) {
					gridMark[x + xChain][y] = true;
					xChain++;
				}
				while (chainRows) {
					for (int i = 0; i < xChain; i++) {
						if (y + yChain >= layerHeight || !gridCell[x + i][y + yChain] || gridMark[x + i][y + yChain]) {
							chainRows = false;
						}
					}
					if (chainRows) {
						for (int i = 0; i < xChain; i++) {
							if (y + yChain < layerHeight) {
								gridMark[x + i][y + yChain] = true;
							} else {
								break;
							}
						}
						yChain++;
					}
				}
				blockArray.add(new Platform(x * 16, y * 16, 16 * xChain, 16 * yChain));
			}
		}
	}
	
	public void dispose() {
		sb.dispose();
		sr.dispose();
	}

	private void updateKeys() {
		p.keyListener();
	}

	public void render() {
		Gdx.graphics.setTitle("" + Gdx.graphics.getFramesPerSecond());
		updateKeys();
		Input.update();
		p.update(Gdx.graphics.getDeltaTime());
		view.follow(p.getx(), p.gety());
		view.update();
		cam = view.getCam();
		level.setView(cam);
		sb.setProjectionMatrix(cam.combined);
		sr.setProjectionMatrix(cam.combined);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		sb.begin();
		sb.draw(bg, cam.position.x - 320, 0);
		sb.end();
		//level.render();
		//p.render(sb);
		for (Block b : blockArray) { b.render(sr); }
		p.render(sr);
		s.update(Gdx.graphics.getDeltaTime());
		s2.update(Gdx.graphics.getDeltaTime());
	}

	public static Block blockIn(Rectangle r) {
		for (Block b : blockArray) {
			if (b.getBody().overlaps(r)) {
				return b;
			}
		}
		return null;
	}

	public void resize(int width, int height) {}

	public void pause() {}

	public void resume() {}
}
