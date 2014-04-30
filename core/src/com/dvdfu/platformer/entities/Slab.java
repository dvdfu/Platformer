package com.dvdfu.platformer.entities;

import com.badlogic.gdx.math.Rectangle;
import com.dvdfu.platformer.states.Play;

public class Slab extends Block {
	private float dx;
	private float pushTimer;
	private int direction;
	private boolean lockedLeft;
	private boolean lockedRight;

	public Slab(float x, float y, float width, float height) {
		super(x, y, width, height);
		direction = 0;
		dx = 180;
		pushTimer = 0;
		lockedLeft = false;
		lockedRight = false;
		setDrawable(true);
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
		body.x = x;
	}

	public void push(int direction) {
		this.direction = direction;
		if (pushTimer == 0) {
			lockedRight = Play.blockIn(new Rectangle(x + width, y, 16, height)) != null;
			lockedLeft = Play.blockIn(new Rectangle(x - 16, y, 16, height)) != null;
			pushTimer = 0.4f;
		}
	}
}
