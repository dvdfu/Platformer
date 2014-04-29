package com.dvdfu.platformer.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.dvdfu.platformer.handlers.DynamicObject;
import com.dvdfu.platformer.states.Play;

public class Player extends DynamicObject {
	private boolean ground;
	private float gravity;
	private float deltatime;
	private Rectangle xprojection;
	private Rectangle yprojection;
	private Rectangle xcollision;
	private Rectangle ycollision;

	public Player() {
		super(320, 320, 32, 32);
		load();
		ground = false;
		gravity = 1000f;
		xprojection = new Rectangle(x, y, width, height);
		yprojection = new Rectangle(x, y, width, height);
		xcollision = null;
		ycollision = null;
	}

	private void load() {

		TextureRegion sprite[] = new TextureRegion[3];
		for (int i = 0; i < 3; i++) {
			sprite[i] = new TextureRegion(new Texture(Gdx.files.internal("img/block" + i + ".png")));
		}
		setAnimation(sprite, 1 / 3f);
		setOffset(0, 0);
	}

	public void update(float dt) {
		deltatime = dt;
		yprojection.setPosition(x, y+dy*dt*2);
		ycollision = Play.blockIn(yprojection);
		xprojection.setPosition(x+dx*dt*2, y);
		xcollision = Play.blockIn(xprojection);
		if (y < 0) {
			y = 0;
			ground = true;
		}
		if (ground) {
			dy = 0;
		}
		else {
			dy -= gravity*dt;
		}
		if (ycollision != null) {
			if (dy < 0) {
				dy = 0;
				y = ycollision.y+ycollision.height;
				ground = true;
			}
			if (dy > 0) {
				dy = 0;
				y = ycollision.y-height;
				ground = false;
			}
		}
		if (xcollision != null) {
			if (dx > 0) {
				dx = 0;
				x = xcollision.x-width;
			}
			if (dx < 0) {
				dx = 0;
				x = xcollision.x+xcollision.width;
			}
		}
		super.update(deltatime);
		dx = 0;
	}

	public void moveUp() {
		dy = 400;
		ground = false;
	}

	public void moveDown() {
		ground = false;
	}

	public void moveLeft() {
		dx = -200;
	}

	public void moveRight() {
		dx = 200;
	}
	
	public void rp (ShapeRenderer sr) {
		sr.begin(ShapeType.Line);
		sr.setColor(Color.YELLOW);
		sr.rect(xprojection.x, xprojection.y, xprojection.width, xprojection.height);
		if (xcollision != null) {
			sr.rect(xcollision.x, xcollision.y, xcollision.width, xcollision.height);
		}

		sr.setColor(Color.BLUE);
		sr.rect(yprojection.x, yprojection.y, yprojection.width, yprojection.height);
		if (ycollision != null) {
			sr.rect(ycollision.x, ycollision.y, ycollision.width, ycollision.height);
		}
		sr.end();
	}
}
