package com.mygdx.multiplayer_modelos;

import java.io.Serializable;

public class data_inicial implements Serializable {
    public int id;
    public float x;
    public float y;

    public data_inicial(int id, float x, float y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }
}
