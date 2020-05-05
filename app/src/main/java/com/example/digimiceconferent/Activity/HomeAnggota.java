package com.example.digimiceconferent.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.digimiceconferent.Fragment.AkunFragment;
import com.example.digimiceconferent.Fragment.EventFragment;
import com.example.digimiceconferent.Fragment.PesertaFragment;
import com.example.digimiceconferent.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeAnggota extends AppCompatActivity {
    final Fragment fragmentEvent = new EventFragment();
    final Fragment fragmentPeserta = new PesertaFragment();
    final Fragment fragmentAkun = new AkunFragment();

    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragmentEvent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_anggota);

        getSupportActionBar().setTitle("Event");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_anggota);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        bottomNavigationView.getMenu().findItem(R.id.navigation_event).setChecked(true);

        fm.beginTransaction().add(R.id.nav_host_fragment_anggota, fragmentEvent, "1").commit();
        fm.beginTransaction().add(R.id.nav_host_fragment_anggota, fragmentPeserta, "2").hide(fragmentPeserta).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment_anggota, fragmentAkun, "3").hide(fragmentAkun).commit();

        TextView largeTextView = bottomNavigationView.findViewById(R.id.bottom_nav_anggota).findViewById(com.google.android.material.R.id.largeLabel);
        TextView smallTextView = bottomNavigationView.findViewById(R.id.bottom_nav_anggota).findViewById(com.google.android.material.R.id.smallLabel);
        largeTextView.setVisibility(View.GONE);
        smallTextView.setVisibility(View.VISIBLE);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.navigation_event:
                    fm.beginTransaction().hide(active).show(fragmentEvent).commit();
                    active = fragmentEvent;
                    getSupportActionBar().setTitle("Event");
                    return true;
                case R.id.navigation_peserta:
                    fm.beginTransaction().hide(active).show(fragmentPeserta).commit();
                    active = fragmentPeserta;
                    getSupportActionBar().setTitle("Peserta");
                    return true;
                case R.id.navigation_akun:
                    fm.beginTransaction().hide(active).show(fragmentAkun).commit();
                    active = fragmentAkun;
                    getSupportActionBar().setTitle("Akun");
                    return true;
            }
            return false;
        }
    };
}
