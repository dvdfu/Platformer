package com.dvdfu.platformer.entities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Ramp extends Block {
	private boolean slope;

	public Ramp(float x, float y, float width, float height, boolean slope) {
		super(x, y, width, height);
		this.slope = slope;
	}
	
	public void render(ShapeRenderer sr) {
		sr.begin(ShapeType.Line);
		sr.setColor(1, 0, 0, 1);
		if (slope) {
			sr.line(x, y, x+width, y+height);
			sr.line(x+width, y, x+width, y+height);
		}
		else {
			sr.line(x, y+height, x+width, y);
			sr.line(x, y, x, y+height);
		}
		sr.line(x, y, x+width, y);
		sr.end();
	}

}
