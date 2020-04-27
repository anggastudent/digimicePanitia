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

import com.example.digimiceconferent.Adapter.SectionPagerPembayaranAdapter;
import com.example.digimiceconferent.R;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class PembayaranFragment extends Fragment {


    public PembayaranFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pembayaran, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SectionPagerPembayaranAdapter sectionPagerPembayaranAdapter = new SectionPagerPembayaranAdapter(getChildFragmentManager(), getContext());
        ViewPager viewPager = view.findViewById(R.id.view_pager_pembayaran);
        viewPager.setAdapter(sectionPagerPembayaranAdapter);
        TabLayout tabLayout = view.findViewById(R.id.tab_pembayaran);
        tabLayout.setupWithViewPager(viewPager);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(0);
    }
}
