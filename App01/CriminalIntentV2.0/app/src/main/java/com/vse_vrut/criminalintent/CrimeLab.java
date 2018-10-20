package com.vse_vrut.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Влад on 31.10.2017.
 */

public class CrimeLab {

    private static CrimeLab sCrimeLab;
    private List<Crime> mCrimes;

    public static CrimeLab get(Context context){
        if(sCrimeLab == null){
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context Context) {
//        mAppContext = appContext;
        mCrimes = new ArrayList<>();
    }


    public List<Crime> getCrimes() {
        return mCrimes;
    }

    public void addCrime(Crime c){
        mCrimes.add(c);
    }

    public void removeCrime(UUID id){
        for (Crime crime: mCrimes
                ) {
            if(crime.getId().equals(id)){
                mCrimes.remove(crime);
            }
        }
    }

    public Crime getCrime(UUID id){
        for (Crime crime: mCrimes
             ) {
            if(crime.getId().equals(id)){
                return crime;
            }
        }
        return null;
    }
}
