package com.vse_vrut.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

/**
 * Created by Влад on 01.02.2018.
 */

public class CrimePagerActivity extends AppCompatActivity {

    private static final String EXTRA_CRIME_ID = "com.vse_vrut.criminalintent.crime_id";
    private ViewPager mViewPager;
    private List<Crime> mCrimes;

    public static Intent newIntent(Context packageContext, UUID crimeId){
        Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID,crimeId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);      //загружаем макет активити

        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);     //Идентификатор преступления

        mViewPager = (ViewPager)findViewById(R.id.activity_crime_pager_view_pager); //привязываем макет ViewPager к коду

        mCrimes = CrimeLab.get(this).getCrimes();

        FragmentManager fragmentManager = getSupportFragmentManager();              //ФМ необходим ФСПА для добавления фрагментов в активность
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {      //Подгружаем адаптер, с помощью субкласса
            @Override                                                               //берущего на себя всю основную реализацию
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);                                //Достаем преступление по идентификатору,
                return CrimeFragment.newInstance(crime.getId());                    //с задаными параметрами(аргументами)
            }

            @Override
            public int getCount() {
                return mCrimes.size();                  //Возвращаем размер списка приступлений
            }
        });

        for (int i = 0; i<mCrimes.size(); i++){             //Назначаем ВП элемент с указаным индексом, вместо первого(по умолчанию)
            if(mCrimes.get(i).getId().equals(crimeId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
