package com.dvdfu.platformer.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.dvdfu.platformer.handlers.Vars;
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
		sprite.setSprite(new TextureRegion(new Texture(Gdx.files.internal("img/block2.png"))));
	}

	public void update() {
		if (pushTimer > 0) {
			if (pushTimer > Vars.SPF) {
				if ((direction == 1 && !lockedRight) | (direction == -1 && !lockedLeft)) {
					x += pushTimer * direction * dx * Vars.SPF;
				}
				pushTimer -= Vars.SPF;
			} else {
				pushTimer = 0;
				x = Math.round(x / 16) * 16;
				lockedRight = GameScreen.blockIn(new Rectangle(x + width, y, 16, height)) != null;
				lockedLeft = GameScreen.blockIn(new Rectangle(x - 16, y, 16, height)) != null;
			}
		}
		if (dy == 0) {
			below = GameScreen.blockIn(new Rectangle(x, y - 1, width, 1));
		} else {
			below = GameScreen.blockIn(new Rectangle(x, y + dy * Vars.SPF, width, -dy * Vars.SPF));
		}
		if (below == null) {
			dy -= Vars.GRAVITY * Vars.SPF;
		} else {
			y = below.getBody().y + below.getBody().height;
			dy = 0;
		}
		y += dy * Vars.SPF;
		body.x = x;
		body.y = y;
	}

	public void push(int direction) {
		this.direction = direction;
		if (pushTimer == 0 && dy == 0) {
			lockedRight = GameScreen.blockIn(new Rectangle(x + width, y, 16, height)) != null;
			lockedLeft = GameScreen.blockIn(new Rectangle(x - 16, y, 16, height)) != null;
			pushTimer = 0.4f;
		}
	}
	
	public void render(ShapeRenderer sr) {
		sr.begin(ShapeType.Line);
		sr.setColor(0, 1, 0, 1);
		sr.rect(x, y, width, height);
		sr.end();
	}
}