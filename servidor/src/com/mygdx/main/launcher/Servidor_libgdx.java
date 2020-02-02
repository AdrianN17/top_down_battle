package com.mygdx.main.launcher;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import com.libs.multiplayer.servidor.Servidor;
import com.mygdx.main.entidades.entidad.player;

public class Servidor_libgdx extends Game implements InputProcessor {

    public Servidor server;
    public World world;

    public Array<player> list_player;


    @Override
    public void create() {
        server = new Servidor();

        list_player = new Array<>();

        world = new World(new Vector2(0, 0), true);

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
