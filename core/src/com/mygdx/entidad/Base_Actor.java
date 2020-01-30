package com.mygdx.entidad;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.mygdx.main.Constantes;

public class Base_Actor extends Actor {
    protected Array<TextureRegion> texturas;
    protected float hp = 10;
    protected World world;
    protected Body body;
    protected Fixture fixture;
    protected CircleShape shape;
    protected enum movimiento_horizontal{ninguno,a,d,todos_lados}
    protected enum movimiento_vertical{ninguno,w,s,todos_lados}
    protected BitmapFont font;

    public int id_textura = 1;

    public Base_Actor(float x, float y, Stage stage)
    {
        super();
        font = new BitmapFont();

        stage.addActor(this);

    }

    public void loadAnimationFromFiles(String folder,String[] fileNames) {
        int fileCount = fileNames.length;
        Array<TextureRegion> textureArray = new Array<TextureRegion>();
        for (int n = 0; n < fileCount; n++) {
            String fileName = folder + "/" + fileNames[n] + ".png";
            Texture texture = new Texture(Gdx.files.internal(fileName));
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            textureArray.add(new TextureRegion(texture));
        }

        this.texturas = textureArray;

        resize();



    }

    public void resize()
    {
        float width = texturas.get(id_textura).getRegionWidth();
        float height = texturas.get(id_textura).getRegionHeight();
        setSize(width, height);
        setScale(Constantes.scale_player);

        setOrigin(width / 2, height / 2);
        setOrigin(width / 2, height / 2);
    }

    public void act(float dt) {
        super.act(dt);
    }

    public void draw(Batch batch, float parentAlpha)
    {
        super.draw(batch, parentAlpha);
        Color color = getColor();

        batch.setColor(color.r, color.g, color.b, color.a);

        if (isVisible())
        {
            font.draw(batch,"Vida : " + hp,  getX()-(0.5f*getWidth()*Constantes.scale), getY()+(getHeight()*2)*Constantes.scale);
            batch.draw(texturas.get(id_textura), getX(), getY(),getOriginX(), getOriginY(),getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        }
    }
}
