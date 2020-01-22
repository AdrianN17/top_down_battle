package com.mygdx.main.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.main.*;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Escena_juego escena = new Base_Juego();
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(escena, config);
	}
}
