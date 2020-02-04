package com.libs.multiplayer.cliente;

import com.badlogic.gdx.utils.Array;
import com.libs.modelos_principal.Event;
import com.esotericsoftware.kryonet.Client;
import com.libs.multiplayer.custom.envios;
import com.libs.runnable.custom_runnable;

import java.io.IOException;

public class Cliente {
    protected Client client;
    public Kryonet_Listener_Client listener_list;
    public envios envio;

    public Cliente()
    {

        client =  new Client();
        client.start();
        envio = new envios(client);
        try {
            client.connect(5000, "192.168.0.3", 22122, 22123);
        } catch (IOException e) {
            e.printStackTrace();
        }

        listener_list = new Kryonet_Listener_Client();



        client.getKryo().register(Event.class);
        client.getKryo().register(Object.class);
        client.getKryo().register(Object[].class);

        client.addListener(listener_list);
    }

    public void add_classes(Array<Class> myclass)
    {
        for(Class cla : myclass)
        {
            client.getKryo().register(cla);
        }
    }


    public void add_trigger(String name, custom_runnable function)
    {
        listener_list.events_list.put(name,function);
    }

    public int getMS()
    {
        return client.getReturnTripTime();
    }

    public String IsConnected()
    {
        return Boolean.toString(client.isConnected());
    }


    public void close()
    {
        client.close();
    }



}
