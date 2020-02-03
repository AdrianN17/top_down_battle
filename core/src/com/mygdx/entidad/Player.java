package com.mygdx.entidad;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.main.Constantes;
import com.mygdx.main.entidades.entidad.Balas;
import com.mygdx.main.entidades.modelo.bala;
import com.mygdx.main.entidades.modelo.userdata_value;

public class Player extends  Base_Actor{



    public movimiento_horizontal movh;
    public movimiento_vertical movv;

    protected float vel = 350;

    public boolean disparando = false;
    public int arma_index=0;

    public int id;
    public Balas balas;

    RayCastCallback callback;

    public double radio_android=0;
    protected Vector2 punto_inicio= new Vector2();
    protected Vector2 punto_inicio_2= new Vector2();;

    public Player(float x, float y, Stage stage, World world,final int id) {
        super(x, y, stage);

        this.id = id;

        this.world=world;

        loadAnimationFromFiles("robot_1" ,new String[]{"robot1_gun","robot1_hold","robot1_machine",
                "robot1_reload","robot1_silencer","robot1_stand"});

        setPosition(x,y);

        //box2d
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((getX() + getWidth()/2) /
                        Constantes.PIXEL_IN_METERS,
                (getY() + (getHeight()/2)) / Constantes.PIXEL_IN_METERS);


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


        //balas

        balas = new Balas();

    }

    @Override
    public void act(float dt) {
        super.act(dt);


        Vector2 vec = new Vector2(0, 0);

        if(Gdx.app.getType() == Application.ApplicationType.Desktop) {
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
        }
        else if(Gdx.app.getType() == Application.ApplicationType.Android)
        {
            if(movh == movimiento_horizontal.todos_lados && movv == movimiento_vertical.todos_lados)
            {
                double r = Math.toRadians(radio_android);

                vec.x = (float)Math.cos(r);
                vec.y = (float) Math.sin(r);
            }
        }

        if(!disparando) {
            if (movimiento_horizontal.ninguno == movh  && movimiento_vertical.ninguno == movv)
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


    public void presionar_pc(int button)
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

    public void soltar_pc(int button)
    {
        if(button == Input.Buttons.LEFT)
        {
            switch(arma_index)
            {
                case 1: {
                    disparando=false;
                    break;
                }
                case 2: {
                    disparando=false;
                    break;
                }
            }
        }
    }

    public void presionar_android()
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

    public void soltar_android(int pointer)
    {
        switch(arma_index)
        {
            case 1: {
                disparando=false;
                //counter = 0;
                break;
            }
            case 2: {
                disparando=false;
                //counter = 0;
                break;
            }
        }
    }


    public void hacer_raycast()
    {
        float x,y;
        double r = Math.toRadians(getRotation());


        x = getX()+(getWidth()*Constantes.scale) / 2+(float)Math.cos(r)*Constantes.raycast_distancia;
        y = getY()+(getHeight()*Constantes.scale) / 2+(float)Math.sin(r)*Constantes.raycast_distancia;

        world.rayCast(callback, new Vector2((getX()+getWidth() / 2)/Constantes.PIXEL_IN_METERS,(getY()+getHeight() / 2)/Constantes.PIXEL_IN_METERS), new Vector2(x,y));
    }

    public void get_angulo(Camera camera,int x,int y)
    {
        Vector3 vec3_1 =camera.unproject(new Vector3(x,y,0));


        float cx= getX()+ getWidth() / 2;
        float cy = getY() + getHeight() / 2;

        double radio  = Math.atan2(cy- vec3_1.y ,cx- vec3_1.x ) + Math.PI;

        setRotation((float)Math.toDegrees(radio));
    }

    public void get_angulo_android(Camera camera,int x,int y)
    {
        Vector3 vec3_1 =camera.unproject(new Vector3(x,y,0));
        Vector3 vec3_2 =camera.unproject(new Vector3(punto_inicio.x,punto_inicio.y,0));

        double radio  = Math.atan2(vec3_2.y- vec3_1.y ,vec3_2.x-vec3_1.x ) + Math.PI;


        this.radio_android =  (float)Math.toDegrees(radio);
    }

    public void get_angulo_android_2(Camera camera,int x,int y)
    {
        Vector3 vec3_1 =camera.unproject(new Vector3(x,y,0));
        Vector3 vec3_2 =camera.unproject(new Vector3(punto_inicio_2.x,punto_inicio_2.y,0));

        double radio  = Math.atan2(vec3_2.y- vec3_1.y ,vec3_2.x-vec3_1.x ) + Math.PI;

        setRotation((float)Math.toDegrees(radio));
    }

    public void presionar_mover_android()
    {
        movh = movimiento_horizontal.todos_lados;
        movv = movimiento_vertical.todos_lados;
    }

    public void soltar_mover_android()
    {
        movh = movimiento_horizontal.ninguno;
        movv = movimiento_vertical.ninguno;
    }

    public void set_inicial_vector(int x,int y)
    {
        punto_inicio.set(x,y);
    }

    public void set_inicial_vector_2(int x, int y)
    {
        punto_inicio_2.set(x,y);
    }

    public int get_enum_h()
    {
        switch(movh)
        {
            default:
            {
                return 0;
            }
            case a:
            {
                return 1;
            }
            case d:
            {
                return 2;
            }
            case todos_lados:
            {
                return 3;
            }
        }
    }

    public int get_enum_v()
    {
        switch(movv)
        {
            default:
            {
                return 0;
            }
            case w:
            {
                return 1;
            }
            case s:
            {
                return 2;
            }
            case todos_lados:
            {
                return 3;
            }
        }
    }

    public void setPosicion(float x, float y)
    {
        float ox;
        float oy;

        ox = (x/Constantes.scale)/Constantes.PIXEL_IN_METERS;
        oy = (y/Constantes.scale)/Constantes.PIXEL_IN_METERS;

        body.setTransform(ox,oy,0);
    }


}
