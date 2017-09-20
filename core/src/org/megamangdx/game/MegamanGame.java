package org.megamangdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
	public static final short BULLET_BIT = 32;
	public static final short NOTHING_BIT = 64;

	public static final String MEGAMAN_WILY_STAGE_1_2 = "audio/music/wily-stage-1-2.mp3";
	public static final String MEGAMAN_DEFEAT_SOUND = "audio/sounds/megaman-defeat.mp3";
	public static final String MEGAMAN_BUSTER_SOUND = "audio/sounds/megaman-buster.mp3";
	public static final String MEGAMAN_DAMAGE = "audio/sounds/megaman-damage.mp3";

	public static AssetManager assetManager;

	@Override
	public void create () {
		batch = new SpriteBatch();
		assetManager = new AssetManager();
		assetManager.load(MEGAMAN_WILY_STAGE_1_2, Music.class);
		assetManager.load(MEGAMAN_DEFEAT_SOUND, Sound.class);
		assetManager.load(MEGAMAN_BUSTER_SOUND, Sound.class);
		assetManager.load(MEGAMAN_DAMAGE, Sound.class);
		assetManager.finishLoading();

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
