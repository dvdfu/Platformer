package com.dvdfu.platformer.states;

import com.badlogic.gdx.Screen;
import com.dvdfu.platformer.handlers.Input;

public class Menu implements Screen {
	GameScreen game;
	
	public Menu(GameScreen game) {
		this.game = game;
	}

	public void render(float delta) {
		if (Input.mouseClick) {
			game.getScreen();
		}
	}

	public void resize(int width, int height) {}

	public void show() {}

	public void hide() {}

	public void pause() {}

	public void resume() {}

	public void dispose() {}
}
