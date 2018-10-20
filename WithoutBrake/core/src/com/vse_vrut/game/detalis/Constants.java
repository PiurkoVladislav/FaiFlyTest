package com.vse_vrut.game.detalis;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Влад on 16.12.2017.
 */
public class Constants {

    public static final Color BACKGROUND_COLOR = Color.WHITE;

    public static final float WORLD_SIZE = 128;

    public static final String TEXTURE_ATLAS = "images/withoutBrakes.pack.atlas";

    public static final String CAR_SPRITE = "car";
    public static final Vector2 CAR_EYE_POSITION = new Vector2(0,0);
    public static final float CAR_EYE_HEIGHT = 6.0f;
    public static final float CAR_MOVE_SPEED = 64;


    public static final String GATE_OPEN = "vorota1";
    public static final String GATE_CLOSE = "vorota2";

    public static final float OPENING_LOOP_DURATION= 0.25f;


}
