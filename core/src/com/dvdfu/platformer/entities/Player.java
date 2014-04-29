package com.dvdfu.platformer.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.dvdfu.platformer.handlers.DynamicObject;
import com.dvdfu.platformer.handlers.Input;
import com.dvdfu.platformer.states.Play;

public class Player extends DynamicObject {
	private boolean ground;
	private float gravity;
	private float deltatime;
	private Rectangle xprojection;
	private Rectangle yprojection;
	private Rectangle xcollision;
	private Rectangle ycollision;
	private boolean debug;

	public Player() {
		super(320, 320, 16, 16);
		load();
		ground = false;
		gravity = 800f;
		xprojection = new Rectangle(x, y, width, height);
		yprojection = new Rectangle(x, y, width, height);
		xcollision = null;
		ycollision = null;
		debug = true;
	}

	private void load() {
		// TextureRegion sprite[] = new TextureRegion[3];
		// for (int i = 0; i < 3; i++) {
		// sprite[i] = new TextureRegion(new
		// Texture(Gdx.files.internal("img/block" + i + ".png")));
		// }
		TextureRegion sprite = new TextureRegion(new Texture(Gdx.files.internal("img/blob.png")));
		setAnimation(sprite, 1 / 3f);
		setOffset(0, 0);
	}

	public void update(float dt) {
		
		deltatime = dt;
		xprojection.setPosition(x + dx * dt, y);
		xcollision = Play.blockIn(xprojection);
		yprojection.setPosition(x, y + dy * dt);
		ycollision = Play.blockIn(yprojection);
		if (ground) {
			dy = 0;
			if (Play.blockIn(new Rectangle(x, y - 1, width, height)) == null) {
				ground = false;
			}
		}
		if (!ground) {
			dy -= gravity * dt;
		}
		if (ycollision != null) {
			if (dy < 0) {
				dy = 0;
				y = ycollision.y + ycollision.height;
				ground = true;
			}
			if (dy > 0) {
				dy = 0;
				y = ycollision.y - height;
				ground = false;
			}
		}
		if (xcollision != null) {
			if (dx > 0) {
				dx = 0;
				x = xcollision.x - width;
			}
			if (dx < 0) {
				dx = 0;
				x = xcollision.x + xcollision.width;
			}
		}
		super.update(deltatime);
		dx = 0;
	}
	
	public void keyListener() {
		if (Input.KeyPressed(Input.ARROW_UP)) {
			moveUp();
		}
		if (Input.KeyPressed(Input.ARROW_DOWN)) {
			moveDown();
		}
		if (Input.KeyDown(Input.ARROW_LEFT)) {
			moveLeft();
		}
		if (Input.KeyDown(Input.ARROW_RIGHT)) {
			moveRight();
		}
	}

	public void moveUp() {
		if (ground) {
			dy = 360;
			ground = false;
		}
	}

	public void moveDown() {
		debug = !debug;
	}

	public void moveLeft() {
		dx = -160;
	}

	public void moveRight() {
		dx = 160;
	}

	public void rp(ShapeRenderer sr) {
		if (debug) {
			sr.begin(ShapeType.Filled);
			sr.setColor(Color.BLUE);
			sr.rect(yprojection.x, yprojection.y, yprojection.width, yprojection.height);
			if (ycollision != null) {
				if (xcollision == ycollision) {
					sr.setColor(Color.GREEN);
				}
				sr.rect(ycollision.x, ycollision.y, ycollision.width, ycollision.height);
			}
			sr.setColor(Color.YELLOW);
			sr.rect(xprojection.x, xprojection.y, xprojection.width, xprojection.height);
			if (xcollision != null) {
				if (xcollision == ycollision) {
					sr.setColor(Color.GREEN);
				}
				sr.rect(xcollision.x, xcollision.y, xcollision.width, xcollision.height);
			}
			sr.end();
		}
	}
}
