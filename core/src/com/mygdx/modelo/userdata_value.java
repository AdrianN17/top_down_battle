package com.mygdx.modelo;

public class userdata_value {
    private String tipo;
    private Object objeto;

    public userdata_value(String tipo, Object objeto) {
        this.tipo = tipo;
        this.objeto = objeto;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Object getObjeto() {
        return objeto;
    }

    public void setObjeto(Object objeto) {
        this.objeto = objeto;
    }
}
