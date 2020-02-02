package com.mygdx.main.launcher;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Constantes_Server {
    public static final ArrayList<Vector2> punto_inicio = new ArrayList<Vector2>(){{
        add(new Vector2(193.939f , 1521.2122f));
        add(new Vector2(1427.27f , 1472.727f));
        add(new Vector2(127.273f , 109.089966f));
        add(new Vector2(1303.03f , 112.119995f));
    }};

    public static final float PIXEL_IN_METERS =100f;
    public static final float raycast_distancia = 1000;
}
