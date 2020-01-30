package com.mygdx.multiplayer;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Server {

    DatagramSocket socket;


    public Server(String ip, int port) throws SocketException, UnknownHostException {
        socket = new DatagramSocket(port,InetAddress.getByName(ip));
    }

    public void update()
    {

    }

    public void destroy()
    {

    }
}
