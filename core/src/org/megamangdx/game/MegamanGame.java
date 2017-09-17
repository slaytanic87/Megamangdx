package org.megamangdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.megamangdx.game.screens.PlayScreen;

public class MegamanGame extends Game {

	public SpriteBatch batch;
	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 400;
	public static final float PPM = 150;

	// Collision classes as bits
	public static final short PLAYER_BIT = 1;
	public static final short ENEMY_BIT = 2;
	public static final short WALL_BIT = 4;
	public static final short GROUND_BIT = 8;
	public static final short PLATFORM_BIT = 16;

	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
		batch.dispose();
	}
}
