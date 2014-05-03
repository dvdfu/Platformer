package com.dvdfu.platformer.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.dvdfu.platformer.handlers.Vars;
import com.dvdfu.platformer.handlers.GameObject;
import com.dvdfu.platformer.handlers.Input;
import com.dvdfu.platformer.states.GameScreen;

public class Player2 extends GameObject {
	private float vx;
	private float vy;
	private int keyUp;
	private int keyLeft;
	private int keyRight;
	private boolean jump;
	private boolean moveLeft;
	private boolean moveRight;
	
	private Block yCollision = null;
	private Block xCollision = null;

	private enum State {
		GROUND, JUMPING, FALLING
	};

	private State state;
	private TextureRegion sprIdleLeft = new TextureRegion(new Texture(Gdx.files.internal("img/block0.png")));
	private TextureRegion sprIdleRight = new TextureRegion(new Texture(Gdx.files.internal("img/block1.png")));
	private TextureRegion sprJumpLeft = new TextureRegion(new Texture(Gdx.files.internal("img/block2.png")));
	private TextureRegion sprJumpRight = new TextureRegion(new Texture(Gdx.files.internal("img/block3.png")));

	public Player2() {
		super(320, 320, 32, 32);
		load();
		vx = 0;
		vy = 0;
		keyUp = Input.ARROW_UP;
		keyLeft = Input.ARROW_LEFT;
		keyRight = Input.ARROW_RIGHT;
		state = State.FALLING;
	}

	private void load() {
		this.sprite.setSprite(sprIdleLeft);
		setOffset(0, 0);
	}

	public void update() {
		x += vx * Vars.SPF;
		y += vy * Vars.SPF;
		body.x = x;
		body.y = y;
		keyListener();
		xCollision = GameScreen.blockIn(new Rectangle(x + vx * Vars.SPF, y, width, height));
		if (xCollision == null) {
			if (moveLeft) {
				vx = -200;
			} else if (moveRight) {
				vx = 200;
			} else {
				vx = 0;
			}
		} else {
			if (vx > 0) {
				x = xCollision.getX() - width;
			}
			else if (vx < 0) {
				x = xCollision.getX() + xCollision.getWidth();
			}
			vx = 0;
		}
		switch (state) {
		case JUMPING:
			getSprite().setSprite(sprJumpLeft);
			yCollision = GameScreen.blockIn(new Rectangle(x, y + height + vy * Vars.SPF, width, vy * Vars.SPF));
			vy -= Vars.GRAVITY * Vars.SPF;
			if (yCollision != null) {
				state = State.FALLING;
				vy = 0;
				y = yCollision.getY() - height;
			} else if (vy < 0) {
				state = State.FALLING;
			}
			break;
		case FALLING:
			getSprite().setSprite(sprIdleRight);
			yCollision = GameScreen.blockIn(new Rectangle(x, y + vy * Vars.SPF, width, -vy * Vars.SPF));
			vy -= Vars.GRAVITY * Vars.SPF;
			if (yCollision != null) {
				state = State.GROUND;
				vy = 0;
				y = yCollision.getY() + yCollision.getHeight();
			}
			break;
		case GROUND:
			getSprite().setSprite(sprIdleLeft);
			yCollision = GameScreen.blockIn(new Rectangle(x, y - 1, width, 1));
			if (yCollision == null) {
				state = State.JUMPING;
			} else {
				if (jump) {
					vy = 400;
				} else {
					vy = 0;
					y = yCollision.getY() + yCollision.getHeight();
				}
			}
			break;
		}
	}

	private void keyListener() {
		jump = Input.KeyPressed(keyUp);
		moveLeft = Input.KeyDown(keyLeft);
		moveRight = Input.KeyDown(keyRight);
	}
	
	public void render(ShapeRenderer sr) {
		sr.begin(ShapeType.Line);
			sr.rect(x, y + vy * Vars.SPF, width, height);
			sr.rect(x + vx * Vars.SPF, y, width, height);
		sr.end();
		super.render(sr);
	}
}