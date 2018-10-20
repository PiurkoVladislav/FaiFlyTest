package com.vse_vrut.cannongame;

import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by Влад on 08.11.2017.
 */

public class Cannonball extends GameElement {
    private float velocityX;
    private boolean onSceen;


    public Cannonball(CannonView view, int color, int soundId, int x, int y, int radius, float velocityX, float velocityY) {
        super(view, color,soundId, x, y, 2*radius, 2*radius, velocityY);

        this.velocityX = velocityX;
        onSceen = true;
    }
    //radius 9dra
    private int getRadius(){
        return (shape.right- shape.left)/2;
    }

    //stolknovenie s ob'ektom
    public boolean collidesWith(GameElement element){
        return (Rect.intersects(shape, element.shape)&&velocityX>0);
    }

    //nahodits9 li 9dro na ekrane
    public boolean isOnSceen(){
        return onSceen;
    }

    //invertaci9 skorosti 9dra
    public void reverseVelocityX(){
        velocityX*= -1;
    }

    @Override
    public void update(double interval) {
        super.update(interval);//obnovlenie vertikal'noi pozicii 9dra

        //obnovlenie gorizontal'noi pozicii
        shape.offset((int)(velocityX*interval),0);

        //esli 9dro yhodit za predeli ekrana
        if(shape.top<0||shape.left<0||
                shape.bottom>view.getScreenHeight()||
                shape.right>view.getScreenHeight())
            onSceen=false;
    }
//risuem 9dro
    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(shape.left+getRadius(),shape.top+getRadius(),getRadius(),mPaint);
    }
}
