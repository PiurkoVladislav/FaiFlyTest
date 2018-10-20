package com.vse_vrut.criminalintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

/**
 * Created by Влад on 16.02.2018.
 */

public class PictureUntils {

    public static Bitmap getScaledBitmap(String path, Activity activity){       //метод проверяет размер экрана и уменьшает изображение до его размеров
        Point size = new Point();

        activity.getWindowManager().getDefaultDisplay().getSize(size);

        return getScaledBitmap(path,size.x,size.y);
    }

    public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight){           //Клас для маштабирования изображения

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path,options);                         //чтение размеров изображения на диске

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        int inSampleSize = 1;                                           //определяет величину образца для каждого пиксела исходного изображения
        if (srcHeight> destHeight || srcWidth > destWidth){             //вычисление степени маштабирования
            if(srcWidth>srcHeight){
                inSampleSize = Math.round(srcHeight/destHeight);
            }else {
                inSampleSize = Math.round(srcWidth/destWidth);
            }
        }
        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        return BitmapFactory.decodeFile(path,options);                  //чтение данных и сздание итогового изобрражения
    }
}
