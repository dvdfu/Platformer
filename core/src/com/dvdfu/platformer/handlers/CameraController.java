package com.dvdfu.platformer.handlers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

public class CameraController {
	private OrthographicCamera cam;
	private int pan;
	private Vector2 pos;
	private Vector2 dist;
	private Vector2 lead;
	
	public CameraController(float viewWidth, float viewHeight) {
		cam = new OrthographicCamera(viewWidth, viewHeight);
		cam.setToOrtho(false, viewWidth, viewHeight);
		cam.position.set(viewWidth / 2f, viewWidth / 2f, 0f);
		cam.update();
		pan = 0;
		pos = new Vector2(cam.position.x, cam.position.y);
		dist = new Vector2();
		lead = new Vector2();
	}
	
	public void setPan(int pan) {
		this.pan = pan;
	}
	
	public OrthographicCamera getCam() {
		return cam;
	}
	
	public void follow(float x, float y) {
		lead.x = x;
		lead.y = y;
	}
	
	public void update() {
		cam.update();
		cam.position.x = lead.x;
		cam.position.y = lead.y;
		dist = pos.sub(lead);
	}
}
