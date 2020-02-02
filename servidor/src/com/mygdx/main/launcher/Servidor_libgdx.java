package com.mygdx.main.launcher;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.main.entidades.entidad.player;
import com.mygdx.multiplayer_cliente.Event;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;

public class Servidor_libgdx extends Game implements InputProcessor {

    public Server server ;
    public World world;

    public ArrayList<player> list_player;


    @Override
    public void create() {

        list_player = new ArrayList<>();

        world = new World(new Vector2(0, 0), true);


        server = new Server();

        server.start();

        try {
            InetSocketAddress socket1 = new InetSocketAddress("192.168.0.3", 22122);
            InetSocketAddress socket2 = new InetSocketAddress("192.168.0.3", 22123);

            server.bind(socket1, socket2);

        } catch (IOException e) {
            e.printStackTrace();
        }

        server.getKryo().register(Event.class );
        server.getKryo().register(byte[].class);

        server.addListener(new Kryonet_Listener_Server(this));
    }

    @Override
    public void render()
    {

    }

    @Override
    public void dispose()
    {
        server.close();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
