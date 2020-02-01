package com.mygdx.main;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.entidad.Player;

import java.io.IOException;


public class Base_Juego extends Escena_juego {

    Box2DDebugRenderer debugRenderer;
    Matrix4 debugMatrix;


    @Override
    public void initialize() {
        debugRenderer = new Box2DDebugRenderer();

        player = new Player(puntos_nacimiento.get(0).x,puntos_nacimiento.get(0).y, state, world);



    }

    @Override
    public void update(float dt) {

        //fps
        long delta = TimeUtils.timeSinceMillis(lastTimeCounted);
        lastTimeCounted = TimeUtils.millis();

        sinceChange += delta;
        if(sinceChange >= 1000) {
            sinceChange = 0;
            frameRate = Gdx.graphics.getFramesPerSecond();
        }


        if(Gdx.app.getType() == Application.ApplicationType.Desktop)
        {
            player.get_angulo(state.getCamera(),Gdx.input.getX(),Gdx.input.getY());
        }


        world.step(1f/60f, 6, 2);
    }

    @Override
    public boolean keyDown(int keycode) {

        if(Gdx.app.getType() == Application.ApplicationType.Desktop)
        {
            player.keyDown(keycode);
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(Gdx.app.getType() == Application.ApplicationType.Desktop) {
            player.keyUp(keycode);
        }

        return false;
    }

    @Override
    public void box2d_debug()
    {
        state.getBatch().setProjectionMatrix(state.getCamera().combined);


        debugMatrix = state.getBatch().getProjectionMatrix().cpy().scale(Constantes.PIXEL_IN_METERS,
                Constantes.PIXEL_IN_METERS, 0);

        state.getBatch().begin();

        state.getBatch().end();


        debugRenderer.render(world, debugMatrix);
    }

    @Override
    public boolean keyTyped(char character) {

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(Gdx.app.getType() == Application.ApplicationType.Desktop)
        {
            player.presionar_pc(button);
        }
        else if(Gdx.app.getType() == Application.ApplicationType.Android)
        {
            if (pointer == 0) {
                player.set_inicial_vector(screenX,screenY);
            }
            else if(pointer == 1)
            {
                player.get_angulo(state.getCamera(),screenX,screenY);
                //player.set_inicial_vector_2(screenX,screenY);
            }
        }



        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(Gdx.app.getType() == Application.ApplicationType.Desktop)
        {
            player.soltar_pc(button);
        }
        else if(Gdx.app.getType() == Application.ApplicationType.Android)
        {
            if(pointer == 0)
            {
                player.soltar_mover_android();
            }
            else if(pointer == 1)
            {
                player.soltar_android(pointer);
            }
        }

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        if(Gdx.app.getType() == Application.ApplicationType.Android)
        {

            if(pointer == 0)
            {
                player.get_angulo_android(state.getCamera(),screenX,screenY);
                player.presionar_mover_android();
            }
            else if(pointer == 1)
            {
                player.get_angulo(state.getCamera(),screenX,screenY);
                player.presionar_android();
            }
        }


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
