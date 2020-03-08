package com.example.digimiceconferent.Fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.digimiceconferent.R;
import com.example.digimiceconferent.SectionPagerAdapter;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class PanitiaFragment extends Fragment {


    public PanitiaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_panitia, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SectionPagerAdapter sectionPagerAdapter = new SectionPagerAdapter(getChildFragmentManager(),getContext());
        ViewPager viewPager = view.findViewById(R.id.view_pager_panitia);
        viewPager.setAdapter(sectionPagerAdapter);
        TabLayout tabLayout = view.findViewById(R.id.tab_panitia);
        tabLayout.setupWithViewPager(viewPager);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setElevation(0);
    }
}
