package com.dvdfu.platformer.handlers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class HUDCountable extends HUDImage {
	private int num;
	private float xMargin;
	private float yMargin;

	public HUDCountable(TextureRegion reg, float x, float y, float xMargin, float yMargin) {
		this(new TextureRegion[] { reg }, x, y, xMargin, yMargin);
	}

	public HUDCountable(TextureRegion reg[], float x, float y, float xMargin, float yMargin) {
		super(reg, x, y);
		this.xMargin = xMargin;
		this.yMargin = yMargin;
		num = 10;
	}

	public void render(SpriteBatch sb, OrthographicCamera cam) {
		for (int i = 0; i < num; i++) {
			sb.draw(image.getFrame(), 
					x + cam.position.x - cam.viewportWidth / 2 + i * (width + xMargin), 
					y + cam.position.y - cam.viewportHeight / 2);
		}
	}

	public void setNum(int n) {
		num = n;
	}

	public int getNum() {
		return num;
	}
}
