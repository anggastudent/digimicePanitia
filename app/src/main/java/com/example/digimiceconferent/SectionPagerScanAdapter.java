package com.example.digimiceconferent;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.digimiceconferent.Fragment.ScanKartuNamaFragment;
import com.example.digimiceconferent.Fragment.ScanSmartphoneFragment;
import com.example.digimiceconferent.R;

public class SectionPagerScanAdapter extends FragmentPagerAdapter {

    private Context context;

    public SectionPagerScanAdapter(@NonNull FragmentManager fm,  Context mContext) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        context = mContext;
    }

    @StringRes
    private int[] TAB_TITLES = new int[]{
            R.string.tab_1_scan,
            R.string.tab_2_scan
    };
    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new ScanSmartphoneFragment();
                break;
            case 1:
                fragment = new ScanKartuNamaFragment();
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return context.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }
}
