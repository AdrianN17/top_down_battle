package com.mygdx.main.launcher;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Constantes_Server {
    public static final Array<Vector2> punto_inicio = new Array<Vector2>(){{
        add(new Vector2(193.939f , 1521.2122f));
        add(new Vector2(1427.27f , 1472.727f));
        add(new Vector2(127.273f , 109.089966f));
        add(new Vector2(1303.03f , 112.119995f));
    }};

    public static final float PIXEL_IN_METERS =100f;
    public static final float raycast_distancia = 1000;
}
