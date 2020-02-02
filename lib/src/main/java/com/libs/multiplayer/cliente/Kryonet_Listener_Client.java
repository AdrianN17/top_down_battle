package com.libs.multiplayer.cliente;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.libs.modelos_principal.Event;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.libs.modelos_principal.Event_trigger;
import com.libs.runnable.custom_runnable;

import org.apache.commons.lang3.SerializationUtils;

public class Kryonet_Listener_Client extends Listener {

    public Array<Event_trigger> events_list;

    public Kryonet_Listener_Client() {
        events_list = new Array();
    }

    public void connected(Connection connection) {
        System.out.println("cliente conectado al servidor" + connection.getID());
    }

    public void disconnected(Connection connection) {
        System.out.println("cliente desconectado al servidor" + connection.getID());
    }


    public void received(Connection connection, Object object) {
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
            /*case "Inicializar":
            {
                final data_inicial obj = SerializationUtils.deserialize(event.obj);

                base.index_player = obj.id-1;

                Gdx.app.postRunnable(new Runnable() {
                    public void run() {
                        base.list_player.add(new Player(obj.x,obj.y,base.state,base.world, obj.id-1));
                    }});
            }*/

