package com.libs.modelos;

import java.io.Serializable;

public class data_cada_tiempo implements Serializable {

    public int id;
    public float x;
    public float y;
    public int hp;
    public float angulo;
    public int movh;
    public int movv;

    public data_cada_tiempo(int id, float x, float y, int hp, float angulo, int movh, int movv) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.hp = hp;
        this.angulo = angulo;
        this.movh = movh;
        this.movv = movv;
    }
}
