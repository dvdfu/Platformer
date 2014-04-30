package com.dvdfu.platformer.handlers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class HUDObject {
	protected float x;
	protected float y;

	public HUDObject(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public abstract void render(SpriteBatch sb, OrthographicCamera cam);

	public abstract void update(float dt);
}
