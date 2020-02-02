package com.mygdx.multiplayer_cliente;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.entidad.Player;
import com.mygdx.main.Base_Juego;
import com.mygdx.main.Escena_juego;
import com.mygdx.multiplayer_modelos.data_inicial;

import org.apache.commons.lang3.SerializationUtils;

public class Kryonet_Listener_Cliente extends Listener {

    public Escena_juego base;

    public Kryonet_Listener_Cliente(Escena_juego base)
    {
        this.base = base;
    }

    public void connected(Connection connection)
    {
        System.out.println("cliente conectado al servidor" + connection.getID());
    }

    public void disconnected(Connection connection)
    {
        System.out.println("cliente desconectado al servidor" + connection.getID());
    }


    public void received (Connection connection, Object object) {
        if (object instanceof Event) {
            Event response = (Event)object;

            System.out.println(response.name);
            received_list(response);


        }
    }

    public void received_list(Event event)
    {
        switch(event.name)
        {
            case "Inicializar":
            {
                final data_inicial obj = SerializationUtils.deserialize(event.obj);

                base.index_player = obj.id-1;

                Gdx.app.postRunnable(new Runnable() {
                    public void run() {
                        base.list_player.add(new Player(obj.x,obj.y,base.state,base.world, obj.id-1));
                    }});
            }


        }
    }
}
