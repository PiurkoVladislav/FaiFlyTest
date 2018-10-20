package com.vse_vrut.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.vse_vrut.game.entitys.Car;

/**
 * Created by Влад on 16.12.2017.
 */
public class Level {
    Car car;

    public Level(){
        car = new Car();
    }

    public void update(float delta){
            car.update(delta);

    }

    public void render(SpriteBatch batch){
        batch.begin();
        car.render(batch);
        batch.end();
    }
}
