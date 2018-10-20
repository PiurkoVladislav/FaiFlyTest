package com.vse_vrut.cannongame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by Влад on 08.11.2017.
 */

public class GameElement {
    protected CannonView view;
    protected Paint mPaint = new Paint();
    protected Rect shape;//ограничивающий прямоугольник
    private float velocityY;//вертикальная скорость элемента
    private int soundId;

    public GameElement(CannonView view, int color, int soundId, int x, int y, int width, int length, float velocityY) {
        this.view = view;
        mPaint.setColor(color);
        shape = new Rect(x, y, x + width, y + length);
        this.soundId = soundId;
        this.velocityY = velocityY;
    }

    public void update(double interval){
        //Обновление вертикальной позиции
        shape.offset(0,(int)(velocityY*interval));

        if(shape.top<0 && velocityY<0||shape.bottom>view.getScreenHeight()&&velocityY>0){
            velocityY*= -1;//изменить скорость на противопожожную
        }
    }
    //просисовка елемента на Canvas
    public void draw(Canvas canvas){
        canvas.drawRect(shape,mPaint);
    }
    //воспроизведение звука
//    public void playSound(){
//        view.playSound(soundId);
//    }
}
