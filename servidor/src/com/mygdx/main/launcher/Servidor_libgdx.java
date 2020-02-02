package com.mygdx.main.launcher;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import com.libs.modelos.data_inicial;
import com.libs.modelos_principal.Event;
import com.libs.multiplayer.servidor.Servidor;
import com.libs.runnable.custom_runnable;
import com.mygdx.main.entidades.entidad.player;

import org.apache.commons.lang3.SerializationUtils;

public class Servidor_libgdx extends Game {

    public Servidor server;
    public World world;

    public Array<player> list_player;


    @Override
    public void create() {
        server = new Servidor();

        list_player = new Array<>();

        world = new World(new Vector2(0, 0), true);

        listener();

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

    public void listener()
    {
        //trigger
        server.add_trigger("Connect", new custom_runnable(){
            @Override
            public void run()
            {
                //para el servidor
                int id = this.connection.getID()-1;
                Vector2 vec = Constantes_Server.punto_inicio.get(id);

                list_player.add(new player(vec.x,vec.y, id,world));

                //para el cliente

                Vector2 posicion = Constantes_Server.punto_inicio.get(id);
                data_inicial data = new data_inicial(id,posicion.x,posicion.y);

                Event event = new Event();
                event.name = "Inicializar";
                event.obj = SerializationUtils.serialize(data);
                System.out.println(event.obj.getClass().getName());
                this.connection.sendUDP(event);

            }} );
    }
}
