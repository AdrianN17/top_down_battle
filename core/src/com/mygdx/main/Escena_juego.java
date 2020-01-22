package com.mygdx.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.entidad.Player;

public abstract class Escena_juego extends Game implements InputProcessor {
    Stage state;
    World world;
    Player player;

    @Override
    public void create() {
        state = new Stage(new FitViewport(600, 600));
        initialize();
    }

    public abstract void initialize();
    public abstract void box2d_debug();


    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();

        // metodo act
        state.act();

        // pasa el tiempo
        update(dt);

        // limpiar pantalla
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // dibujar las graficas
        state.draw();

        box2d_debug();


    }

    public abstract void update(float dt);

    @Override
    public void dispose()
    {
        world.dispose();
    }
}
