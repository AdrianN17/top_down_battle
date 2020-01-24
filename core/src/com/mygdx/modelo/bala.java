package com.mygdx.modelo;

public class bala {
    public int stock;
    public int max_stock;
    public int municion;
    public int max_municion;
    public float dano;
    public float velocidad;
    public boolean raycast;


    public bala(int stock, int max_stock, int municion, int max_municion, float dano, float velocidad, boolean raycast) {
        this.stock = stock;
        this.max_stock = max_stock;
        this.municion = municion;
        this.max_municion = max_municion;
        this.dano = dano;
        this.velocidad = velocidad;
        this.raycast=raycast;

    }
}
