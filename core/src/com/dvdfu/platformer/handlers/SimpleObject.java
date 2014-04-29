package com.dvdfu.platformer.handlers;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

public class SimpleObject {
	protected Rectangle body;
	protected float x;
	protected float y;
	protected float width;
	protected float height;

	public SimpleObject() {
		this(0, 0, 0, 0);
	}

	public SimpleObject(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		body = new Rectangle(x, y, width, height);
	}

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void setBody(float width, float height) {
		this.width = width;
		this.height = height;
	}

	public void render(ShapeRenderer sr) {
		sr.begin(ShapeType.Filled);
		sr.setColor(1, 0, 0, 1);
		sr.rect(x, y, width, height);
		sr.end();
	}

	public Rectangle getBody() {
		return body;
	}
	
	public float getx() {
		return x;
	}
	
	public float gety() {
		return y;
	}
}
