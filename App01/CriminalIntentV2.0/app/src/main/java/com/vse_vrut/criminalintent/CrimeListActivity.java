package com.vse_vrut.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by Влад on 01.11.2017.
 */

public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
