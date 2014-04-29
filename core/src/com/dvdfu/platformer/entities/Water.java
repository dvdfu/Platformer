package com.dvdfu.platformer.entities;

import com.badlogic.gdx.math.Vector2;
import com.dvdfu.platformer.states.Play;

public class Water {
	private Vector2 orig;
	private Vector2 contact;
	//private Vector2 left;
	//private Vector2 right;
	private boolean foundContact;
	//private boolean foundLeft;
	//private boolean foundRight;
	
	public Water(float x, float y) {
		orig = new Vector2(x, y);
		contact = orig;
		foundContact = false;
		while (!foundContact) {
			contact.y--;
			if (contact.y < 0) {
				contact.set(orig.x, 0);
				foundContact = true;
			}
			if (Play.blockAt(contact.x, contact.y) != null) {
				contact.set(orig.x, Play.blockAt(contact.x, contact.y).y+Play.blockAt(contact.x, contact.y).height);
				foundContact = true;
			}
		}
	}
}
