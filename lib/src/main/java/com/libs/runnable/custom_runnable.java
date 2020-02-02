package com.libs.runnable;

import com.esotericsoftware.kryonet.Connection;

public class custom_runnable implements Runnable {

    public Connection connection;
    public Object obj;

    public custom_runnable() {

    }

    public void run() {

    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}