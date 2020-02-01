package com.mygdx.main.entidades;

import java.io.Serializable;

public class event implements Serializable {

    public String tipo;
    public Object obj;

    public event(String tipo, Object obj)
    {
        this.tipo = tipo;
        this.obj = obj;
    }
}
