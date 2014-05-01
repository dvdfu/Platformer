package com.dvdfu.platformer.handlers;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animation {
	
	private TextureRegion[] frames;
	private float time;
	private float delay;
	private int currentFrame;
	private int timesPlayed;
	
	public Animation() {}
	
	public Animation(TextureRegion[] frames) {
		this(frames, 1/12f);
	}
	
	public Animation(TextureRegion[] frames, float delay) {
		this.frames = frames;
		this.delay = delay;
		time = 0;
		currentFrame = 0;
	}
	
	public void setDelay(float f) {
		delay = f;
	}

	public void setCurrentFrame(int i) {
		currentFrame = i % frames.length;
	}

	public void setFrames(TextureRegion[] frames) {
		setFrames(frames, 1/12f);
	}
	
	public void setFrames(TextureRegion[] frames, float delay) {
		this.frames = frames;
		this.delay = delay;
		time = 0;
		currentFrame = 0;
		timesPlayed = 0;
	}
	
	public void update() {
		if(delay <= 0) return;
		time += GameConstants.SPF;
		while(time >= delay) {
			time -= delay;
			currentFrame++;
			if (currentFrame == frames.length) {
				currentFrame = 0;
				timesPlayed++;
			}
		}
	}
	
	public TextureRegion getFrame() {
		return frames[currentFrame];
	}

	public int getTimesPlayed() {
		return timesPlayed;
	}

	public boolean hasPlayedOnce() {
		return timesPlayed > 0;
	}
}
