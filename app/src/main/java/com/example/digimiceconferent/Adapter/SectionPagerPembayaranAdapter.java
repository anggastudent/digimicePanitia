package com.example.digimiceconferent.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.digimiceconferent.Fragment.BatalFragment;
import com.example.digimiceconferent.Fragment.BelumLunasFragment;
import com.example.digimiceconferent.Fragment.LunasFragment;
import com.example.digimiceconferent.R;

public class SectionPagerPembayaranAdapter extends FragmentPagerAdapter {

    private Context context;
    public SectionPagerPembayaranAdapter(@NonNull FragmentManager fm, Context mContext) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        context = mContext;
    }

    @StringRes
    private int[] TAB_TITLES = new int[]{
            R.string.tab_1_pembayaran,
            R.string.tab_2_pembayaran,
            R.string.tab_3_pembayaran
    };

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new BelumLunasFragment();
                break;
            case 1:
                fragment = new BatalFragment();
                break;
            case 2:
                fragment = new LunasFragment();
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
