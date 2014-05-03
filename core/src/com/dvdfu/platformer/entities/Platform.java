package com.dvdfu.platformer.entities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Platform extends Block {

	public Platform(float x, float y, float width, float height) {
		super(x, y, width, height);
	}
	
	public void render(ShapeRenderer sr) {
		sr.begin(ShapeType.Line);
		sr.setColor(1, 0, 1, 1);
		sr.rect(x, y, width, height);
		sr.end();
	}
}
