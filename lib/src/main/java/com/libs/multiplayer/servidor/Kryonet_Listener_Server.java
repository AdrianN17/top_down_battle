package com.libs.multiplayer.servidor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ArrayMap;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.libs.modelos_principal.Event;
import com.libs.runnable.custom_runnable;



public class Kryonet_Listener_Server extends Listener {

    public ArrayMap<String,custom_runnable> events_list;

    public Kryonet_Listener_Server() {
        events_list = new ArrayMap();
    }


    public void connected(final Connection connection)
    {
        Gdx.app.log("Cliente conectado al server" , " Num : " +  connection.getID());
        if(events_list.containsKey("Connect"))
        {
            Gdx.app.postRunnable(new Runnable() {
                public void run() {

                    custom_runnable cr = events_list.get("Connect");
                    cr.setConnection(connection);

                    cr.run();
                }
            });
        }
    }

    public void disconnected(final Connection connection)
    {
        Gdx.app.log("Cliente desconectado del server" , " Num : " +  connection.getID());

        if(events_list.containsKey("Disconnect"))
        {
            Gdx.app.postRunnable(new Runnable() {
                public void run() {

                    custom_runnable cr = events_list.get("Connect");
                    cr.setConnection(connection);

                    cr.run();
                }
            });
        }
    }

    public void received (Connection connection, Object object) {
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