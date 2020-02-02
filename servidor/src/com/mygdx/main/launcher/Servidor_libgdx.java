package com.mygdx.main.launcher;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import com.libs.modelos.array_data_inicial;
import com.libs.modelos.data_inicial;
import com.libs.modelos_principal.Event;
import com.libs.multiplayer.servidor.Servidor;
import com.libs.runnable.custom_runnable;
import com.mygdx.main.entidades.entidad.player;

import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;

public class Servidor_libgdx extends Game {

    public Servidor servidor;
    public World world;

    public Array<player> list_player;


    @Override
    public void create() {
        servidor = new Servidor();
        listener();

        list_player = new Array<>();

        world = new World(new Vector2(0, 0), true);

        ;

    }

    @Override
    public void render()
    {

    }

    @Override
    public void dispose()
    {
        servidor.close();
    }

    public void listener()
    {
        //class

        servidor.add_classes(new Array<Class>(){{
            add(Array.class);
            add(data_inicial.class);
            add(array_data_inicial.class);
        }});

        //trigger

        servidor.add_trigger("Connect", new custom_runnable(){
            @Override
            public void run()
            {
                //para el servidor
                int id = this.connection.getID()-1;
                Vector2 vec = Constantes_Server.punto_inicio.get(id);

                list_player.add(new player(vec.x,vec.y, id,world));

                Array<data_inicial> lista_inicial= new Array();

                for(player pl : list_player)
                {
                    Vector2 posicion = Constantes_Server.punto_inicio.get(pl.id);

                    data_inicial da = new data_inicial();
                    da.id = pl.id;
                    da.x = posicion.x;
                    da.y = posicion.y;

                    lista_inicial.add(da);
                }
                array_data_inicial al_da = new array_data_inicial();
                al_da.array_data = lista_inicial;
                al_da.id = id;

                servidor.envio.Send("Inicializar",al_da,this.connection);

            }} );
    }
}
