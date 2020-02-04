package com.libs.multiplayer.custom;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.libs.modelos_principal.Event;

import java.util.Collection;

public class envios {

    Server server;
    Client client;


    public envios(Client client)
    {
        this.client = client;
    }

    public envios(Server server)
    {
        this.server = server;
    }

    public void Send(String name, Object o, Connection co)
    {
        Event event = new Event();
        event.name = name;
        event.obj = o;
        co.sendTCP(event);
    }

    public void SendClient(String name, Object o)
    {
        if(client!=null)
        {
            Event event = new Event();
            event.name = name;
            event.obj = o;
            client.sendTCP(event);
        }
    }

    public void sendToAll(String name, Object o)
    {
        Event event = new Event();
        event.name = name;
        event.obj = o;


        for(Connection co : getConnections())
        {
            co.sendTCP(event);
        }
    }

    public void sendToAllBut(String name, Object o,Connection co_exclude)
    {
        Event event = new Event();
        event.name = name;
        event.obj = o;


        for(Connection co : getConnections())
        {
            if(co!= co_exclude)
            {
                co.sendTCP(event);
            }
        }
    }

    public Connection[] getConnections()
    {
        if(server!=null)
        {
            return server.getConnections();
        }
        else
        {
            return server.getConnections();
        }
    }
}
