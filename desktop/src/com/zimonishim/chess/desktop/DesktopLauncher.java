package com.zimonishim.chess.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.zimonishim.chess.GameHandler;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "Chess";				//Sets the text in the bar of the Application to "Chess".
		config.pauseWhenMinimized = false; //Keeps running the game despite not being shown on the screen.

		new LwjglApplication(new GameHandler(), config);
	}
}
