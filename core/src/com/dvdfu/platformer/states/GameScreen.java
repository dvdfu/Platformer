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
import com.dvdfu.platformer.entities.Moving;
import com.dvdfu.platformer.entities.Platform;
import com.dvdfu.platformer.entities.Player;
import com.dvdfu.platformer.entities.Ramp;
import com.dvdfu.platformer.entities.Slab;
import com.dvdfu.platformer.handlers.CameraController;
import com.dvdfu.platformer.handlers.Vars;
import com.dvdfu.platformer.handlers.HUD;
import com.dvdfu.platformer.handlers.HUDCountable;
import com.dvdfu.platformer.handlers.HUDText;
import com.dvdfu.platformer.handlers.Input;
import com.dvdfu.platformer.handlers.InputProcessor;

public class GameScreen extends Game {
	private static Array<Block> blockArray;
	public static AssetManager am;
	private SpriteBatch sb;
	private ShapeRenderer sr;
	private Player p;
	private OrthogonalTiledMapRenderer level;
	private TiledMap map;
	// private TextureRegion bg;
	private CameraController view;
	private OrthographicCamera cam;
	private Slab s;
	private Slab s2;
	private HUD h;
	private HUDCountable hc;
	private HUDText t;
	private boolean debug;
	private Moving m;

	public void create() {
		Gdx.input.setInputProcessor(new InputProcessor());
		am = new AssetManager();
		am.load("img/bg.png", Texture.class);
		view = new CameraController(Vars.SCREEN_WIDTH, Vars.SCREEN_HEIGHT);
		view.setPan(20);
		view.setZoom(1f);
		cam = new OrthographicCamera();
		sb = new SpriteBatch();
		sr = new ShapeRenderer();
		p = new Player();
		map = new TmxMapLoader().load("data/untitled.tmx");
		blockArray = new Array<Block>();
		createBlocks("blocks");
		createPlatforms("platforms");
		createRamps("ramps");
		// bg = new TextureRegion(new
		// Texture(Gdx.files.internal("img/bg.png")));
		level = new OrthogonalTiledMapRenderer(map, 1f);
		s = new Slab(160, 400, 32, 32);
		blockArray.add(s);
		s2 = new Slab(300, 200, 32, 32);
		blockArray.add(s2);
		m = new Moving(400, 240, 64, 16);
		blockArray.add(m);
		h = new HUD();
		TextureRegion sprite[] = new TextureRegion[2];
		for (int i = 0; i < 2; i++) {
			;
			sprite[i] = new TextureRegion(new Texture(Gdx.files.internal("img/heart1.png")), i * 32, 0, 32, 32);
		}
		m.getSprite().setSprite(new TextureRegion(new Texture(Gdx.files.internal("img/platform.png"))));
		hc = new HUDCountable(sprite, 64, 64, -2, 0);
		hc.setNum(4);
		hc.setDelay(1 / 2f);
		h.addElement(hc);
		t = new HUDText("Lives: ", 16, 88);
		h.addElement(t);
		debug = true;
	}

	private void createBlocks(String layerName) {
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(layerName);
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
		/*
		 * for (int y = 1; y < layerHeight - 1; y++) { for (int x = 1; x <
		 * layerWidth - 1; x++) { if (gridCell[x - 1][y] && gridCell[x + 1][y]
		 * && gridCell[x][y - 1] && gridCell[x][y + 1]) { gridMark[x][y] = true;
		 * } } } // remove inner cells, reset marked cells for (int y = 1; y <
		 * layerHeight - 1; y++) { for (int x = 1; x < layerWidth - 1; x++) { if
		 * (gridMark[x][y] && gridCell[x][y]) { gridCell[x][y] = false; }
		 * gridMark[x][y] = false; } }
		 */
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

	private void createPlatforms(String layerName) {
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(layerName);
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
				gridMark[x][y] = true;
				while (x + xChain < layerWidth && gridCell[x + xChain][y] && !gridMark[x + xChain][y]) {
					gridMark[x + xChain][y] = true;
					xChain++;
				}
				blockArray.add(new Platform(x * 16, y * 16, 16 * xChain, 16));
			}
		}
	}
	
	private void createRamps(String layerName) {
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(layerName);
		int layerWidth = layer.getWidth();
		int layerHeight = layer.getHeight();
		// init grid arrays
		for (int y = 0; y < layerHeight; y++) {
			for (int x = 0; x < layerWidth; x++) {
				if (layer.getCell(x, y) != null) {
					if (layer.getCell(x, y).getTile().getProperties().get("slope").equals("1")) {
						blockArray.add(new Ramp(x * 16, y * 16, 16, 16, 1));
					} else {
						blockArray.add(new Ramp(x * 16, y * 16, 16, 16, -1));
					}
				}
			}
		}
	}

	public void dispose() {
		sb.dispose();
		sr.dispose();
	}

	public void render() {
		Gdx.graphics.setTitle("" + Gdx.graphics.getFramesPerSecond());
		m.update();
		s.update();
		s2.update();
		h.update();
		p.update();
		view.follow(p.getX() + p.getWidth() / 2, p.getY() + p.getHeight() / 2);
		view.update();
		cam = view.getCam();
		level.setView(cam);
		sb.setProjectionMatrix(cam.combined);
		sr.setProjectionMatrix(cam.combined);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// sb.begin();
		// sb.draw(bg, cam.position.x - 320, 0);
		// sb.end();
		h.setView(cam);
		if (debug) {
			for (Block b : blockArray) {
				b.render(sr);
			}
			p.render(sr);
		} else {
			level.render();
			for (Block b : blockArray) {
				b.render(sb);
			}
			h.render(sb);
			p.render(sb);
		}
		if (Input.KeyPressed(Input.ARROW_DOWN)) {
			debug = !debug;
		}
		Input.update();
	}

	public static Block blockIn(Rectangle r) {
		Block best = null;
		for (Block b : blockArray) {
			if (b.getBody().overlaps(r)) {
				if (b instanceof Platform) {
					if (best == null || b.getY() < best.getY()) {
						best = b;
					}
				} else {
					return b;
				}
			}
		}
		return best;
	}

	public void resize(int width, int height) {}

	public void pause() {}

	public void resume() {}
}
