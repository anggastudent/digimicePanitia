package com.example.digimiceconferent;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.digimiceconferent.Fragment.AddPanitiaFragment;
import com.example.digimiceconferent.Fragment.AddPemateriFragment;
import com.example.digimiceconferent.Fragment.PaketFragment;
import com.example.digimiceconferent.Fragment.UploadMateriFragment;

public class SectionPagerAdapter extends FragmentPagerAdapter {
    private Context context;

    public SectionPagerAdapter(@NonNull FragmentManager fm, Context mContext) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        context = mContext;
    }

    @StringRes
    private int[] TAB_TITLES = new int[]{
            R.string.tab_1,
            R.string.tab_2,
            R.string.tab_4
    };

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0 :
                fragment = new PaketFragment();
                break;
            case 1 :
                fragment = new AddPanitiaFragment();
                break;
            case 2 :
                fragment = new AddPemateriFragment();
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
