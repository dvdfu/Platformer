package com.dvdfu.platformer.handlers;

public class DynamicObject extends GameObject {
	protected float dx;
	protected float dy;
	
	public DynamicObject() {
		super();
	}
	
	public DynamicObject(float x, float y, float width, float height) {
		super(x, y, width, height);
	}
	
	public void update(float dt) {
		super.update(dt);
		x += dx*dt;
		y += dy*dt;
	}
	
	/*public void setdx(float dx) { this.dx = dx; }
	public void setdy(float dy) { this.dy = dy; }
	public float getdx() { return dx; }
	public float getdy() { return dy; }*/
}
