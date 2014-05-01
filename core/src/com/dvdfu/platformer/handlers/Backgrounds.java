package com.dvdfu.platformer.handlers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class Backgrounds {
	private Array<HUDImage> backgrounds;
	private OrthographicCamera cam;
	
	public Backgrounds() {
		backgrounds = new Array<HUDImage>();
		cam = new OrthographicCamera();
	}
	
	public void render(SpriteBatch sb) {
		sb.begin();
		for (HUDObject i : backgrounds) {
			i.render(sb, cam);
		}
		sb.end();
	}	
	public void update() {
		for (HUDObject i : backgrounds) {
			i.update();
		}
	}
	
	public void addElement(HUDImage e) {
		backgrounds.add(e);
	}
	
	public void setView(OrthographicCamera cam) {
		this.cam = cam;
	}
}
