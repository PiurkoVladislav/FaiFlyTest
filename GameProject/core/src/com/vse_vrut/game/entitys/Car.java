package com.vse_vrut.game.entitys;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.vse_vrut.game.detalis.Assets;
import com.vse_vrut.game.detalis.Constants;

/**
 * Created by Влад on 16.12.2017.
 */
public class Car {

    public final String TAG = Car.class.getName();

    public Vector2 position;


    public Car(){
        position = new Vector2(20,Constants.CAR_EYE_HEIGHT);
    }

    private void init(){

    }

    public void update(float delta){

        if (Gdx.input.justTouched()){
            move(delta);
        }
    }

    private void move(float delta){
        while (true){
            position.x +=delta*Constants.CAR_MOVE_SPEED;
        }
    }

    public void render(SpriteBatch batch){

        TextureRegion region = Assets.instance.carAssets.car;

        batch.draw(region.getTexture(),
                position.x - Constants.CAR_EYE_POSITION.x,
                position.y - Constants.CAR_EYE_POSITION.y,
                0,
                0,
                region.getRegionWidth(),
                region.getRegionHeight(),
                1,
                1,
                0,
                region.getRegionX(),
                region.getRegionY(),
                region.getRegionWidth(),
                region.getRegionHeight(),
                false,
                false);

    }

}
