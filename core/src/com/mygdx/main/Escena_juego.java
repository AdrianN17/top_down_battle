package com.mygdx.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.libs.modelos.array_data_inicial;
import com.libs.modelos.data_inicial;
import com.libs.modelos_principal.Event;
import com.libs.multiplayer.cliente.Cliente;
import com.libs.runnable.custom_runnable;
import com.libs.timer.Timer;
import com.mygdx.entidad.Player;
import com.mygdx.main.entidades.entidad.player;
import com.mygdx.main.entidades.modelo.userdata_value;
import com.mygdx.main.launcher.Constantes_Server;

public abstract class Escena_juego extends Game implements InputProcessor {
    public Stage state;
    public World world;
    public Array<Player> list_player;
    TiledMap map;
    TiledMapRenderer tiledMapRenderer;
    OrthographicCamera orthocamera;

    protected ShapeRenderer shapeRenderer;

    float map_X, map_Y;

    protected long lastTimeCounted;
    protected float sinceChange;
    protected float frameRate;
    protected BitmapFont font;
    protected SpriteBatch batch;

    public int index_player = -1;

    public Cliente cliente;

    public Timer timer;


    @Override
    public void create() {
        timer = new Timer();
        cliente = new Cliente();

        listener();

        list_player = new Array<>();


        world = new World(new Vector2(0, 0), true);
        Gdx.input.setInputProcessor(this);



        orthocamera = new OrthographicCamera();
        orthocamera.setToOrtho(false,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        map = new TmxMapLoader().load("mapa.tmx");
        state = new Stage(new FitViewport(Constantes.width_G, Constantes.height_G, orthocamera));//
        tiledMapRenderer = new OrthogonalTiledMapRendererWithSprites(map,Constantes.scale);

        MapProperties prop = map.getProperties();

        int mapWidth = prop.get("width", Integer.class);
        int mapHeight = prop.get("height", Integer.class);
        int tilePixelWidth = prop.get("tilewidth", Integer.class);
        int tilePixelHeight = prop.get("tileheight", Integer.class);

        int mapPixelWidth = mapWidth * tilePixelWidth;
        int mapPixelHeight = mapHeight * tilePixelHeight;

        map_X = mapPixelWidth*Constantes.scale ;
        map_Y = mapPixelHeight*Constantes.scale ;

        crear_limite(map_X,map_Y);
        crear_objetos();

        shapeRenderer = new ShapeRenderer();

        sinceChange = 0;
        frameRate = Gdx.graphics.getFramesPerSecond();
        font = new BitmapFont();
        batch = new SpriteBatch();

        initialize();

        timer.Every("get_fps", 1, new Runnable() {
            @Override
            public void run() {
                frameRate = Gdx.graphics.getFramesPerSecond();
            }
        });


    }

    public abstract void initialize();
    public abstract void box2d_debug();


    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        state.act();

        timer.Update(dt);
        update(dt);

        if(index_player!= -1)
        {
            float x = clamp(list_player.get(index_player).getX(), map_X - Constantes.width_G/2, 0+ Constantes.width_G/2);
            float y = clamp(list_player.get(index_player).getY(), map_Y - Constantes.height_G/2, 0+ Constantes.height_G/2);

            state.getCamera().position.set(x,y, 0);
        }


        tiledMapRenderer.setView(orthocamera);
        tiledMapRenderer.render();
        state.draw();

        box2d_debug();


        //raycast
        if(index_player!= -1)
        {
            dibujar_raycast();
        }

        //dinujar_puntos();

        batch.begin();
        font.draw(batch, (int)frameRate + " fps", 10, Gdx.graphics.getHeight() - 10);
        batch.end();

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
        cliente.close();
        font.dispose();
        batch.dispose();

        world.dispose();

    }

    public void crear_objetos()
    {


        MapLayer layer = map.getLayers().get("Borrador");
        MapObjects objects = layer.getObjects();

        for (MapObject object: objects) {


            switch(object.getName())
            {
                case "Pared":
                {
                    float x, y, w, h;

                    x = (float) (object.getProperties().get("x"));
                    y = (float) (object.getProperties().get("y"));
                    w = (float) (object.getProperties().get("width"));
                    h = (float) (object.getProperties().get("height"));

                    crear_pared(x * Constantes.scale, y * Constantes.scale, w * Constantes.scale, h * Constantes.scale);

                    break;
                }
                case "Punto":
                {


                    /*float x = (float) (object.getProperties().get("x"));
                    float y = (float) (object.getProperties().get("y"));
                    System.out.println(x+" , "+y);*/
                   // puntos_nacimiento.add(new Vector2(x*Constantes.scale,y*Constantes.scale));
                    //System.out.println(x*Constantes.scale + " , " + y*Constantes.scale);
                    break;
                }
            }

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
        fixture.setUserData(new userdata_value("Pared",null));

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
        fixture.setUserData(new userdata_value("Limite",null));

        shape.dispose();
    }

    public void dibujar_raycast()
    {
        shapeRenderer.setProjectionMatrix(state.getBatch().getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        float cx,cy;
        double r = Math.toRadians(list_player.get(index_player).getRotation());


        cx = list_player.get(index_player).getX()+list_player.get(index_player).getWidth() / 2+(float)Math.cos(r)*Constantes.raycast_distancia;
        cy = list_player.get(index_player).getY()+list_player.get(index_player).getHeight() / 2+(float)Math.sin(r)*Constantes.raycast_distancia;


        shapeRenderer.line(new Vector2((list_player.get(index_player).getX()+list_player.get(index_player).getWidth() / 2),(list_player.get(index_player).getY()+list_player.get(index_player).getHeight() / 2)), new Vector2(cx,cy));
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.end();
    }

    public  void dinujar_puntos()
    {
        /*shapeRenderer.setProjectionMatrix(state.getBatch().getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (Vector2 vec: puntos_nacimiento) {
            shapeRenderer.circle(vec.x,vec.y,10);
            shapeRenderer.setColor(Color.RED);

        }
        shapeRenderer.end();*/
    }

    public void listener()
    {
        //class

        cliente.add_classes(new Array<Class>(){{
            add(Array.class);
            add(data_inicial.class);
            add(array_data_inicial.class);
        }});

        //trigger

        cliente.add_trigger("Inicializar", new custom_runnable(){
            @Override
            public void run()
            {

                if(this.obj instanceof array_data_inicial) {
                    array_data_inicial data = (array_data_inicial) this.obj;

                    index_player = data.id;

                    for(data_inicial di :data.array_data)
                    {
                        list_player.add(new Player(di.x, di.y, state, world, di.id));
                    }

                }
                else
                {
                    Gdx.app.log("trigger","Error al instanciar");
                }


            }} );
    }

}

