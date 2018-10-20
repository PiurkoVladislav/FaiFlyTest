package com.vse_vrut.cannongame;

/**
 * Created by Влад on 08.11.2017.
 */

public class Target extends GameElement {
    private int hitReward; //popadanie v miwen', + k vremeni

    public Target(CannonView view, int color, int hitReward, int x, int y, int width, int length, float velocityY) {
        super(view, color, CannonView.TARGET_SOUND_ID, x, y, width, length, velocityY);

        this.hitReward = hitReward;
    }

    public int getHitReward() {
        return hitReward;
    }
}
