package com.dvdfu.platformer.entities;

import com.badlogic.gdx.math.MathUtils;
import com.dvdfu.platformer.handlers.Vars;

public class Moving extends Platform {
	
	private float t;
	private float vx;
	private float vy;

	public Moving(float x, float y, float width, float height) {
		super(x, y, width, height);
		t = 0;
	}
	
	public void update() {
		t += Vars.SPF;
		vx = 120 * MathUtils.cos(t * 2);
		//vy = 120 * MathUtils.sin(t * 2);
		x += vx * Vars.SPF;
		y += vy * Vars.SPF;
		body.x = x;
		body.y = y;
	}
	
	public float getVX() {
		return vx;
	}
	
	public float getVY() {
		return vy;
	}
}
