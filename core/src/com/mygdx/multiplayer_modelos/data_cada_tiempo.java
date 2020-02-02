package com.mygdx.multiplayer_modelos;

import java.io.Serializable;

public class data_cada_tiempo implements Serializable {

    public int id;
    public float x;
    public float y;
    public int hp;

    public int stock_1;
    public int stock_2;
    public int municion_1;
    public int municion_2;

    public float angulo;


    public data_cada_tiempo(int id, float x, float y, int hp, int stock_1, int stock_2, int municion_1, int municion_2, float angulo) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.hp = hp;
        this.stock_1 = stock_1;
        this.stock_2 = stock_2;
        this.municion_1 = municion_1;
        this.municion_2 = municion_2;
        this.angulo = angulo;
    }
}
