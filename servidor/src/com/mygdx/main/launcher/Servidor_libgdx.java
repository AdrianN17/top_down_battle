package com.mygdx.main.launcher;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.libs.modelos.array_data_inicial;
import com.libs.modelos.data_cada_tiempo;
import com.libs.modelos.data_cada_tiempo_cliente;
import com.libs.modelos.data_disparo;
import com.libs.modelos.data_inicial;
import com.libs.multiplayer.servidor.Servidor;
import com.libs.runnable.custom_runnable;
import com.libs.timer.Timer;
import com.mygdx.main.entidades.entidad.player;
import com.mygdx.main.entidades.modelo.userdata_value;


public class Servidor_libgdx extends Game {

    public Servidor servidor;
    public World world;

    public Array<player> list_player;
    ShapeRenderer renderer;

    TiledMap map;
    int mapPixelWidth;
    int mapPixelHeight;

    public Array<Vector2> punto_inicio;
    public Timer timer;
    int id_ini=0;
    protected BitmapFont font;
    protected SpriteBatch batch;



    @Override
    public void create() {
        punto_inicio = new Array<Vector2>();

        servidor = new Servidor();

        listener();

        list_player = new Array<>();

        world = new World(new Vector2(0, 0), true);

        renderer = new ShapeRenderer();

        map = new TmxMapLoader().load("mapa.tmx");

        MapProperties prop = map.getProperties();

        int mapWidth = prop.get("width", Integer.class);
        int mapHeight = prop.get("height", Integer.class);
        int tilePixelWidth = prop.get("tilewidth", Integer.class);
        int tilePixelHeight = prop.get("tileheight", Integer.class);

        mapPixelWidth = mapWidth * tilePixelWidth;
        mapPixelHeight = mapHeight * tilePixelHeight;

        crear_limite(mapPixelWidth,mapPixelHeight);
        crear_objetos();

        timer = new Timer();

        timer.Every("Enviar_Posiciones_Todos", 1, new Runnable() {
            @Override
            public void run() {

            Array<data_cada_tiempo_cliente> al_dctc = new Array<>();

            for(player pl : list_player)
            {
                data_cada_tiempo_cliente dctc = new data_cada_tiempo_cliente();
                dctc.hp = pl.hp;

                dctc.x = pl.body.getPosition().x;
                dctc.y = pl.body.getPosition().y;

                dctc.id = pl.id;

                dctc.stock_1 = pl.balas.balas.get(0).stock;
                dctc.municion_1 = pl.balas.balas.get(0).municion;
                dctc.stock_2 = pl.balas.balas.get(1).stock;
                dctc.municion_2 = pl.balas.balas.get(1).municion;

                al_dctc.add(dctc);
            }

                servidor.envio.sendToAll("Enviar_Posiciones_Todos",al_dctc);
            }
        });

        timer.Every("Check_HP", 0.5f, new Runnable() {
            @Override
            public void run() {
                for(player pl: list_player)
                {
                    if(pl.hp<0.1)
                    {
                        servidor.envio.sendToAll("eliminar_player_en_cliente_muerto",pl.id);
                        list_player.removeValue(pl,true);
                    }
                }
            }
        });


        font = new BitmapFont();
        batch = new SpriteBatch();
    }

    @Override
    public void render()
    {
        float dt = Gdx.graphics.getDeltaTime();
        world.step(1f/60f, 6, 2);
        timer.Update(dt);

        for(player pl : list_player)
        {
            pl.update(dt);
        }


        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.CYAN);

        batch.begin();
        for(player pl : list_player)
        {
            //renderer.circle(pl.x, pl.y, 20);
            font.draw(batch,"Arma : " + pl.arma_index, pl.x-(0.5f*pl.w), pl.y+(pl.h));
        }

        renderer.end();
        batch.end();

    }

    @Override
    public void dispose()
    {
        servidor.close();
    }

    public void listener()
    {
        //class

        servidor.add_classes(new Array<Class>(){{
            add(Array.class);

            add(data_inicial.class);
            add(array_data_inicial.class);
            add(data_cada_tiempo.class);
            add(data_cada_tiempo_cliente.class);
            add(data_disparo.class);
        }});

        //trigger

        servidor.add_trigger("Connect", new custom_runnable(){
            @Override
            public void run()
            {
                //para el servidor
                int id = id_ini;
                id_ini++;

                Vector2 vec = punto_inicio.get(id);

                list_player.add(new player(vec.x,vec.y, id,world));

                Array<data_inicial> lista_inicial= new Array();

                for(player pl : list_player)
                {
                    Vector2 posicion = punto_inicio.get(pl.id);

                    data_inicial da = new data_inicial();
                    da.id = pl.id;
                    da.x = posicion.x;
                    da.y = posicion.y;

                    lista_inicial.add(da);
                }
                array_data_inicial al_da = new array_data_inicial();
                al_da.array_data = lista_inicial;
                al_da.id = id;

                servidor.envio.Send("Inicializar",al_da,this.connection);

                //enviar a todos excepto el

                servidor.envio.sendToAllBut("Inicializar_nuevo",list_player.get(id),this.connection);

            }} );

        servidor.add_trigger("Posiciones", new custom_runnable(){
            @Override
            public void run()
            {
                if(this.obj instanceof data_cada_tiempo) {
                    data_cada_tiempo data = (data_cada_tiempo) this.obj;

                    int id = data.id;

                    player pl = list_player.get(id);
                    pl.radio = data.angulo;
                    pl.radio_android = data.android_angulo;
                    pl.setmovh(data.movh);
                    pl.setmovv(data.movv);
                }
                else
                {
                    Gdx.app.log("trigger","Error al instanciar");
                }
            }
        } );

        servidor.add_trigger("eliminar_player", new custom_runnable()
        {
            @Override
            public void run() {
                if (this.obj instanceof Integer) {
                    int data = (Integer) this.obj;

                    for (player pl:list_player) {
                        if(pl.id == data)
                        {
                            list_player.removeValue(pl,true);

                            servidor.envio.sendToAll("eliminar_player_en_cliente",data);


                            break;
                        }
                    }
                } else {
                    Gdx.app.log("trigger", "Error al instanciar");
                }
            }
        });

        servidor.add_trigger("disparar",new custom_runnable()
        {
            @Override
            public void run()
            {
                if (this.obj instanceof data_disparo) {
                    data_disparo data = (data_disparo) this.obj;

                    for (player pl:list_player) {
                        if (pl.id == data.id) {

                            if(!data.disparando)
                            {
                                pl.arma_index = data.bala;
                                pl.disparar();
                            }
                            else
                            {
                                pl.dejar_disparar();
                            }

                            break;

                        }
                    }
                }
                else
                {
                    Gdx.app.log("trigger", "Error al instanciar");
                }
            }
        });

        servidor.add_trigger("recargar",new custom_runnable()
        {
            @Override
            public void run()
            {
                if (this.obj instanceof data_disparo)
                {
                    final data_disparo data = (data_disparo) this.obj;

                    for (player pl:list_player) {
                        if (pl.id == data.id) {
                            pl.arma_index = data.bala;
                            pl.recarga();
                            break;
                        }
                    }
                }
                else
                {
                    Gdx.app.log("trigger", "Error al instanciar");
                }
            }
        });
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

                    crear_pared(x , y , w , h);

                    break;
                }
                case "Punto":
                {


                    float x = (float) (object.getProperties().get("x"));
                    float y = (float) (object.getProperties().get("y"));

                    punto_inicio.add(new Vector2(x,y));

                    break;
                }
            }

        }

        map.getLayers().remove(layer);


    }

    public void crear_limite(float w, float h)
    {
        float wh,hh;

        wh = (w)/Constantes_Server.PIXEL_IN_METERS;
        hh = (h)/Constantes_Server.PIXEL_IN_METERS;


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

    public void crear_pared(float x, float y,float w, float h)
    {

        float xh,yh,wh,hh;

        wh = (w/2)/Constantes_Server.PIXEL_IN_METERS;
        hh = (h/2)/Constantes_Server.PIXEL_IN_METERS;
        xh = (x/Constantes_Server.PIXEL_IN_METERS)+wh;
        yh = (y/Constantes_Server.PIXEL_IN_METERS)+hh;


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
}

