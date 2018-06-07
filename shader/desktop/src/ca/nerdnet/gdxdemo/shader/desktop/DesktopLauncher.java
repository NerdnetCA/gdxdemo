package ca.nerdnet.gdxdemo.shader.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import ca.nerdnet.brucie.core.BrucieConfig;
import ca.nerdnet.gdxdemo.shader.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		// This sets the window size
		// Future: matching up resolutions becomes important in some situations.
		//   some kind of resolution management might be nice here
		config.width = 1024;
		config.height = 680;

		// Create BrucieConfig instance. This remains the same across platforms
		BrucieConfig bconfig = new BrucieConfig();

		// Create game
		MyGdxGame game = new MyGdxGame(bconfig);

		// Register features
		//game.registerFeature("N);

		// Launch the LibGDX app
		new LwjglApplication(game, config);	}
}
