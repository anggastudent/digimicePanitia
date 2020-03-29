package com.example.digimiceconferent.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.TextView;

import com.example.digimiceconferent.Model.Event;
import com.example.digimiceconferent.R;
import com.example.digimiceconferent.SectionPagerEventAdapter;
import com.example.digimiceconferent.SharedPrefManager;
import com.google.android.material.tabs.TabLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class KelolaEvent extends AppCompatActivity {

    TextView namaEvent, tempatEvent, alamatEvent, waktuEvent;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelola_event);
        sharedPrefManager = new SharedPrefManager(this);
        namaEvent = findViewById(R.id.name_kelola_event_paket);
        tempatEvent = findViewById(R.id.tempat_kelola_event_paket);
        alamatEvent = findViewById(R.id.alamat_kelola_event_paket);
        waktuEvent = findViewById(R.id.tanggal_kelola_event_paket);

        SectionPagerEventAdapter sectionPagerAdapter = new SectionPagerEventAdapter(getSupportFragmentManager(), this);
        ViewPager viewPager = findViewById(R.id.view_pager_event);
        viewPager.setAdapter(sectionPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tab_kelola_event);
        tabLayout.setupWithViewPager(viewPager);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setTitle("Kelola Event");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        namaEvent.setText(sharedPrefManager.getSpNameEvent());
        tempatEvent.setText(sharedPrefManager.getSpPlaceEvent());
        alamatEvent.setText(sharedPrefManager.getSpAddressEvent());
        waktuEvent.setText(sharedPrefManager.getSpWaktuEvent());

    }
}
