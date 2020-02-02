package com.mygdx.main.launcher;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import java.io.IOException;


public class ServerLauncher {
    public static void main(String[] args) throws IOException {

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        new LwjglApplication(new Servidor_libgdx(), config);


    }
}