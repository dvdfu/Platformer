package com.dvdfu.platformer.handlers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

public class GameObject {
	protected boolean drawable;
	protected Rectangle body;
	protected float width;
	protected float height;
	protected Animation animation;
	protected float xOffset;
	protected float yOffset;
	protected float spriteWidth;
	protected float spriteHeight;

	public GameObject() {
		this(0, 0, 0, 0, false);
	}

	public GameObject(float x, float y, float width, float height, boolean imaged) {
		this.width = width;
		this.height = height;
		this.drawable = imaged;
		body = new Rectangle(x, y, width, height);
		body.x = x;
		body.y = y;
		xOffset = 0;
		yOffset = 0;
		spriteWidth = 0;
		spriteHeight = 0;
		animation = new Animation();
	}

	public void setPosition(float x, float y) {
		body.x = x;
		body.y = y;
	}
	
	public void setDrawable(boolean drawable) {
		this.drawable = drawable;
	}

	public void setBody(float width, float height) {
		this.width = width;
		this.height = height;
	}

	public Rectangle getBody() {
		return body;
	}
	
	public float getx() {
		return body.x;
	}
	
	public float gety() {
		return body.y;
	}

	public void setOffset(float xOffset, float yOffset) {
		drawable = true;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	public void setAnimation(TextureRegion reg, float delay) {
		setAnimation(new TextureRegion[] { reg }, delay);
	}

	public void setAnimation(TextureRegion[] reg, float delay) {
		drawable = true;
		animation.setFrames(reg, delay);
		spriteWidth = reg[0].getRegionWidth();
		spriteHeight = reg[0].getRegionHeight();
	}

	public void update() {
		animation.update();
	}

	public void render(SpriteBatch sb) {
		if (drawable) {
			sb.begin();
			sb.draw(animation.getFrame(), body.x + xOffset, body.y + yOffset);
			sb.end();
		}
	}

	public void render(ShapeRenderer sr) {
		sr.begin(ShapeType.Line);
		sr.setColor(1, 0, 0, 1);
		sr.rect(body.x, body.y, width, height);
		if (drawable) {
			sr.setColor(0, 1, 0, 1);
			sr.rect(body.x + xOffset, body.y + yOffset, spriteWidth, spriteHeight);
		}
		sr.end();
	}
}
