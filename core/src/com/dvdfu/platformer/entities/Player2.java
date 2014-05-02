package com.dvdfu.platformer.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.dvdfu.platformer.handlers.Vars;
import com.dvdfu.platformer.handlers.GameObject;
import com.dvdfu.platformer.handlers.Input;
import com.dvdfu.platformer.states.GameScreen;

public class Player2 extends GameObject {
	private float vx;
	private float vy;
	private int keyUp;
	private int keyLeft;
	private int keyRight;

	public Player2() {
		super(320, 320, 32, 32);
		load();
		vx = 0;
		vy = 0;
		keyUp = Input.ARROW_UP;
		keyLeft = Input.ARROW_LEFT;
		keyRight = Input.ARROW_RIGHT;
	}

	private void load() {
		TextureRegion sprIdleLeft = new TextureRegion(new Texture(Gdx.files.internal("img/block0.png")));
		TextureRegion sprIdleRight = new TextureRegion(new Texture(Gdx.files.internal("img/block1.png")));
		TextureRegion sprJumpLeft = new TextureRegion(new Texture(Gdx.files.internal("img/block2.png")));
		TextureRegion sprJumpRight = new TextureRegion(new Texture(Gdx.files.internal("img/block3.png")));
		this.sprite.setSprite(sprIdleLeft);
		setOffset(0, 0);
	}

	public void update() {}

	public void keyListener() {
		if (Input.KeyPressed(keyUp)) {
			jump();
		}
		if (Input.KeyDown(keyLeft)) {
			moveLeft();
		}
		if (Input.KeyDown(keyRight)) {
			moveRight();
		}
	}

	private void jump() {}

	private void moveLeft() {}

	private void moveRight() {}
}