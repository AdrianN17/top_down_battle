package com.libs.multiplayer.cliente;

import com.libs.modelos_principal.Event;
import com.esotericsoftware.kryonet.Client;
import com.libs.runnable.custom_runnable;

import java.io.IOException;

public class Cliente {
    protected Client client;
    public Kryonet_Listener_Client listener_list;

    public Cliente()
    {

        client =  new Client();
        client.start();
        try {
            client.connect(5000, "192.168.0.3", 22122, 22123);
        } catch (IOException e) {
            e.printStackTrace();
        }

        listener_list = new Kryonet_Listener_Client();

        client.addListener(listener_list);

        client.getKryo().register(Event.class);
        client.getKryo().register(byte[].class);
    }

    public void add_trigger(String name, custom_runnable function)
    {
        listener_list.events_list.put(name,function);
    }

    public void close()
    {
        client.close();
    }



}
