package com.dvdfu.platformer.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.dvdfu.platformer.handlers.GameObject;
import com.dvdfu.platformer.handlers.Input;
import com.dvdfu.platformer.states.Play;

public class Player extends GameObject {
	private boolean ground;
	private float gravity;
	private float vx;
	private float vy;
	private float ax;
	private Rectangle xprojection;
	private Rectangle yprojection;
	private Block xcollision;
	private Block ycollision;
	private boolean debug;

	public Player() {
		super(320, 320, 32, 32, true);
		load();
		ground = false;
		gravity = 800f;
		vx = 0;
		vy = 0;
		ax = 10f;
		xprojection = new Rectangle(x, y, width, height);
		yprojection = new Rectangle(x, y, width, height);
		xcollision = null;
		ycollision = null;
		debug = true;
	}

	private void load() {
		TextureRegion sprite[] = new TextureRegion[3];
		for (int i = 0; i < 3; i++) {
			sprite[i] = new TextureRegion(new Texture(Gdx.files.internal("img/block" + i + ".png")));
		}
		// TextureRegion sprite = new TextureRegion(new
		// Texture(Gdx.files.internal("img/blob.png")));
		setAnimation(sprite, 1 / 3f);
		setOffset(0, 0);
	}

	public void update(float dt) {
		collisions(dt);
		if (vx == 0) {
			x = Math.round(x / 4) * 4;
		}
		animation.update(dt);
		x += vx * dt;
		y += vy * dt;
		body.x = x;
		body.y = y;
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
		if (!Input.KeyDown(Input.ARROW_LEFT) && !Input.KeyDown(Input.ARROW_RIGHT)) {
			slowx();
		}
		if (Input.KeyPressed(Input.SPACEBAR)) {
			debug = !debug;
		}
	}

	private void collisions(float dt) {
		xprojection.setPosition(x + vx * dt * 2, y);
		yprojection.setPosition(x, y + vy * dt * 2);
		if (vx != 0 || vy != 0) {
			xcollision = Play.blockIn(xprojection);
			ycollision = Play.blockIn(yprojection);
		}
		else {
			xcollision = null;
			ycollision = null;
		}
		if (ground) {
			Block beneath = Play.blockIn(new Rectangle(x, y - 1, width, height));
			if (beneath == null || (beneath instanceof Platform && vy > 0)) {
				ground = false;
			}
		}
		if (!ground) {
			vy -= gravity * dt;
		}
		if (ycollision != null) {
			if (!(ycollision instanceof Platform) || y > ycollision.getBody().y + ycollision.getBody().height) {
				if (vy > 0) {
					vy = 0;
					y = ycollision.getBody().y - height;
				}
				if (vy < 0) {
					vy = 0;
					y = ycollision.getBody().y + ycollision.getBody().height;
					ground = true;
				}
			}
		}
		if (xcollision != null && !(xcollision instanceof Platform)) {
			if (vx > 0) {
				if (xcollision instanceof Slab && ground) {
					((Slab) xcollision).push(1);
				}
				vx = 0;
				x = xcollision.getBody().x - width;
			}
			if (vx < 0) {
				if (xcollision instanceof Slab && ground) {
					((Slab) xcollision).push(-1);
				}
				vx = 0;
				x = xcollision.getBody().x + xcollision.getBody().width;
			}
		}
	}

	private void moveUp() {
		if (ground) {
			vy = 360;
		}
	}

	private void moveDown() {
		if (ground) {
			vy = -320;
		}
	}

	private void moveLeft() {
		if (vx > -160) {
			vx -= ax;
		} else {
			vx = -160;
		}
	}

	private void moveRight() {
		if (vx < 160) {
			vx += ax;
		} else {
			vx = 160;
		}
	}

	private void slowx() {
		if (vx > 0) {
			if (vx > ax) {
				if (ground) {
					vx -= ax;
				} else {
					vx -= ax / 4;
				}
			} else {
				vx = 0;
			}
		} else if (vx < 0) {
			if (vx < -ax) {
				if (ground) {
					vx += ax;
				} else {
					vx += ax / 4;
				}
			} else {
				vx = 0;
			}
		}
	}

	public void render(ShapeRenderer sr) {
		if (debug) {
			sr.begin(ShapeType.Line);
			sr.setColor(Color.CYAN);
			sr.rect(yprojection.x, yprojection.y, yprojection.width, yprojection.height);
			if (ycollision != null) {
				Rectangle ybody = ycollision.getBody();
				sr.rect(ybody.x, ybody.y, ybody.width, ybody.height);
			}
			sr.setColor(Color.YELLOW);
			sr.rect(xprojection.x, xprojection.y, xprojection.width, xprojection.height);
			if (xcollision != null) {
				Rectangle xbody = xcollision.getBody();
				sr.rect(xbody.x, xbody.y, xbody.width, xbody.height);
			}
			sr.end();
		}
		super.render(sr);
	}
}
