package com.mygdx.entidad;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.main.Constantes;

public class Player extends  Base_Actor{

    public Body body;
    private Fixture fixture;
    private CircleShape shape;
    private enum movimiento_horizontal{ninguno,a,d}
    private enum movimiento_vertical{ninguno,w,s}
    public movimiento_horizontal movh;
    public movimiento_vertical movv;

    public float vel = 200;


    public Player(float x, float y, Stage stage, World world) {
        super(x, y, stage);

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


        float radio = (float)Math.sqrt(getWidth()*getWidth() + getHeight()*getHeight())/2;
        shape = new CircleShape();
        shape.setRadius((radio/2)/Constantes.PIXEL_IN_METERS);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density=0.1f; // (weight: range 0.01 to 1 is good)
        fixtureDef.friction = 0.7f; // (how slippery it is: 0=like ice 1 = like rubber)
        fixtureDef.restitution = 0.3f;

        fixture = body.createFixture(fixtureDef);
        body.setLinearDamping(2);
        body.resetMassData();
        fixture.setDensity(0);
        body.setFixedRotation(true);


        movh = movimiento_horizontal.ninguno;
        movv = movimiento_vertical.ninguno;
    }

    @Override
    public void act(float dt) {
        super.act(dt);

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
            id_textura = 1;
            resize();
        }

        if(keycode == Input.Keys.W)
        {
            movv = movimiento_vertical.w;
            id_textura = 1;
            resize();
        }

        if(keycode == Input.Keys.S)
        {
            movv = movimiento_vertical.s;
            id_textura = 1;
            resize();
        }

        if(keycode == Input.Keys.NUM_1)
        {
            /*id_textura = 0;
            resize();*/
        }

        if(keycode == Input.Keys.NUM_2)
        {
            /*id_textura = 2;
            resize();*/
        }
    }

    public void keyUp(int keycode) {

            if(keycode == Input.Keys.A )
            {
                movh = movimiento_horizontal.ninguno;
                id_textura = 5;
                resize();
            }

            if(keycode == Input.Keys.D)
            {
                movh = movimiento_horizontal.ninguno;
                id_textura = 5;
                resize();
            }

            if(keycode == Input.Keys.W)
            {
                movv = movimiento_vertical.ninguno;
                id_textura = 5;
                resize();
            }

            if(keycode == Input.Keys.S)
            {
                movv = movimiento_vertical.ninguno;
                id_textura = 5;
                resize();
            }
    }


}
