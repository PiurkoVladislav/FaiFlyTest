package com.vse_vrut.cannongame;

/**
 * Created by Влад on 08.11.2017.
 */

public class Blocker extends GameElement {
    private int missPenalty; //poteria vremeni pri popadanii v blok

    public Blocker(CannonView view, int color,int missPenalty, int x, int y, int width, int length, float velocityY) {
        super(view, color, CannonView.BLOCKER_SOUND_ID, x, y, width, length, velocityY);
        this.missPenalty = missPenalty;
    }
    //wtraf pri popadanii v blok
    public int getMissPenalty() {
        return missPenalty;
    }
}
