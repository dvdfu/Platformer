package com.dvdfu.platformer.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.dvdfu.platformer.handlers.DynamicObject;
import com.dvdfu.platformer.states.Play;

public class Player extends DynamicObject {
	public boolean collision;
	private boolean ground;
	private boolean moving;
	private float time;
	private float gravity;
	private float deltatime;

	public Player() {
		super(300, 200, 32, 32);
		load();
		ground = false;
		moving = false;
		time = 0;
		collision = false;
		gravity = 1000f;
	}

	private void load() {

		TextureRegion sprite[] = new TextureRegion[3];
		for (int i = 0; i < 3; i++) {
			sprite[i] = new TextureRegion(new Texture(Gdx.files.internal("img/block" + i + ".png")));
		}
		setAnimation(sprite, 1 / 3f);
		setOffset(0, 0);
		// dx = 100;
	}

	public void update(float dt) {
		deltatime = dt;
		time += deltatime;
		// dx = 300*MathUtils.cos(time*2);
		// dy = 300*MathUtils.sin(time*2);
		super.update(deltatime);
		if (Math.abs(dx) > 0.1) {
			dx *= 0.9;
		} else {
			dx = 0;
		}
		dy -= gravity * deltatime;
		if (y < 0) {
			ground = true;
			y = 0;
		}
	}

	public void moveUp() {
		if (ground) {
			dy = 400;
			ground = false;
		}
	}

	public void moveDown() {
	}

	public void moveLeft() {
		if (Play.spaceFree(x-200*deltatime, y)) {
			dx = -200; 
		}
	}

	public void moveRight() {
		if (Play.spaceFree(x+200*deltatime, y)) {
			dx = 200; 
		}
	}
}
