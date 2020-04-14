package com.example.digimiceconferent.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.digimiceconferent.R;
import com.example.digimiceconferent.SectionPagerAdapter;
import com.example.digimiceconferent.SectionPagerScanAdapter;
import com.google.android.material.tabs.TabLayout;

public class ScanQRPeserta extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_q_r_peserta);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Scan QRCode");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        SectionPagerScanAdapter sectionPagerAdapter = new SectionPagerScanAdapter(getSupportFragmentManager(),this);
        ViewPager viewPager = findViewById(R.id.view_pager_scan);
        viewPager.setAdapter(sectionPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tab_scan);
        tabLayout.setupWithViewPager(viewPager);
        getSupportActionBar().setElevation(0);
    }
}
