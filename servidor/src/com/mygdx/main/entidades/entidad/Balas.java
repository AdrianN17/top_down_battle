package com.mygdx.main.entidades.entidad;

import com.badlogic.gdx.utils.Array;
import com.mygdx.main.entidades.modelo.bala;

public class Balas {

    public Array<bala> balas;

    public Balas()
    {
        balas = new Array();

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
