package com.libs.timer;

public class Timer_Entidad {
    public String name;
    public boolean infinito;
    public float counter;
    public float limit_counter;
    public Runnable funcion;

    public Timer_Entidad(String name, boolean infinito, float counter, float limit_counter, Runnable funcion) {
        this.name = name;
        this.infinito = infinito;
        this.counter = counter;
        this.limit_counter = limit_counter;
        this.funcion = funcion;
    }
}
