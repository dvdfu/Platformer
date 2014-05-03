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
	final private float MOVE_SPEED = 160f;
	final private float JUMP_SPEED = 400f;
	final private float AX = 20f;
	final private float AX_AIR = 4f;
	private float vx;
	private float vy;
	private int keyUp;
	private int keyLeft;
	private int keyRight;
	private int keyDown;
	private boolean jump;
	private boolean moveLeft;
	private boolean moveRight;
	private boolean forward;
	private Block yCollision = null;
	private Block xCollision = null;
	private Rectangle xProjection = new Rectangle();
	private Rectangle yProjection = new Rectangle();
	private TextureRegion sprIdleL[];
	private TextureRegion sprRunL[];
	private TextureRegion sprJumpL[];
	private TextureRegion sprFallL[];
	private TextureRegion sprIdleR[];
	private TextureRegion sprRunR[];
	private TextureRegion sprJumpR[];
	private TextureRegion sprFallR[];
	private boolean debug;

	private enum YStates {
		GROUND, JUMPING, FALLING
	};

	private enum XStates {
		IDLE, SLOWING, MOVING, PUSHING
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
		keyDown = Input.ARROW_DOWN;
		yState = YStates.FALLING;
		xState = XStates.IDLE;
		debug = false;
		forward = true;
	}

	private void load() {
		sprIdleL = new TextureRegion[2];
		sprRunL = new TextureRegion[2];
		sprJumpL = new TextureRegion[2];
		sprFallL = new TextureRegion[2];
		sprIdleR = new TextureRegion[2];
		sprRunR = new TextureRegion[2];
		sprJumpR = new TextureRegion[2];
		sprFallR = new TextureRegion[2];
		for (int i = 0; i < 2; i++) {
			sprIdleL[i] = new TextureRegion(new Texture(Gdx.files.internal("img/idleL.png")), i * 32, 0, 32, 32);
			sprRunL[i] = new TextureRegion(new Texture(Gdx.files.internal("img/runL.png")), i * 32, 0, 32, 32);
			sprJumpL[i] = new TextureRegion(new Texture(Gdx.files.internal("img/jumpL.png")), i * 32, 0, 32, 32);
			sprFallL[i] = new TextureRegion(new Texture(Gdx.files.internal("img/fallL.png")), i * 32, 0, 32, 32);
			sprIdleR[i] = new TextureRegion(new Texture(Gdx.files.internal("img/idleR.png")), i * 32, 0, 32, 32);
			sprRunR[i] = new TextureRegion(new Texture(Gdx.files.internal("img/runR.png")), i * 32, 0, 32, 32);
			sprJumpR[i] = new TextureRegion(new Texture(Gdx.files.internal("img/jumpR.png")), i * 32, 0, 32, 32);
			sprFallR[i] = new TextureRegion(new Texture(Gdx.files.internal("img/fallR.png")), i * 32, 0, 32, 32);
		}
		this.sprite.setSprite(sprFallR);
		setOffset(0, 0);
	}

	private void changeYState(YStates state) {
		switch (state) {
		case JUMPING:
			vy = JUMP_SPEED;
			if (xState == XStates.PUSHING) {
				xState = XStates.IDLE;
			}
			break;
		case FALLING:
			break;
		case GROUND:
			vy = 0;
			y = yCollision.getY() + yCollision.getHeight();
			break;
		}
		yState = state;
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
				if (!(yCollision instanceof Platform) || y - vy * Vars.SPF > yCollision.getY() + yCollision.getHeight()) {
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

	private void changeXState(XStates state) {
		switch (state) {
		case IDLE:
			vx = 0;
			x = MathUtils.round(x / 4) * 4;
			break;
		case SLOWING:
			break;
		case MOVING:
			break;
		case PUSHING:
			break;
		}
		xState = state;
	}

	private void handleXState() {
		x += vx * Vars.SPF;
		body.x = x;
		xProjection.set(x + vx * Vars.SPF, y, width, height);
		switch (xState) {
		case IDLE:
			if (!(moveLeft == moveRight)) {
				if (moveLeft) {
					forward = false;
					xCollision = GameScreen.blockIn(new Rectangle(x - 1, y, 1, height));
					if (xCollision == null || xCollision instanceof Platform) {
						changeXState(XStates.MOVING);
					} else if (xCollision instanceof Slab && yState == YStates.GROUND) {
						changeXState(XStates.PUSHING);
					}
				}
				if (moveRight) {
					forward = true;
					xCollision = GameScreen.blockIn(new Rectangle(x + width, y, 1, height));
					if (xCollision == null || xCollision instanceof Platform) {
						changeXState(XStates.MOVING);
					} else if (xCollision instanceof Slab && yState == YStates.GROUND) {
						changeXState(XStates.PUSHING);
					}
				}
			}
			break;
		case SLOWING:
			xCollision = GameScreen.blockIn(xProjection);
			if (xCollision == null || xCollision instanceof Platform) {
				if (moveLeft == moveRight) {
					if (moveLeft == moveRight && vx > AX) {
						if (yState == YStates.GROUND) {
							vx -= AX;
						} else {
							vx -= AX_AIR;
						}
					} else if (moveLeft == moveRight && vx < -AX) {
						if (yState == YStates.GROUND) {
							vx += AX;
						} else {
							vx += AX_AIR;
						}
					} else {
						changeXState(XStates.IDLE);
					}
				} else {
					changeXState(XStates.MOVING);
				}
			} else {
				if (vx > 0) {
					x = xCollision.getX() - width;
				}
				if (vx < 0) {
					x = xCollision.getX() + xCollision.getWidth();
				}
				changeXState(XStates.IDLE);
			}
			break;
		case MOVING:
			if (moveLeft == moveRight) {
				changeXState(XStates.SLOWING);
			} else {
				xCollision = GameScreen.blockIn(xProjection);
				if (xCollision == null || xCollision instanceof Platform) {
					if (moveRight) {
						forward = true;
						vx += AX;
						if (vx > MOVE_SPEED) {
							vx = MOVE_SPEED;
						}
					}
					if (moveLeft) {
						forward = false;
						vx -= AX;
						if (vx < -MOVE_SPEED) {
							vx = -MOVE_SPEED;
						}
					}
				} else {
					if (vx > 0) {
						x = xCollision.getX() - width;
					}
					if (vx < 0) {
						x = xCollision.getX() + xCollision.getWidth();
					}
					if (xCollision instanceof Slab && yState == YStates.GROUND) {
						changeXState(XStates.PUSHING);
					} else {
						changeXState(XStates.SLOWING);
					}
				}
			}
			break;
		case PUSHING:
			if (moveLeft == moveRight) {
				changeXState(XStates.MOVING);
			} else {
				if (moveLeft) {
					forward = false;
					xCollision = GameScreen.blockIn(new Rectangle(x - 16, y, 16, height));
					if (xCollision instanceof Slab) {
						x = xCollision.getX() + xCollision.getWidth();
						((Slab) xCollision).push(-1);
					} else {
						changeXState(XStates.MOVING);
					}
				}
				if (moveRight) {
					forward = true;
					xCollision = GameScreen.blockIn(new Rectangle(x + width, y, 16, height));
					if (xCollision instanceof Slab) {
						x = xCollision.getX() - width;
						((Slab) xCollision).push(1);
					} else {
						changeXState(XStates.MOVING);
					}
				}
			}
			break;
		}
	}

	public void update() {
		if (forward) {
			switch (yState) {
			case GROUND:
				if (vx == 0) {
					sprite.setSprite(sprIdleR);
				} else {
					sprite.setSprite(sprRunR);
				}
				break;
			case JUMPING:
				sprite.setSprite(sprJumpR);
				break;
			case FALLING:
				sprite.setSprite(sprFallR);
				break;
			}
		} else {
			switch (yState) {
			case GROUND:
				if (vx == 0) {
					sprite.setSprite(sprIdleL);
				} else {
					sprite.setSprite(sprRunL);
				}
				break;
			case JUMPING:
				sprite.setSprite(sprJumpL);
				break;
			case FALLING:
				sprite.setSprite(sprFallL);
				break;
			}
		}
		sprite.update();
		keyListener();
		handleXState();
		handleYState();
	}

	private void keyListener() {
		jump = Input.KeyPressed(keyUp);
		moveLeft = Input.KeyDown(keyLeft);
		moveRight = Input.KeyDown(keyRight);
		if (Input.KeyPressed(keyDown)) {
			debug = !debug;
		}
	}

	public void render(ShapeRenderer sr) {
		if (debug) {
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
		} else {
			super.render(sr);
		}
	}
}