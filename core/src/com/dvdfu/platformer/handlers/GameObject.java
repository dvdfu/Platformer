package com.dvdfu.platformer.handlers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

public class GameObject {
	protected Rectangle body;
	protected Animation animation;
	protected float x;
	protected float y;
	protected float width;
	protected float height;
	protected float xOffset;
	protected float yOffset;
	protected float spriteWidth;
	protected float spriteHeight;

	public GameObject() {
		this(0, 0, 0, 0);
	}

	public GameObject(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		xOffset = 0;
		yOffset = 0;
		spriteWidth = 0;
		spriteHeight = 0;
		body = new Rectangle(x, y, width, height);
		animation = new Animation();
	}

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void setOffset(float xOffset, float yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	public void setBody(float width, float height) {
		this.width = width;
		this.height = height;
	}

	public void setAnimation(TextureRegion reg, float delay) {
		setAnimation(new TextureRegion[] { reg }, delay);
	}

	public void setAnimation(TextureRegion[] reg, float delay) {
		animation.setFrames(reg, delay);
		spriteWidth = reg[0].getRegionWidth();
		spriteHeight = reg[0].getRegionHeight();
	}

	public void update(float dt) {
		animation.update(dt);
	}

	public void render(SpriteBatch sb) {
		sb.begin();
		sb.draw(animation.getFrame(), x + xOffset, y + yOffset);
		sb.end();
	}

	public void render(ShapeRenderer sr) {
		sr.begin(ShapeType.Line);
		sr.setColor(1, 0, 0, 1);
		sr.rect(x, y, width, height);
		sr.setColor(0, 1, 0, 1);
		sr.rect(x + xOffset, y + yOffset, spriteWidth, spriteHeight);
		sr.end();
	}

	public Rectangle getBody() {
		return body;
	}
}
