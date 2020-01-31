package com.mygdx.main.multiplayer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Server {

    DatagramSocket socket;


    public Server(String ip, int port) throws SocketException, UnknownHostException {
        socket = new DatagramSocket(port,InetAddress.getByName(ip));
    }

    public void update() throws IOException {


        byte[] receiveData = new byte[socket.getReceiveBufferSize()];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);








    }

    public void destroy()
    {
        socket.close();
    }
}
