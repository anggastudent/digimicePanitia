package com.example.digimiceconferent.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.TextView;

import com.example.digimiceconferent.R;
import com.example.digimiceconferent.SectionPagerEventAdapter;
import com.example.digimiceconferent.SectionPagerPesertaAdapter;
import com.example.digimiceconferent.SharedPrefManager;
import com.google.android.material.tabs.TabLayout;

public class KelolaPeserta extends AppCompatActivity {
    TextView namaEvent, tempatEvent, alamatEvent, waktuEvent;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelola_peserta);

        sharedPrefManager = new SharedPrefManager(this);
        namaEvent = findViewById(R.id.name_kelola_event_peserta);
        tempatEvent = findViewById(R.id.tempat_kelola_event_peserta);
        alamatEvent = findViewById(R.id.alamat_kelola_event_peserta);
        waktuEvent = findViewById(R.id.tanggal_kelola_event_peserta);

        SectionPagerPesertaAdapter sectionPagerPesertaAdapter = new SectionPagerPesertaAdapter(getSupportFragmentManager(), this);
        ViewPager viewPager = findViewById(R.id.view_pager_peserta);
        viewPager.setAdapter(sectionPagerPesertaAdapter);

        TabLayout tabLayout = findViewById(R.id.tab_kelola_peserta);
        tabLayout.setupWithViewPager(viewPager);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setTitle("Kelola Peserta");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        namaEvent.setText(sharedPrefManager.getSpNameEvent());
        tempatEvent.setText(sharedPrefManager.getSpPlaceEvent());
        alamatEvent.setText(sharedPrefManager.getSpAddressEvent());
        waktuEvent.setText(sharedPrefManager.getSpWaktuEvent());
    }
}
