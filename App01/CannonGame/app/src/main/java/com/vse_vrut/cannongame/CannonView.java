package com.vse_vrut.cannongame;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Влад on 08.11.2017.
 */

public class CannonView extends SurfaceView implements SurfaceHolder.Callback {
    public static final String TAG = "CannonView";//teg dla poiska owibok

    //igrovie konstanti
    public static final int MISS_PENALTY = 2;//wtraf za promah
    public static final int HIT_REWARD = 3;//poowrenie za popadanie

    //konstanti dla risovania puwki
    public static final double CANNON_BASE_RADIUS_PERCENT = 3.0 /40;
    public static final double CANNON_BARREL_WIDTH_PERCENT = 3.0 / 40;
    public static final double CANNON_BARREL_LENGTH_PERCENT = 1.0 / 10;

    //dlz risovania 9dra
    public static final double CANNONBALL_RADIUS_PERCENT = 3.0 / 80;
    public static final double CANNONBALL_SPEED_PERCENT = 3.0 / 2;

    //dlz risovania miwenei
    public static final double TARGET_WIDTH_PERCENT = 1.0 / 40;
    public static final double TARGET_LENGTH_PERCENT = 3.0 / 20;
    public static final double TARGET_FIRST_X_PERCENT = 3.0 / 5;
    public static final double TARGET_SPACING_PERCENT = 1.0 / 60;
    public static final double TARGET_PIECES = 9;
    public static final double TARGET_MIN_SPEED_PERCENT = 3.0 / 4;
    public static final double TARGET_MAX_SPEED_PERCENT = 6.0 / 4;

    //dlz risovania bloka
    public static final double BLOCKER_WIDTH_PERCENT = 1.0 / 40;
    public static final double BLOCKER_LENGTH_PERCENT = 1.0 / 4;
    public static final double BLOCKER_X_PERCENT = 1.0 / 2;
    public static final double BLOCKER_SPEED_PERCENT = 1.0;

    //pazmer teksta sostavliaet 1/18 ot wireni ekrana
    public static final double TEXT_SIZE_PERCENT = 1.0 / 18;

    private CannonThread cannonThread;// upravliaet ciklom igri
    private Activity activity;//dlz otobrajenia okna v potoke GUI
    private boolean dialogIsDisplayed = false;

    //Igrovie ob'ektu
    private Cannon cannon;
    private Blocker blocker;
    private List<Target> targets;

    //peremennie razmerov
    private int screenWidth;
    private int screenHeight;

    //peremennie dlz igrovogo cikla i otslejivani9 sosto9nii igri
    private boolean gameOver;//igra zakonchena?
    private double timeLeft;//ostalosi vremeni v sekundah
    private int shotsFired; // Kolichestvo vistrelov
    private double totalElapsedTime;//potracheno vremeni

    //konstantu i peremennie dl9 ypravleni9 zvukom
    public static final int TARGET_SOUND_ID = 0;
    public static final int CANNON_SOUND_ID = 1;
    public static final int BLOCKER_SOUND_ID = 2;
    private SoundPool soundPool;//vosproizvedenie zvyka
    private SparseIntArray soundMap;//sv9z' identifikatorov s Sound Pool

    //peremennie Paint dlz risovani9 elementov na ekrane
    private Paint textPaint;
    private Paint backgroundPaint;



    public CannonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        activity = (Activity)context;
        AudioAttributes.Builder attrBuilder;
        SoundPool.Builder builder;

        //pegistraci9 slywatel9 SurfaceHolder.Callback
        getHolder().addCallback(this);

        //nastroika atributov dl9 vosproizvedeni9 zvukov
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            attrBuilder = new AudioAttributes.Builder();
//            attrBuilder.setUsage(AudioAttributes.USAGE_GAME);
//            builder = new SoundPool.Builder();
//            builder.setMaxStreams(1);
//            builder.setAudioAttributes(attrBuilder.build());
//        }
//        soundMap = new SparseIntArray(3);
//        soundMap.put(TARGET_SOUND_ID, soundPool.load(context, R.raw.target_hit,1));
//        soundMap.put(CANNON_SOUND_ID, soundPool.load(context, R.raw.cannon_fire,1));
//        soundMap.put(BLOCKER_SOUND_ID, soundPool.load(context, R.raw.blocker_hit,1));

        textPaint = new Paint();
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.WHITE);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        screenWidth = w;
        screenHeight = h;

        textPaint.setTextSize((int) (TEXT_SIZE_PERCENT*screenHeight));
        textPaint.setAntiAlias(true);
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

//    public void playSound(int soundId){
//        soundPool.play(soundMap.get(soundId), 1, 1, 1, 0, 1f);
//    }

    //sbros vseh elementov ekrana i sozdanie novoi igri
    public void newGame(){
        //sozdanie puwki
        cannon = new Cannon(this,(int)(CANNON_BASE_RADIUS_PERCENT*screenHeight),
                (int)(CANNON_BARREL_LENGTH_PERCENT*screenWidth),
                (int)(CANNON_BARREL_WIDTH_PERCENT*screenHeight));
        Random random = new Random();
        targets = new ArrayList<>();

        //inicializaci9 targetX dl9 pervoi miwenisleva
        int targetX = (int)(TARGET_FIRST_X_PERCENT*screenWidth);

        //vichislenie koordinat Y
        int targetY = (int) ((0.5 - TARGET_LENGTH_PERCENT / 2) *
                 screenHeight);

        //dobavlenie TARGET_PIECES
        for(int n=0; n < TARGET_PIECES; n++) {

            //poluchenie sluchainoi skorosti ot min do max dlz miweni n
            double velocity = screenHeight * (random.nextDouble() *
                    (TARGET_MAX_SPEED_PERCENT - TARGET_MIN_SPEED_PERCENT)
                    + TARGET_MIN_SPEED_PERCENT);

            //cveta miwenei cheredyuts9 mejdy belim i chernim
            int color = (n % 2 == 0) ?
                    Color.BLUE :
                    Color.YELLOW;
//            if(Build.VERSION.SDK_INT >= 23){
//                 color = (n % 2 == 0) ?
//                        getResources().getColor(R.color.dark, getContext().getTheme()):
//                        getResources().getColor(R.color.light,getContext().getTheme());
//            }
            //skorost' protivopolojna9
            velocity *= -1;

            //sozdanie i dibavlenie novoi miweni v spisok
            targets.add(new Target(this, color, HIT_REWARD, targetX, targetY,
                    (int) (TARGET_WIDTH_PERCENT * screenWidth),
                    (int) (TARGET_LENGTH_PERCENT * screenHeight),
                    (int) velocity));
            //yvelichenie koordindti x dlz smeweni9 vpravo sled.miweni
            targetX += (TARGET_WIDTH_PERCENT + TARGET_SPACING_PERCENT) * screenWidth;
        }

            //postroenie novogo bloka
            blocker = new Blocker(this, Color.BLACK, MISS_PENALTY,
                    (int) (BLOCKER_X_PERCENT * screenWidth),
                    (int) ((0.5 - BLOCKER_LENGTH_PERCENT / 2) * screenHeight),
                    (int) (BLOCKER_WIDTH_PERCENT * screenWidth),
                    (int) (BLOCKER_LENGTH_PERCENT * screenHeight),
                    (float) (BLOCKER_SPEED_PERCENT * screenHeight));

            timeLeft = 10;
            shotsFired = 0;
            totalElapsedTime = 0.0;

            //nachat' novyu igry
            if(gameOver){
                gameOver = false;
                cannonThread = new CannonThread(getHolder());
                cannonThread.start();
            }
            hideSystemBars();

    }

    private void updatePositions(double elapsedTimeMS){
        double interval = elapsedTimeMS/1000.0;

        //obnovlenie pozicii 9dra
        if(cannon.getCannonball() != null){
            cannon.getCannonball().update(interval);
        }

        blocker.update(interval);

        for (GameElement target: targets
             ) {
            target.update(interval);
        }

        timeLeft -= interval;
        if(timeLeft<=0){
            timeLeft = 0.0;
            gameOver = true;
            cannonThread.setRunning(false);
            showGameOverDialog(R.string.lose);
        }

        //esli popal vo vse miweni
        if (targets.isEmpty()){
            cannonThread.setRunning(false);
            showGameOverDialog(R.string.win);
            gameOver = true;
        }
    }
    // Метод определяет угол наклона ствола и стреляет из пушки,
    // если ядро не находится на экране
         public void alignAndFireCannonball(MotionEvent event) {
             // Получение точки касания в этом представлении
             Point touchPoint = new Point((int) event.getX(),
                     (int) event.getY());

             // Вычисление расстояния точки касания от центра экрана
             // по оси y
             double centerMinusY = (screenHeight / 2 - touchPoint.y);


             double angle = 0; // Инициализировать angle значением 0

             // Вычислить угол ствола относительно горизонтали
             angle = Math.atan2(touchPoint.x, centerMinusY);

             // Ствол наводится в точку касания
             cannon.align(angle);

             // Пушка стреляет, если ядро не находится на экране
             if (cannon.getCannonball() == null ||
                     !cannon.getCannonball().isOnSceen()) {
                 cannon.fireCannonball();
                 ++shotsFired;
             }
         }

         private void showGameOverDialog(final int messageId){
             final DialogFragment gameResult = new DialogFragment() {
                 @Override
                 public Dialog onCreateDialog(Bundle savedInstanceState) {
                     //sozdanie dialogovogo okna s vivodom stroki soobweni9
                     AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                     builder.setTitle(getResources().getString(messageId));

                     //vivod kolichestva vustrelov i zatrachenogo vremeni
                     builder.setMessage(getResources().getString(R.string.results_format,
                             shotsFired, totalElapsedTime));
                     builder.setPositiveButton(R.string.reset_game,
                             new DialogInterface.OnClickListener() {
                                 @Override
                                 public void onClick(DialogInterface dialogInterface, int i) {
                                     dialogIsDisplayed = false;
                                     newGame();
                                 }
                             });
                     return builder.create();

                 }
             };
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showSystemBars(); // Выход из режима погружения
                    dialogIsDisplayed = true;
                    gameResult.setCancelable(false);
                    gameResult.show(activity.getFragmentManager(),"results");
                }
            });
         }

         public void drawGameElements(Canvas canvac){
             //ochistka fonf
             canvac.drawRect(0,0,canvac.getWidth(),canvac.getHeight(),backgroundPaint);
             //vivod ostavwegos9 vremeni
             canvac.drawText(getResources().getString(R.string.time_remaining_format, timeLeft),
                     50,100,textPaint);
             cannon.draw(canvac);//risovanie pywki
             //risovanie igrovih elementov
             if (cannon.getCannonball()!=null&&
                     cannon.getCannonball().isOnSceen()){
                 cannon.getCannonball().draw(canvac);
             }
             blocker.draw(canvac);

             for (GameElement target: targets
                  ) {
                 target.draw(canvac);
             }
         }
//proverka stolknoveni9 s blokom ili miwen'u
    public void testForCollisions(){
        //ydalenie miwenei slolknyvwixc9 s 9drom
        if(cannon.getCannonball() != null&&
                cannon.getCannonball().isOnSceen()){
            for (int n = 0; n <targets.size() ; n++) {
                if (cannon.getCannonball().collidesWith(targets.get(n))) {
//                    targets.get(n).playSound();//zvyk popadani9 v miwen'

                    //pribavlenie nagradu k ostavwemys9 vremeni
                    timeLeft += targets.get(n).getHitReward();

                    cannon.removeCannonball();
                    targets.remove(n);
                    --n;
                    break;

                }
            }
        }
        else {
            cannon.removeCannonball();
        }

        //proverka na popadanie v blok
        if (cannon.getCannonball()!= null &&
        cannon.getCannonball().collidesWith(blocker)){
//            blocker.playSound();

            cannon.getCannonball().reverseVelocityX();

            timeLeft -=blocker.getMissPenalty();
        }
    }

    public void stopGame(){
        if(cannonThread != null)
            cannonThread.setRunning(false);
    }

    public void releaseResources(){
        soundPool.release();
        soundPool = null;
    }

    //vuzuvaets9 pri sozdanii poverhnosti
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if(!dialogIsDisplayed){
            newGame();
            cannonThread = new CannonThread(surfaceHolder);
            cannonThread.setRunning(true);
            cannonThread.start();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        cannonThread.setRunning(false);

        while (retry){
            try {
                cannonThread.join();
                retry = false;
            }
            catch (InterruptedException e){
                Log.e(TAG,"Thread iterrupted",e);
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //int predstavl9et tip deistvi9
        int action = event.getAction();

        //koanie ekrana ili proveli po nemy
        if(action==MotionEvent.ACTION_DOWN||
                action == MotionEvent.ACTION_MOVE){
            alignAndFireCannonball(event);
        }
        return true;
    }

    private class CannonThread extends Thread{

        private SurfaceHolder surfaceHolder;
        private boolean threadIsRunning = true;

        public CannonThread(SurfaceHolder holder){
            surfaceHolder = holder;
            setName("CannonThread");
        }

        public void setRunning(boolean running){
            threadIsRunning = running;
        }

        @Override
        public void run() {
            Canvas canvas = null;
            long previousFrameTime = System.currentTimeMillis();

            while (threadIsRunning){
                try {
                    canvas = surfaceHolder.lockCanvas(null);

                    synchronized (surfaceHolder){
                        long currentTime = System.currentTimeMillis();
                        double elapsedTimeMS = currentTime - previousFrameTime;
                        totalElapsedTime += elapsedTimeMS / 1000.0;
                        updatePositions(elapsedTimeMS); // Обновление состояния игры
                        testForCollisions(); // Проверка столкновений с GameElement
                        drawGameElements(canvas); // Рисование на объекте canvas
                        previousFrameTime = currentTime; // Обновление времени
                    }

                }finally {
                    if(canvas!=null)
                        surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
    // Сокрытие системных панелей и панели приложения
    private void hideSystemBars() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_IMMERSIVE);
        }

        // Вывести системные панели и панель приложения
    private void showSystemBars() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
}
