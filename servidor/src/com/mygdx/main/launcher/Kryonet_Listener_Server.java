package com.mygdx.main.launcher;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.entidad.Player;
import com.mygdx.main.entidades.entidad.player;
import com.mygdx.multiplayer_cliente.Event;
import com.mygdx.multiplayer_modelos.data_inicial;

import org.apache.commons.lang3.SerializationUtils;

public class Kryonet_Listener_Server extends Listener {

    public Servidor_libgdx base;

    public Kryonet_Listener_Server(Servidor_libgdx base)
    {
        this.base = base;
    }


    public void connected(final Connection connection)
    {
        System.out.println("cliente conectado " + connection.getID());

        //crear usuario

        Gdx.app.postRunnable(new Runnable() {
            public void run() {
                Vector2 vec = Constantes_Server.punto_inicio.get(connection.getID()-1);

                base.list_player.add(new player(vec.x,vec.y, connection.getID(),base.world));
            }});



        Vector2 posicion = Constantes_Server.punto_inicio.get(connection.getID());
        data_inicial data = new data_inicial(connection.getID(),posicion.x,posicion.y);


        Event event = new Event();
        event.name = "Inicializar";
        event.obj = SerializationUtils.serialize(data);
        //enviar
        connection.sendUDP(event);

    }

    public void disconnected(Connection connection)
    {
        System.out.println("cliente desconectado");
    }

    public void received (Connection connection, Object object) {
        if (object instanceof Event) {
            Event request = (Event)object;
            System.out.println(request.name);


        }
    }

}
