package com.mygdx.entidad;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.main.Constantes;
import com.mygdx.modelo.bala;
import com.mygdx.modelo.userdata_value;

public class Player extends  Base_Actor{



    protected movimiento_horizontal movh;
    protected movimiento_vertical movv;

    protected float vel = 200;

    protected boolean disparando = false;
    protected short arma_index=0;
    protected float counter=0;

    public short id;
    public Balas balas;

    RayCastCallback callback;

    public Player(float x, float y, Stage stage, World world) {
        super(x, y, stage);

        this.world=world;

        loadAnimationFromFiles("robot_1" ,new String[]{"robot1_gun","robot1_hold","robot1_machine",
                "robot1_reload","robot1_silencer","robot1_stand"});

        setPosition(x,y);

        //box2d
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(Constantes.scale_player*(getX() + getWidth()/2) /
                        Constantes.PIXEL_IN_METERS,
                Constantes.scale_player*(getY() + (getHeight()/2)) / Constantes.PIXEL_IN_METERS);


        body = world.createBody(bodyDef);


        float radio = Constantes.scale_player*(float)Math.sqrt(getWidth()*getWidth() + getHeight()*getHeight())/2;
        shape = new CircleShape();
        shape.setRadius((radio/2)/Constantes.PIXEL_IN_METERS);

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


        id=0;

        //balas

        balas = new Balas();

        //raycast

        callback = new RayCastCallback(){
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {

                Object obj =  fixture.getUserData();

                if(obj !=null)
                {
                    userdata_value obj_user = (userdata_value)obj;

                    if(obj_user.getTipo().equals("Player") )
                    {
                        Player obj_player = (Player)obj_user.getObjeto();

                        if(obj_player.id!= id)
                        {
                            System.out.println("aaa");
                        }
                    }
                }


                return 1;
            }
        };



    }

    @Override
    public void act(float dt) {
        super.act(dt);

        if(disparando)
        {
            counter = counter+dt;
        }

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

        if(!disparando) {
            if (vec.x == 0 && vec.y == 0)
            {
                id_textura = 5;
                resize();
            }
            else
            {
                id_textura = 1;
                resize();
            }
        }
        else
        {
            if(arma_index-1>=1)
            {
                final  bala bala_elegida = balas.balas.get(arma_index-1);

                if(counter>bala_elegida.velocidad)
                {
                    balas.disminuir_bala(2, new Runnable() {
                        @Override
                        public void run() {
                            System.out.println(bala_elegida.stock);
                            hacer_raycast();
                        }
                    });

                    counter=0;
                }
            }
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




        this.setPosition((body.getPosition().x * Constantes.PIXEL_IN_METERS) - getWidth() / 2, (body.getPosition().y * Constantes.PIXEL_IN_METERS) - getHeight() / 2);
    }



    public void keyDown(int keycode) {
        if(keycode == Input.Keys.A)
        {
            movh = movimiento_horizontal.a;
            id_textura = 1;
            resize();
        }

        if(keycode == Input.Keys.D)
        {
            movh = movimiento_horizontal.d;
        }

        if(keycode == Input.Keys.W)
        {
            movv = movimiento_vertical.w;
        }

        if(keycode == Input.Keys.S)
        {
            movv = movimiento_vertical.s;
        }

        if(keycode == Input.Keys.NUM_1)
        {
            arma_index = 1;
        }

        if(keycode == Input.Keys.NUM_2)
        {
            arma_index = 2;
        }
    }

    public void keyUp(int keycode) {

            if(keycode == Input.Keys.A )
            {
                movh = movimiento_horizontal.ninguno;
            }

            if(keycode == Input.Keys.D)
            {
                movh = movimiento_horizontal.ninguno;
            }

            if(keycode == Input.Keys.W)
            {
                movv = movimiento_vertical.ninguno;
            }

            if(keycode == Input.Keys.S)
            {
                movv = movimiento_vertical.ninguno;
            }
    }

    public void touchDown(int screenX, int screenY, int pointer, int button)
    {
        if(button == Input.Buttons.LEFT)
        {
            switch(arma_index)
            {
                case 1: {
                    id_textura = 0;
                    resize();

                    balas.disminuir_bala(1, new Runnable() {
                        @Override
                        public void run() {
                            hacer_raycast();
                        }
                    });

                    disparando=true;
                    break;
                }
                case 2: {
                    id_textura = 2;
                    resize();
                    disparando=true;
                    break;
                }
            }
        }
        else if(button == Input.Buttons.RIGHT)
        {
            switch(arma_index) {
                case 1:
                {
                    balas.recargar_bala(arma_index );
                    break;
                }
                case 2:
                {
                    balas.recargar_bala(arma_index);
                    break;
                }
            }
        }

    }

    public void touchUp(int screenX, int screenY, int pointer, int button)
    {
        if(button == Input.Buttons.LEFT)
        {
            switch(arma_index)
            {
                case 1: {
                    disparando=false;
                    counter = 0;
                    break;
                }
                case 2: {
                    disparando=false;
                    counter = 0;
                    break;
                }
            }
        }
    }


    public void hacer_raycast()
    {
        float x,y;
        double r = Math.toRadians(getRotation());


        x = getX()+getWidth() / 2+(float)Math.cos(r)*Constantes.raycast_distancia;
        y = getY()+getHeight() / 2+(float)Math.sin(r)*Constantes.raycast_distancia;


        world.rayCast(callback, new Vector2((getX()+getWidth() / 2)/Constantes.PIXEL_IN_METERS,(getY()+getHeight() / 2)/Constantes.PIXEL_IN_METERS), new Vector2(x,y));
    }


}
