package com.dvdfu.platformer.handlers;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class CameraController {
	private OrthographicCamera cam;
	private float followx;
	private float followy;
	
	public CameraController(float viewWidth, float viewHeight) {
		cam = new OrthographicCamera(viewWidth, viewHeight);
		cam.setToOrtho(false, viewWidth, viewHeight);
		cam.position.set(viewWidth / 2f, viewWidth / 2f, 0f);
		cam.update();
	}
	
	public OrthographicCamera getCam() {
		return cam;
	}
	
	public void follow(float x, float y) {
		followx = x;
		followy = y;
	}
	
	public void update() {
		cam.update();
	}
}
