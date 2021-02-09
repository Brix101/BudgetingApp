package com.brix.budgetingapp.adapters;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.brix.budgetingapp.R;
import com.brix.budgetingapp.fragment.FragmentCalendar;
import com.brix.budgetingapp.fragment.FragmentDaily;
import com.brix.budgetingapp.fragment.FragmentMonthly;
import com.brix.budgetingapp.fragment.FragmentWeekly;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tabDaily,R.string.tabCalendar, R.string.tabWeekly, R.string.tabMonthly};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new FragmentDaily();
                break;
            case 1:
                fragment = new FragmentCalendar();
                break;
            case 2:
                fragment = new FragmentWeekly();
                break;
            case 3:
                fragment = new FragmentMonthly();
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 4;
    }

}