package com.brix.budgetingapplication.adapters;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.brix.budgetingapplication.R;
import com.brix.budgetingapplication.fragment.FragmentBudget;
import com.brix.budgetingapplication.fragment.FragmentExpense;
import com.brix.budgetingapplication.fragment.FragmentIncome;


/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SecondSectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tabIncome, R.string.tabExpense, R.string.tabBudget};
    private final Context mContext;

    public SecondSectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new FragmentIncome();
                break;
            case 1:
                fragment = new FragmentExpense();
                break;
            case 2:
                fragment = new FragmentBudget();
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
        return 3;
    }
}