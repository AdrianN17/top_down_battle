package com.mygdx.main.multiplayer;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.main.Base_Juego;
import com.mygdx.main.Escena_juego;


public class ServerLauncher {
    public static void main(String[] args) {
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
