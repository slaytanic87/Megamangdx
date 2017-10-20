package org.megamangdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.megamangdx.game.application.MegamanGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 400;
		config.height = 400;
		config.title = "Sandbox Game";
		new LwjglApplication(new MegamanGame(), config);
	}
}
