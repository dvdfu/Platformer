package com.dvdfu.platformer.entities;

import com.badlogic.gdx.utils.Pool.Poolable;
import com.dvdfu.platformer.handlers.GameObject;

public class Block extends GameObject implements Poolable {
	
	public Block(float x, float y) {
		super(x, y, 16, 16);
	}

	public void reset() {}
}
