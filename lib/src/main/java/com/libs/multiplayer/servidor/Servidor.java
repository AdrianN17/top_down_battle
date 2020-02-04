package com.libs.multiplayer.servidor;

import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryonet.Server;
import com.libs.modelos_principal.Event;
import com.libs.multiplayer.custom.envios;
import com.libs.runnable.custom_runnable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class Servidor {
    public Server server;
    public Kryonet_Listener_Server listener_list;
    public envios envio;



    public Servidor()
    {
        server = new Server();
        listener_list = new Kryonet_Listener_Server();
        envio = new envios(server);
        server.start();

        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            InetSocketAddress socket1 = new InetSocketAddress(inetAddress.getHostAddress(), 22122);
            InetSocketAddress socket2 = new InetSocketAddress(inetAddress.getHostAddress(), 22123);

            server.bind(socket1, socket2);

        } catch (IOException e) {
            e.printStackTrace();
        }

        server.getKryo().register(Event.class);
        server.getKryo().register(Object.class);
        server.getKryo().register(Object[].class);

        server.addListener(listener_list);
    }

    public void add_classes(Array<Class> myclass)
    {
        for(Class cla : myclass)
        {
            server.getKryo().register(cla);
        }
    }

    public void add_trigger(String name, custom_runnable function)
    {
        listener_list.events_list.put(name,function);
    }

    public void close()
    {
        server.close();
    }


}
