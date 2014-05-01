package com.dvdfu.platformer.handlers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class HUDImage extends HUDObject {
	protected Animation image;
	protected float width;
	protected float height;

	public HUDImage(TextureRegion reg, float x, float y) {
		this(new TextureRegion[] { reg }, x, y);
	}

	public HUDImage(TextureRegion reg[], float x, float y) {
		super(x, y);
		image = new Animation();
		setSprite(reg);
	}

	public void setSprite(TextureRegion reg) {
		this.setSprite(new TextureRegion[] { reg });
	}

	public void setSprite(TextureRegion reg[]) {
		image.setFrames(reg, 1 / 12f);
		width = reg[0].getRegionWidth();
		height = reg[0].getRegionHeight();
	}
	
	public void render(SpriteBatch sb, OrthographicCamera cam) {
		sb.draw(image.getFrame(), x + cam.position.x - cam.viewportWidth / 2, y + cam.position.y - cam.viewportHeight / 2);
	}

	public void update() {
		image.update();
	}
}
