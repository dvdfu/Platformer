package com.dvdfu.platformer.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.dvdfu.platformer.states.Play;

public class Slab extends Block {
	final private float GRAVITY= 800f;
	private float dx;
	private float dy;
	private float pushTimer;
	private int direction;
	private boolean lockedLeft;
	private boolean lockedRight;

	public Slab(float x, float y, float width, float height) {
		super(x, y, width, height);
		direction = 0;
		dx = 180;
		dy = 0;
		pushTimer = 0;
		lockedLeft = false;
		lockedRight = false;
		setDrawable(true);
		setAnimation(new TextureRegion(new Texture(Gdx.files.internal("img/block2.png"))), 0);
	}

	public void update(float dt) {
		if (pushTimer > 0) {
			if (pushTimer > dt) {
				if ((direction == 1 && !lockedRight) | (direction == -1 && !lockedLeft)) {
					x += pushTimer * direction * dx * dt;
				}
				pushTimer -= dt;
			} else {
				pushTimer = 0;
				x = Math.round(x / 16) * 16;
				lockedRight = Play.blockIn(new Rectangle(x + width, y, 16, height)) != null;
				lockedLeft = Play.blockIn(new Rectangle(x - 16, y, 16, height)) != null;
			}
		}
		Block b = Play.blockIn(new Rectangle(x, y - 16, width, 16));
		if (b == null) {
			dy -= GRAVITY * dt;
		} else {
			y = b.getBody().y + b.getBody().height;
			dy = 0;
		}
		y += dy * dt;
		body.x = x;
		body.y = y;
	}

	public void push(int direction) {
		this.direction = direction;
		if (pushTimer == 0 && dy == 0) {
			lockedRight = Play.blockIn(new Rectangle(x + width, y, 16, height)) != null;
			lockedLeft = Play.blockIn(new Rectangle(x - 16, y, 16, height)) != null;
			pushTimer = 0.4f;
		}
	}
}
