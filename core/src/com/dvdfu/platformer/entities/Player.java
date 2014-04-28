package com.dvdfu.platformer.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.dvdfu.platformer.handlers.DynamicObject;

public class Player extends DynamicObject {
	public boolean collision;
	private boolean ground;
	private float time;

	public Player() {
		super(300, 200, 32, 32);
		load();
		ground = false;
		time = 0;
		collision = false;
	}

	private void load() {
		
		TextureRegion sprite[] = new TextureRegion[3];
		for (int i = 0; i < 3; i++) {
			sprite[i] = new TextureRegion(new Texture(Gdx.files.internal("img/block" + i + ".png")));
		}
		setAnimation(sprite, 1 / 3f);
		setOffset(0, 0);
		//dx = 100;
	}

	public void update(float dt) {
		time += dt;
		//dx = 300*MathUtils.cos(time*2);
		//dy = 300*MathUtils.sin(time*2);
		super.update(dt);
		dx = 0; 
		dy = 0;
	}
	
	public void moveUp() { dy = 100; }
	public void moveDown() { dy = -100; }
	public void moveLeft() { dx = -100; }
	public void moveRight() { dx = 100; }
}
