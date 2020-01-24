package com.mygdx.entidad;

import com.mygdx.modelo.bala;

import java.util.ArrayList;

public class Balas {

    public ArrayList<bala> balas;

    public Balas()
    {
        balas = new ArrayList();

        balas.add(new bala( 7,7,49,49,1,0,true));
        balas.add(new bala( 30,30,120,120,1,0.5f,true));
    }

    public void disminuir_bala(int index,Runnable funcion)
    {
        bala bala_elegida = balas.get(index-1);


        if(bala_elegida.stock>=1)
        {
            funcion.run();
        }

        bala_elegida.stock --;

    }

    public void recargar_bala(int index)
    {
        bala bala_elegida = balas.get(index-1);

        if(bala_elegida.max_stock>bala_elegida.stock && bala_elegida.municion>0)
        {
            if(bala_elegida.municion + bala_elegida.stock < bala_elegida.max_stock)
            {
                bala_elegida.stock = bala_elegida.municion + bala_elegida.stock;
                bala_elegida.municion = 0;
            }
            else
            {
                int carga = bala_elegida.max_stock-bala_elegida.stock;
                bala_elegida.stock = bala_elegida.stock + carga;
                bala_elegida.municion = bala_elegida.municion - carga;

            }
        }
    }
}
