package com.mygdx.multiplayer_cliente;

import com.esotericsoftware.kryonet.Client;
import com.mygdx.main.Base_Juego;
import com.mygdx.main.Escena_juego;

import java.io.IOException;

public class Cliente  {
    protected Client client;
    public Escena_juego base;

    public Cliente(Escena_juego base)
    {
        this.base =  base;

        client =  new Client();
        client.start();
        try {
            client.connect(5000, "192.168.0.3", 22122, 22123);
        } catch (IOException e) {
            e.printStackTrace();
        }

        client.addListener(new Kryonet_Listener_Cliente(base));

        client.getKryo().register(Event.class);
        client.getKryo().register(byte[].class);
    }

    public void enviar_mensaje()
    {
        Event request = new Event();
        request.name = "Cliente saluda";
        client.sendUDP(request);
    }

    public void close()
    {
        client.close();
    }


}
