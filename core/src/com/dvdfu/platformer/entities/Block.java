package com.dvdfu.platformer.entities;

import com.badlogic.gdx.utils.Pool.Poolable;
import com.dvdfu.platformer.handlers.GameObject;

public class Block extends GameObject implements Poolable {
	
	public Block(float x, float y, float width, float height) {
		super(x, y, width, height);
	}

	public void reset() {}
}
