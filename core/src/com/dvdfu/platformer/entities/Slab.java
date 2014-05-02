package com.dvdfu.platformer.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.dvdfu.platformer.handlers.GameConstants;
import com.dvdfu.platformer.states.GameScreen;

public class Slab extends Block {
	private float dx;
	private float dy;
	private float pushTimer;
	private int direction;
	private boolean lockedLeft;
	private boolean lockedRight;
	private Block below;

	public Slab(float x, float y, float width, float height) {
		super(x, y, width, height);
		direction = 0;
		dx = 180;
		dy = 0;
		pushTimer = 0;
		lockedLeft = false;
		lockedRight = false;
		below = null;
		setDrawable(true);
		setAnimation(new TextureRegion(new Texture(Gdx.files.internal("img/block2.png"))), 0);
	}

	public void update() {
		if (pushTimer > 0) {
			if (pushTimer > GameConstants.SPF) {
				if ((direction == 1 && !lockedRight) | (direction == -1 && !lockedLeft)) {
					body.x += pushTimer * direction * dx * GameConstants.SPF;
				}
				pushTimer -= GameConstants.SPF;
			} else {
				pushTimer = 0;
				body.x = Math.round(body.x / 16) * 16;
				lockedRight = GameScreen.blockIn(new Rectangle(body.x + width, body.y, 16, height)) != null;
				lockedLeft = GameScreen.blockIn(new Rectangle(body.x - 16, body.y, 16, height)) != null;
			}
		}
		if (dy == 0) {
			below = GameScreen.blockIn(new Rectangle(body.x, body.y - 1, width, 1));
		} else {
			below = GameScreen.blockIn(new Rectangle(body.x, body.y + dy * GameConstants.SPF, width, -dy * GameConstants.SPF));
		}
		if (below == null) {
			dy -= GameConstants.GRAVITY * GameConstants.SPF;
		} else {
			body.y = below.getBody().y + below.getBody().height;
			dy = 0;
		}
		body.y += dy * GameConstants.SPF;
	}

	public void push(int direction) {
		this.direction = direction;
		if (pushTimer == 0 && dy == 0) {
			lockedRight = GameScreen.blockIn(new Rectangle(body.x + width, body.y, 16, height)) != null;
			lockedLeft = GameScreen.blockIn(new Rectangle(body.x - 16, body.y, 16, height)) != null;
			pushTimer = 0.4f;
		}
	}
}
