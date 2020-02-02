package com.mygdx.main.entidades.entidad;

import com.badlogic.gdx.physics.box2d.World;

public class player {

    public float x;
    public float y;
    public int id;
    public World world;

    public player(float x, float y, int id, World world) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.world = world;
    }
}
