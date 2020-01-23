package com.mygdx.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mygdx.entidad.Player;

public abstract class Escena_juego extends Game implements InputProcessor {
    Stage state;
    World world;
    Player player;
    TiledMap map;
    TiledMapRenderer tiledMapRenderer;
    OrthographicCamera orthocamera;

    float map_X, map_Y;



    @Override
    public void create() {

        orthocamera = new OrthographicCamera();
        orthocamera.setToOrtho(false,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        map = new TmxMapLoader().load("mapa.tmx");
        state = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), orthocamera));//
        tiledMapRenderer = new OrthogonalTiledMapRendererWithSprites(map,0.5f);


        initialize();

        MapProperties prop = map.getProperties();

        int mapWidth = prop.get("width", Integer.class);
        int mapHeight = prop.get("height", Integer.class);
        int tilePixelWidth = prop.get("tilewidth", Integer.class);
        int tilePixelHeight = prop.get("tileheight", Integer.class);

        int mapPixelWidth = mapWidth * tilePixelWidth;
        int mapPixelHeight = mapHeight * tilePixelHeight;

        map_X = mapPixelWidth*0.5f ;
        map_Y = mapPixelHeight*0.5f ;

        crear_limite(map_X,map_Y);
        crear_objetos();


    }

    public abstract void initialize();
    public abstract void box2d_debug();


    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();

        state.act();

        update(dt);

        float x = clamp(player.getX(), map_X -  Gdx.graphics.getWidth()/2, 0+ Gdx.graphics.getWidth()/2);
        float y = clamp(player.getY(), map_Y - Gdx.graphics.getHeight()/2, 0+ Gdx.graphics.getHeight()/2);

        state.getCamera().position.set(x, y, 0);

        //System.out.println(state.getCamera().position);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        tiledMapRenderer.setView(orthocamera);
        tiledMapRenderer.render();
        state.draw();


        box2d_debug();


    }

    public float clamp(float var, float max, float min) {
        if(var > min) {
            if(var < max) {
                return var;
            } else return max;
        } else return min;
    }


    public abstract void update(float dt);

    @Override
    public void resize(int width, int height) {
        state.getViewport().update(width,height);

    }

    @Override
    public void dispose()
    {
        world.dispose();
    }

    public void crear_objetos()
    {


        MapLayer layer = map.getLayers().get("Borrador");
        MapObjects objects = layer.getObjects();

        for (MapObject object: objects) {

            //System.out.println(object.getName());

            float x,y,w,h;

            x=(Float)(object.getProperties().get("x"));
            y=(Float)(object.getProperties().get("y"));
            w=(Float)(object.getProperties().get("width"));
            h=(Float)(object.getProperties().get("height"));

            crear_pared(x*0.5f,y*0.5f,w*0.5f,h*0.5f);

        }

        map.getLayers().remove(layer);


    }

    public void crear_pared(float x, float y,float w, float h)
    {

        float xh,yh,wh,hh;

        wh = (w/2)/Constantes.PIXEL_IN_METERS;
        hh = (h/2)/Constantes.PIXEL_IN_METERS;
        xh = (x/Constantes.PIXEL_IN_METERS)+wh;
        yh = (y/Constantes.PIXEL_IN_METERS)+hh;


        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(xh,yh);
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();

        shape.setAsBox(wh,hh);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData("Pared");

        shape.dispose();


    }

    public void crear_limite(float w, float h)
    {
        float wh,hh;

        wh = (w)/Constantes.PIXEL_IN_METERS;
        hh = (h)/Constantes.PIXEL_IN_METERS;


        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0,0);
        Body body = world.createBody(bodyDef);

        ChainShape shape = new ChainShape();

        shape.createLoop(new float[]{0,0,wh,0,wh,hh,0,hh});

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData("Limite");

        shape.dispose();
    }
}
