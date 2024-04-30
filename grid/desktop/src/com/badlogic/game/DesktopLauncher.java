package com.badlogic.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.game.Main;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("A refreshing game of blackbox hexagon made by the Pookies (tm)");
		config.setWindowedMode(1600, 900);
		config.setForegroundFPS(60);
		config.setTitle("BlackBoxGame");
		new Lwjgl3Application(new Main(), config);
	}
}
