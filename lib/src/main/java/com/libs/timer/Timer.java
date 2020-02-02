package com.libs.timer;

import com.badlogic.gdx.utils.Array;

public class Timer {

    public Array<Timer_Entidad> list_timer;



    public Timer()
    {
        list_timer = new Array<>();
    }

    public void Update(float dt)
    {
        for(Timer_Entidad te : list_timer)
        {
            te.counter= te.counter +dt;

            if(te.counter>te.limit_counter)
            {
                te.funcion.run();

                if(te.infinito)
                {
                    te.counter = 0;
                }
                else
                {
                    list_timer.removeValue(te,true);
                }

            }
        }
    }

    public void After(String name, float limit_counter, Runnable funcion)
    {
        list_timer.add(new Timer_Entidad(name,false,0,limit_counter,funcion));
    }

    public void Every(String name, float limit_counter, Runnable funcion)
    {
        list_timer.add(new Timer_Entidad(name,true,0,limit_counter,funcion));
    }

    public void remove(String name)
    {
        for(Timer_Entidad te : list_timer)
        {
            if(te.name.equals(name))
            {
                list_timer.removeValue(te,true);
                break;
            }
        }
    }

    public void Clear()
    {
        list_timer.clear();
    }
}
