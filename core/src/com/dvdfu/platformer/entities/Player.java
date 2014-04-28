package com.dvdfu.platformer.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.dvdfu.platformer.handlers.DynamicObject;

public class Player extends DynamicObject {
	private float time;
	private boolean ground;
	public Player() {
		super(300, 0, 64, 64);
		// setBody(64, 64);
		TextureRegion sprite[] = new TextureRegion[3];
		for (int i = 0; i < 3; i++) {
			sprite[i] = new TextureRegion(new Texture(Gdx.files.internal("img/block" + i + ".png")));
		}
		setAnimation(sprite, 1/3f);
		setOffset(16, 16);
		time = 0;
	}
	
	public void update(float dt) {
		time += dt;
		dx = 400*MathUtils.cos(time*2);
		dy = 400*MathUtils.sin(time*2);
		super.update(dt);
	}
	
	public void moveLeft() {
		
	}
}
