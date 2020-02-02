package com.libs.multiplayer.cliente;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ArrayMap;
import com.libs.modelos_principal.Event;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.libs.runnable.custom_runnable;



public class Kryonet_Listener_Client extends Listener {

    public ArrayMap<String,custom_runnable> events_list;

    public Kryonet_Listener_Client() {
        events_list = new ArrayMap();
    }

    public void connected(Connection connection) {
        Gdx.app.log("Cliente conectado" , " Num : " +  connection.getID());

        if(events_list.containsKey("Connect"))
        {
            Gdx.app.postRunnable(new custom_runnable() {
                public void run() {

                    custom_runnable cr = events_list.get("Connect");
                    cr.setConnection(connection);

                    cr.run();
                }
            });
        }
    }

    public void disconnected(Connection connection) {
        Gdx.app.log("Cliente desconectado" , " Num : " +  connection.getID());

        if(events_list.containsKey("Disconnect"))
        {
            Gdx.app.postRunnable(new custom_runnable() {
                public void run() {

                    custom_runnable cr = events_list.get("Disconnect");
                    cr.setConnection(connection);

                    cr.run();
                }
            });
        }
    }

    public void received(Connection connection, Object object) {
        if (object instanceof Event) {
            Event ev = (Event) object;
            Gdx.app.log("Servidor Evento" , " Name : " + ev.name);
            received_list(connection,ev);

        }
    }

    public void received_list(final Connection connection , final Event event) {

        final Object obj =  event.obj;


        if(events_list.containsKey(event.name))
        {
            Gdx.app.postRunnable(new Runnable() {
                public void run() {
                    custom_runnable cr = events_list.get(event.name);
                    cr.setConnection(connection);
                    cr.setObj(obj);

                    cr.run();
                }
            });
        }
    }
}