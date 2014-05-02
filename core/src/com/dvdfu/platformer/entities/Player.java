package com.dvdfu.platformer.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.dvdfu.platformer.handlers.GameConstants;
import com.dvdfu.platformer.handlers.GameObject;
import com.dvdfu.platformer.handlers.Input;
import com.dvdfu.platformer.states.GameScreen;

public class Player extends GameObject {
	final private float MOVE_SPEED = 160f;
	final private float JUMP_SPEED = 400f;
	final private float AX = 10f;
	final private float AX_AIR = 2f;
	private boolean ground;
	private float vx;
	private float vy;
	private Rectangle xprojection;
	private Rectangle yprojection;
	private Block xcollision;
	private Block ycollision;
	private boolean debug;

	public Player() {
		super(320, 320, 32, 32, true);
		load();
		ground = false;
		vx = 0;
		vy = 0;
		xprojection = new Rectangle(body.x, body.y, width, height);
		yprojection = new Rectangle(body.x, body.y, width, height);
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

	private void moveUp() {
		if (ground) {
			vy = JUMP_SPEED;
		}
	}

	private void moveDown() {}

	private void moveLeft() {
		if (vx > -MOVE_SPEED) {
			vx -= AX;
		} else {
			vx = -MOVE_SPEED;
		}
	}

	private void moveRight() {
		if (vx < MOVE_SPEED) {
			vx += AX;
		} else {
			vx = MOVE_SPEED;
		}
	}

	private void slowx() {
		if (vx > 0) {
			if (vx > AX) {
				if (ground) {
					vx -= AX;
				} else {
					vx -= AX_AIR;
				}
			} else {
				vx = 0;
			}
		} else if (vx < 0) {
			if (vx < -AX) {
				if (ground) {
					vx += AX;
				} else {
					vx += AX_AIR;
				}
			} else {
				vx = 0;
			}
		}
	}

	public void update() {
		collisions();
		if (vx == 0) {
			body.x = Math.round(body.x / 4) * 4;
		}
		animation.update();
		body.x += vx * GameConstants.SPF;
		body.y += vy * GameConstants.SPF;
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

	private void collisions() {
		xprojection.setPosition(body.x + vx * GameConstants.SPF * 2, body.y);
		yprojection.setPosition(body.x, body.y + vy * GameConstants.SPF * 2);
		if (vx != 0 || vy != 0) {
			xcollision = GameScreen.blockIn(xprojection);
			ycollision = GameScreen.blockIn(yprojection);
		} else {
			xcollision = null;
			ycollision = null;
		}
		if (ground) {
			Block beneath = GameScreen.blockIn(new Rectangle(body.x, body.y - 1, width, height));
			if (beneath == null || (beneath instanceof Platform && vy > 0)) {
				ground = false;
			}
		}
		if (!ground) {
			vy -= GameConstants.GRAVITY * GameConstants.SPF;
		}
		if (ycollision != null) {
			if (!(ycollision instanceof Platform) || body.y > ycollision.getBody().y + ycollision.getBody().height) {
				if (vy > 0) {
					vy = 0;
					body.y = ycollision.getBody().y - height;
				}
				if (vy < 0) {
					vy = 0;
					body.y = ycollision.getBody().y + ycollision.getBody().height;
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
				body.x = xcollision.getBody().x - width;
			}
			if (vx < 0) {
				if (xcollision instanceof Slab && ground) {
					((Slab) xcollision).push(-1);
				}
				vx = 0;
				body.x = xcollision.getBody().x + xcollision.getBody().width;
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
