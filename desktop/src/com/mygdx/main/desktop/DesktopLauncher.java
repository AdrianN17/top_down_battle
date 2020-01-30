package com.mygdx.main.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.main.*;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Escena_juego escena = new Base_Juego();
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 640;
		config.height = 640;
		config.title = "Top Down Battle";
		config.resizable = false;
		config.x = 0;
		config.y = 0;
		new LwjglApplication(escena, config);
	}
}
