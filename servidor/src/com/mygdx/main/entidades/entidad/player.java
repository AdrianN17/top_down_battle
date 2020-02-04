package com.mygdx.main.entidades.entidad;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.libs.timer.Timer;
import com.mygdx.main.entidades.modelo.bala;
import com.mygdx.main.entidades.modelo.userdata_value;
import com.mygdx.main.launcher.Constantes_Server;

public class player {

    public float x;
    public float y;
    public int id;
    public World world;
    public Body body;
    public CircleShape shape;
    public Fixture fixture;
    public enum movimiento_horizontal{ninguno,a,d,todos_lados}
    public enum movimiento_vertical{ninguno,w,s,todos_lados}
    public movimiento_horizontal movh;
    public movimiento_vertical movv;
    public String tipo;
    public double radio_android;
    public Timer timer;
    protected boolean disparando = false;
    public int arma_index  = 0;
    protected float vel = 350;
    public float w = 33;
    public float h = 43;

    RayCastCallback callback;
    public float hp = 10;
    public Balas balas;
    public double radio;


    public player(float x, float y,final int id, World world) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.world = world;


        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((this.x +(this.w/2)) /
                        Constantes_Server.PIXEL_IN_METERS,
                (this.y + (this.h/2)) / Constantes_Server.PIXEL_IN_METERS);


        body = world.createBody(bodyDef);


        float radio = Constantes_Server.scale_player*(float)Math.sqrt(w*w+h*h)/2;
        shape = new CircleShape();
        shape.setRadius((radio/2)/Constantes_Server.PIXEL_IN_METERS);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density=0.1f;
        fixtureDef.friction = 0.7f;
        fixtureDef.restitution = 0.3f;

        fixture = body.createFixture(fixtureDef);
        body.setLinearDamping(2);
        body.resetMassData();
        fixture.setDensity(0);
        body.setFixedRotation(true);
        fixture.setUserData(new userdata_value("Player",this));


        movh = movimiento_horizontal.ninguno;
        movv = movimiento_vertical.ninguno;

        timer = new Timer();

        balas = new Balas();

        callback = new RayCastCallback(){
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {

                Object obj =  fixture.getUserData();

                if(obj !=null)
                {
                    userdata_value obj_user = (userdata_value)obj;

                    if(obj_user.getTipo().equals("Player") )
                    {
                        player obj_player = (player)obj_user.getObjeto();

                        if(obj_player.id!= id)
                        {
                            obj_player.hp = obj_player.hp - balas.balas.get(arma_index-1).dano;
                        }
                    }
                }


                return 1;
            }
        };

        this.radio=0;

        this.x =  (body.getPosition().x * Constantes_Server.PIXEL_IN_METERS) - this.w / 2;
        this.y =  (body.getPosition().y * Constantes_Server.PIXEL_IN_METERS) - this.h / 2;
    }

    public void update(float dt)
    {
        timer.Update(dt);


        Vector2 vec = new Vector2(0, 0);


        if (movh == movimiento_horizontal.a) {
            vec.x = -1;
        } else if (movh == movimiento_horizontal.d) {
            vec.x = 1;
        }

        if (movv == movimiento_vertical.w) {
            vec.y = 1;
        } else if (movv == movimiento_vertical.s) {
            vec.y = -1;
        }

        if(movh == movimiento_horizontal.todos_lados && movv == movimiento_vertical.todos_lados)
        {
            double r = Math.toRadians(radio_android);

            vec.x = (float)Math.cos(r);
            vec.y = (float) Math.sin(r);
        }


        float mx, my;
        mx = vec.x * body.getMass()* body.getMass() * dt* vel;
        my = vec.y * body.getMass()* body.getMass() * dt* vel;

        Vector2 vecv = body.getLinearVelocity();

        //System.out.println( dt + " , "+ mx + " , " + my + " , " + vecv.x/ Constantes.PIXEL_IN_METERS  + " ,  " + vec.y / Constantes.PIXEL_IN_METERS);

        //if (Math.abs(vecv.x) < max_vel *dt  && Math.abs(vecv.y) < max_vel  *dt) {
        //body.applyLinearImpulse(new Vector2(mx, my), body.getWorldCenter(), false);
        body.applyForceToCenter(mx,my,true);
        //}



        this.x =  (body.getPosition().x * Constantes_Server.PIXEL_IN_METERS) - this.w / 2;
        this.y =  (body.getPosition().y * Constantes_Server.PIXEL_IN_METERS) - this.h / 2;

    }

    public void disparar()
    {
        disparando = true;

        if(arma_index == 1)
        {
            balas.disminuir_bala(arma_index , new Runnable() {
                @Override
                public void run() {
                    hacer_raycast();
                }
            });
        }
        else if(arma_index == 2)
        {
            final bala bala_elegida = balas.balas.get(arma_index-1);

            timer.Every("balas", bala_elegida.velocidad, new Runnable() {
                @Override
                public void run() {
                balas.disminuir_bala(arma_index , new Runnable() {
                    @Override
                    public void run() {
                        hacer_raycast();
                    }
                });
                }
            });


        }

    }

    public void dejar_disparar()
    {
        if(disparando)
        {
            disparando = false;
            timer.remove("balas");
        }
    }

    public void cambiar_arma(int index)
    {
        arma_index = index;
    }

    public void hacer_raycast()
    {
        float x,y;
        double r = Math.toRadians(radio);

        x = this.x+w/ 2+(float)Math.cos(r)*Constantes_Server.raycast_distancia;
        y = this.y+h/ 2+(float)Math.sin(r)*Constantes_Server.raycast_distancia;

        world.rayCast(callback, new Vector2((x+w / 2)/Constantes_Server.PIXEL_IN_METERS,(y+h / 2)/Constantes_Server.PIXEL_IN_METERS), new Vector2(x,y));
    }

    public void setmovh(int id)
    {



        switch(id)
        {
            default: {
                movh = movimiento_horizontal.ninguno;
                break;
            }
            case 1: {
                movh = movimiento_horizontal.a;
                break;
            }
            case 2: {
                movh = movimiento_horizontal.d;
                break;
            }
            case 3: {
                movh = movimiento_horizontal.todos_lados;
                break;
            }
        }
    }

    public void setmovv(int id)
    {


        switch(id)
        {
            default: {
                movv = movimiento_vertical.ninguno;
                break;
            }
            case 1: {
                movv = movimiento_vertical.w;
                break;
            }
            case 2: {
                movv = movimiento_vertical.s;
                break;
            }
            case 3: {
                movv = movimiento_vertical.todos_lados;
                break;
            }
        }
    }

    public void recarga(){
        balas.recargar_bala(arma_index);
    }


}
