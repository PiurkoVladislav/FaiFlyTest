package com.vse_vrut.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vse_vrut.criminalintent.database.CrimeBaseHalper;
import com.vse_vrut.criminalintent.database.CrimeCursorWrapper;
import com.vse_vrut.criminalintent.database.CrimeDbSchema;
import com.vse_vrut.criminalintent.database.CrimeDbSchema.CrimeTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Влад on 31.10.2017.
 */

public class CrimeLab {

    private static CrimeLab sCrimeLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CrimeLab get(Context context){
        if(sCrimeLab == null){
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHalper(mContext)
                .getWritableDatabase();
//        mAppContext = appContext;
    }


    public List<Crime> getCrimes() {
        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper cursor = queryCrimes(null,null);

        try {                                       //Курсоры всегда устанавливаются в определенную позицию
            cursor.moveToFirst();                   //чтоб извлечь данные переносим его на парвый элемент
            while (!cursor.isAfterLast()){          //пока мы в пределах выбора данных,
                crimes.add(cursor.getCrime());      //считываем данные каждой строки
                cursor.moveToNext();
            }
        }finally {
            cursor.close();                         //ВАЖНО!!!!!
        }
        return crimes;
    }

    public void addCrime(Crime c){
        ContentValues values = getContentValues(c);

        mDatabase.insert(CrimeTable.NAME,null,values);      //первый параметр передает название базы, третий значение, а второй дает возможность создать пустую вставку
    }

    public void deleteCrime(UUID uuid){
        mDatabase.delete(CrimeTable.NAME,CrimeTable.Cols.UUID + " = ?",new String[]{uuid.toString()});
    }

    public Crime getCrime(UUID id){
        CrimeCursorWrapper cursor = queryCrimes(CrimeTable.Cols.UUID + " = ?", new String[]{id.toString()});

        try {
            if (cursor.getCount()==0){
                return null;
            }

            cursor.moveToFirst();                   //чтоб извлечь данные переносим его на парвый элемент
            return cursor.getCrime();
        }finally {
            cursor.close();                         //ВАЖНО!!!!!
        }
    }

    public void updateCrime(Crime crime){
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);

        mDatabase.update(CrimeTable.NAME, values,
                CrimeTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    private static ContentValues getContentValues(Crime crime){         //Метод заполняет базу данных значениями Crime
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID, crime.getId().toString() );
        values.put(CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CrimeTable.Cols.SOLVED, crime.isSolved()? 1:0);
        values.put(CrimeTable.Cols.SUSPECT, crime.getSuspect());

        return values;
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                CrimeTable.NAME,
                null,                //Columns - null выбирает все столбцы
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new CrimeCursorWrapper(cursor);
    }
}
