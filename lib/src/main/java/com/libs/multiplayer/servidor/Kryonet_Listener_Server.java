package com.libs.multiplayer.servidor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.libs.modelos_principal.Event;
import com.libs.modelos_principal.Event_trigger;
import com.libs.runnable.custom_runnable;


public class Kryonet_Listener_Server extends Listener {

    public Array<Event_trigger> events_list;

    public Kryonet_Listener_Server() {
        events_list = new Array();
    }


    public void connected(final Connection connection)
    {
        /*System.out.println("cliente conectado " + connection.getID());

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
        connection.sendUDP(event);*/

    }

    public void disconnected(Connection connection)
    {
        System.out.println("cliente desconectado");
    }

    public void received (Connection connection, Object object) {
        if (object instanceof Event) {
            Event ev = (Event) object;
            System.out.println(ev.name);
            received_list(connection,ev);

        }
    }

    public void received_list(final Connection connection_1 , final Event event) {
        for (final Event_trigger et : events_list) {


            if (et.name.equals(event.name)) {
                Gdx.app.postRunnable(new custom_runnable() {
                    public void run() {
                        et.function.connection = connection_1;
                        et.function.obj = event.obj;
                        et.function.run();
                    }
                });
            }
        }
    }

}
