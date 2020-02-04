package com.mygdx.main;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.libs.modelos.data_cada_tiempo;
import com.libs.modelos.data_disparo;
import com.mygdx.entidad.Player;


public class Base_Juego extends Escena_juego {

    Box2DDebugRenderer debugRenderer;
    Matrix4 debugMatrix;


    @Override
    public void initialize() {
        debugRenderer = new Box2DDebugRenderer();

        timer.Every("Enviar_data_al_Servidor", 0.2f, new Runnable() {
            @Override
            public void run() {
                if(index_player!= -1) {
                    Player player = list_player.get(index_player);

                    if (player != null) {
                        data_cada_tiempo dct = new data_cada_tiempo();

                        dct.android_angulo = player.radio_android;
                        dct.angulo = player.getRotation();
                        dct.movh = player.get_enum_h();
                        dct.movv = player.get_enum_v();
                        dct.id = player.id;

                        cliente.envio.SendClient("Posiciones", dct);
                    }
                }
            }
        });

    }

    @Override
    public void update(float dt) {
        world.step(1f/60f, 6, 2);

        if(index_player!= -1)
        {
            if(Gdx.app.getType() == Application.ApplicationType.Desktop)
            {
                list_player.get(index_player).get_angulo(state.getCamera(),Gdx.input.getX(),Gdx.input.getY());
            }

        }
    }

    @Override
    public boolean keyDown(int keycode) {


        if(Gdx.app.getType() == Application.ApplicationType.Desktop)
        {
            if(index_player!= -1)
            {
                list_player.get(index_player).keyDown(keycode);
            }

        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(Gdx.app.getType() == Application.ApplicationType.Desktop) {
            if(index_player!= -1)
            {
                list_player.get(index_player).keyUp(keycode);
            }


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

        if(Gdx.app.getType() == Application.ApplicationType.Desktop && index_player!= -1)
        {
            if(button == Input.Buttons.LEFT) {

                Player pl = list_player.get(index_player);


                if(pl.arma_index!=0)
                {
                    list_player.get(index_player).disparando = true;

                    data_disparo dd = new data_disparo();
                    dd.disparando = true;
                    dd.bala = pl.arma_index;
                    dd.id = index_player;
                }



            }
            else if(button == Input.Buttons.RIGHT)
            {
                final Player pl = list_player.get(index_player);

                if(pl.arma_index!=0)
                {
                    float time = pl.balas.balas.get(pl.arma_index-1).velocidad_recarga;
                    pl.recargando =true;

                    timer.After("disparo_" + pl.id, time, new Runnable() {
                        @Override
                        public void run() {
                            data_disparo dd = new data_disparo();
                            dd.bala = pl.arma_index;
                            dd.id = index_player;

                            list_player.get(index_player).recargando = true;

                            cliente.envio.SendClient("recargar", dd);
                        }
                    });

                }
            }


        }
        else if(Gdx.app.getType() == Application.ApplicationType.Android && index_player!= -1)
        {
            if (pointer == 0) {

                list_player.get(index_player).set_inicial_vector(screenX,screenY);


            }
            else if(pointer == 1)
            {

                Player pl = list_player.get(index_player);

                pl.get_angulo(state.getCamera(),screenX,screenY);

                if(pl.arma_index!=0)
                {
                    list_player.get(index_player).disparando = true;

                    data_disparo dd = new data_disparo();
                    dd.disparando = true;
                    dd.bala = pl.arma_index;
                    dd.id = index_player;

                    cliente.envio.SendClient("disparar", dd);
                }
            }
        }



        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(Gdx.app.getType() == Application.ApplicationType.Desktop && index_player!= -1)
        {

            if(button == Input.Buttons.LEFT)
            {
                Player pl = list_player.get(index_player);

                if(index_player!= -1) {
                    pl.disparando = false;

                    data_disparo dd = new data_disparo();
                    dd.disparando = false;
                    dd.bala = pl.arma_index;
                    dd.id = index_player;

                    cliente.envio.SendClient("disparar", dd);
                }
            }
            else if(button == Input.Buttons.RIGHT)
            {

            }

        }
        else if(Gdx.app.getType() == Application.ApplicationType.Android && index_player!= -1)
        {
            if(pointer == 0)
            {
                list_player.get(index_player).soltar_mover_android();

            }
            else if(pointer == 1)
            {
                Player pl = list_player.get(index_player);

                if(index_player!= -1) {
                    list_player.get(index_player).disparando = false;

                    data_disparo dd = new data_disparo();
                    dd.disparando = false;
                    dd.bala = list_player.get(index_player).arma_index;
                    dd.id = index_player;

                    cliente.envio.SendClient("disparar", dd);
                }
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
                if(index_player!= -1)
                {
                    list_player.get(index_player).get_angulo_android(state.getCamera(),screenX,screenY);
                    list_player.get(index_player).presionar_mover_android();
                }

            }
            else if(pointer == 1)
            {
                if(index_player!= -1)
                {
                    list_player.get(index_player).get_angulo(state.getCamera(),screenX,screenY);
                }

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
