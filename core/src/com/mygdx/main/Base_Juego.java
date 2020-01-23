package com.mygdx.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.entidad.Player;

public class Base_Juego extends Escena_juego {

    Box2DDebugRenderer debugRenderer;
    Matrix4 debugMatrix;


    @Override
    public void initialize() {
        world = new World(new Vector2(0, 0), true);

        player = new Player(300, 300, state, world);

        Gdx.input.setInputProcessor(this);

        debugRenderer = new Box2DDebugRenderer();

    }

    @Override
    public void update(float dt) {

        Vector3 vec3_1 =state.getCamera().unproject(new Vector3(Gdx.input.getX(),Gdx.input.getY(),0));


        float x= player.getX()+ player.getWidth() / 2;
        float y = player.getY() + player.getHeight() / 2;

        double radio  = Math.atan2(y- vec3_1.y ,x- vec3_1.x ) + Math.PI;

        player.setRotation((float)Math.toDegrees(radio));


        world.step(1f/60f, 6, 2);
    }

    @Override
    public boolean keyDown(int keycode) {
        player.keyDown(keycode);

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        player.keyUp(keycode);

        return false;
    }

    @Override
    public void box2d_debug()
    {
        state.getBatch().setProjectionMatrix(state.getCamera().combined);

        // Scale down the sprite batches projection matrix to box2D size
        debugMatrix = state.getBatch().getProjectionMatrix().cpy().scale(Constantes.PIXEL_IN_METERS,
                Constantes.PIXEL_IN_METERS, 0);

        state.getBatch().begin();

        state.getBatch().end();


        debugRenderer.render(world, debugMatrix);
    }

    @Override
    public boolean keyTyped(char character) {

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        player.touchDown(screenX,screenY,pointer,button);

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        player.touchUp(screenX,screenY,pointer,button);

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {

        return false;

    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }




}
