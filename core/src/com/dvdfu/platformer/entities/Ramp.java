package com.dvdfu.platformer.entities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Ramp extends Block {
	private int slope;

	public Ramp(float x, float y, float width, float height, int slope) {
		super(x, y, width, height);
		this.slope = slope;
	}

	public void render(ShapeRenderer sr) {
		sr.begin(ShapeType.Line);
		sr.setColor(1, 0, 1, 1);
		sr.line(x, y, x + width, y);
		if (slope > 0) {
			sr.line(x, y, x + width, y + height);
			sr.line(x + width, y, x + width, y + height);
		} else {
			sr.line(x, y + height, x + width, y);
			sr.line(x, y, x, y + height);
		}
		sr.end();
	}
	
	public float getHeight(float x) {
		if (slope > 0) {
			if (x > this.x + width) {
				return height;
			} else if (x < this.x) {
				return 0;
			} else {
				return x - this.x;
			}
		} else {
			if (x > this.x + width) {
				return 0;
			} else if (x < this.x) {
				return height;
			} else {
				return x - this.x;
			}
		}
	}
}
