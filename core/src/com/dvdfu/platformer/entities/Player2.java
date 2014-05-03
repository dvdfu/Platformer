package com.dvdfu.platformer.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
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
	private boolean jump;
	private boolean moveLeft;
	private boolean moveRight;
	private Block yCollision = null;
	private Block xCollision = null;
	private Rectangle xProjection = new Rectangle();
	private Rectangle yProjection = new Rectangle();
	private TextureRegion sprGround[];
	private TextureRegion sprJump[];
	private TextureRegion sprFall[];

	private enum YStates {
		GROUND, JUMPING, FALLING
	};

	private enum XStates {
		IDLE, MOVING, PUSHING
	};

	private YStates yState;
	private XStates xState;

	public Player2() {
		super(320, 320, 32, 32);
		load();
		vx = 0;
		vy = 0;
		keyUp = Input.ARROW_UP;
		keyLeft = Input.ARROW_LEFT;
		keyRight = Input.ARROW_RIGHT;
		yState = YStates.FALLING;
		xState = XStates.IDLE;
	}

	private void load() {
		sprGround = new TextureRegion[2];
		sprJump = new TextureRegion[2];
		sprFall = new TextureRegion[2];
		for (int i = 0; i < sprGround.length; i++) {
			sprGround[i] = new TextureRegion(new Texture(Gdx.files.internal("img/ground.png")), i * 32, 0, 32, 32);
			sprJump[i] = new TextureRegion(new Texture(Gdx.files.internal("img/jump.png")), i * 32, 0, 32, 32);
			sprFall[i] = new TextureRegion(new Texture(Gdx.files.internal("img/fall.png")), i * 32, 0, 32, 32);
		}
		this.sprite.setSprite(sprFall);
		setOffset(0, 0);
	}

	private void changeYState(YStates state) {
		switch (state) {
		case JUMPING:
			getSprite().setSprite(sprJump);
			vy = 400;
			if (xState == XStates.PUSHING) {
				xState = XStates.IDLE;
			}
			break;
		case FALLING:
			getSprite().setSprite(sprFall);
			break;
		case GROUND:
			getSprite().setSprite(sprGround);
			vy = 0;
			y = yCollision.getY() + yCollision.getHeight();
			break;
		}
		this.yState = state;
	}

	private void handleYState() {
		y += vy * Vars.SPF;
		body.y = y;
		yProjection.set(x, y + vy * Vars.SPF, width, height);
		switch (yState) {
		case JUMPING:
			vy -= Vars.GRAVITY * Vars.SPF;
			if (vy < 0) {
				changeYState(YStates.FALLING);
			} else {
				yCollision = GameScreen.blockIn(yProjection);
				if (yCollision != null && !(yCollision instanceof Platform)) {
					changeYState(YStates.FALLING);
					vy = 0;
					y = yCollision.getY() - height;
				}
			}
			break;
		case FALLING:
			vy -= Vars.GRAVITY * Vars.SPF;
			yCollision = GameScreen.blockIn(yProjection);
			if (yCollision != null) {
				if (!(yCollision instanceof Platform) || y > yCollision.getY()) {
					changeYState(YStates.GROUND);
				}
			}
			break;
		case GROUND:
			yCollision = GameScreen.blockIn(new Rectangle(x, y - 1, width, 1));
			if (yCollision == null) {
				changeYState(YStates.FALLING);
			} else {
				if (jump) {
					yCollision = GameScreen.blockIn(new Rectangle(x, y + height, width, 1));
					if (yCollision == null || yCollision instanceof Platform) {
						changeYState(YStates.JUMPING);
					}
				}
			}
			break;
		}
	}

	private void handleXState() {
		x += vx * Vars.SPF;
		body.x = x;
		xProjection.set(x + vx * Vars.SPF, y, width, height);
		switch (xState) {
		case IDLE:
			if (!(moveLeft == moveRight)) {
				if (moveLeft) {
					xCollision = GameScreen.blockIn(new Rectangle(x - 1, y, 1, height));
					if (xCollision == null || xCollision instanceof Platform) {
						xState = XStates.MOVING;
					} else if (xCollision instanceof Slab && yState == YStates.GROUND) {
						xState = XStates.PUSHING;
					}
				}
				if (moveRight) {
					xCollision = GameScreen.blockIn(new Rectangle(x + width, y, 1, height));
					if (xCollision == null || xCollision instanceof Platform) {
						xState = XStates.MOVING;
					} else if (xCollision instanceof Slab && yState == YStates.GROUND) {
						xState = XStates.PUSHING;
					}
				}
			}
			break;
		case MOVING:
			if (moveLeft == moveRight) {
				xState = XStates.IDLE;
				vx = 0;
				x = MathUtils.round(x / 4) * 4;
			} else {
				xCollision = GameScreen.blockIn(xProjection);
				if (xCollision == null || xCollision instanceof Platform) {
					if (moveRight) {
						vx = 200;
					}
					if (moveLeft) {
						vx = -200;
					}
				} else {
					if (vx > 0) {
						x = xCollision.getX() - width;
					}
					if (vx < 0) {
						x = xCollision.getX() + xCollision.getWidth();
					}
					if (xCollision instanceof Slab && yState == YStates.GROUND) {
						xState = XStates.PUSHING;
					} else {
						xState = XStates.IDLE;
						vx = 0;
						x = MathUtils.round(x / 4) * 4;
					}
				}
			}
			break;
		case PUSHING:
			xCollision = GameScreen.blockIn(xProjection);
			if (moveLeft == moveRight) {
				xState = XStates.IDLE;
				vx = 0;
			} else if (xCollision == null) {
				xState = XStates.MOVING;
			} else {
				if (vx > 0) {
					x = xCollision.getX() - width;
					if (xCollision instanceof Slab) {
						((Slab) xCollision).push(1);
					} else {
						xState = XStates.IDLE;
					}
				} else if (vx < 0) {
					x = xCollision.getX() + xCollision.getWidth();
					if (xCollision instanceof Slab) {
						((Slab) xCollision).push(-1);
					} else {
						xState = XStates.IDLE;
					}
				}
			}
			break;
		}
	}

	public void update() {
		sprite.update();
		keyListener();
		handleXState();
		handleYState();
	}

	private void keyListener() {
		jump = Input.KeyPressed(keyUp);
		moveLeft = Input.KeyDown(keyLeft);
		moveRight = Input.KeyDown(keyRight);
	}

	public void render(ShapeRenderer sr) {
		sr.begin(ShapeType.Line);
		sr.setColor(Color.YELLOW);
		sr.rect(xProjection.x, xProjection.y, xProjection.width, xProjection.height);
		if (xCollision != null) {
			sr.rect(xCollision.getX(), xCollision.getY(), xCollision.getWidth(), xCollision.getHeight());
		}
		sr.setColor(Color.CYAN);
		sr.rect(yProjection.x, yProjection.y, yProjection.width, yProjection.height);
		if (yCollision != null) {
			sr.rect(yCollision.getX(), yCollision.getY(), yCollision.getWidth(), yCollision.getHeight());
		}
		sr.end();
		//super.render(sr);
	}
}