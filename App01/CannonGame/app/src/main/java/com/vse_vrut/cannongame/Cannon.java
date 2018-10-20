package com.vse_vrut.cannongame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

/**
 * Created by Влад on 08.11.2017.
 */

public class Cannon  {
    private int baseRadius; // radius osnovania puwki
    private int barrelLength;//dlinna stvola
    private Point barrelEnd = new Point();//tochka okonchania stvola
    private double barrelAngle;// ugol naklona
    private Cannonball cannonball; // 9dro
    private Paint paint = new Paint();//dlia risovani9
    private CannonView view; // oblast' predstavlenia

    public Cannon(CannonView view, int baseRadius, int barrelLength, int barrelWidth) {
        this.baseRadius = baseRadius;
        this.barrelLength = barrelLength;
        this.view = view;
        paint.setStrokeWidth(barrelWidth);
        paint.setColor(Color.BLACK);
        align(Math.PI / 2); //stvol puwki vpravo
    }

    public void align(double barrelAngle){
        this.barrelAngle = barrelAngle;
        barrelEnd.x = (int)(barrelLength*Math.sin(barrelAngle));
        barrelEnd.y = (int)(-barrelLength*Math.cos(barrelAngle))+ view.getScreenHeight()/2;
    }
    //metod sozdaiot 9dro i streliaet v napravlenii stvola
    public void fireCannonball(){
        //gorizomtal'naa sostavliauwaia skorosri 9dra
        int velocityX = (int)(CannonView.CANNONBALL_SPEED_PERCENT*view.getScreenHeight()
                * Math.sin(barrelAngle));
        //vertikal'naa sostavliauwaia skorosri 9dra
        int velocityY = (int)(CannonView.CANNONBALL_SPEED_PERCENT*view.getScreenHeight()
                * -Math.cos(barrelAngle));
        //radius
        int radius = (int)(view.getScreenHeight()*CannonView.CANNONBALL_RADIUS_PERCENT);
        //postroenie 9dra i ustanovka ego v stvole
        cannonball = new Cannonball(view, Color.BLACK, CannonView.CANNON_SOUND_ID,
                -radius,view.getScreenHeight()/2 - radius,radius,velocityX,velocityY);

//        cannonball.playSound();
    }
    //risuem puwku
    public void draw(Canvas canvas){
        //risuem stvol
        canvas.drawLine(0, view.getScreenHeight()/2,barrelEnd.x,
                barrelEnd.y, paint);
        //risuem osnovanie puwki
        canvas.drawCircle(0,(int) view.getScreenHeight()/2,
                (int)baseRadius, paint);
    }
    public Cannonball getCannonball(){
        return cannonball;
    }

    public void removeCannonball(){
        cannonball= null;
    }
}
