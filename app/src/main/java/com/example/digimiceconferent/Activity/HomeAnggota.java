package com.example.digimiceconferent.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.digimiceconferent.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeAnggota extends AppCompatActivity {
//    final Fragment fragmentEvent = new EventFragment();
//    final Fragment fragmentPeserta = new PesertaFragment();
//    final Fragment fragmentAkun = new AkunFragment();
//
//    final FragmentManager fm = getSupportFragmentManager();
//    Fragment active = fragmentEvent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_anggota);

        getSupportActionBar().setTitle("Event");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_anggota);
        TextView largeTextView = bottomNavigationView.findViewById(R.id.bottom_nav_anggota).findViewById(com.google.android.material.R.id.largeLabel);
        TextView smallTextView = bottomNavigationView.findViewById(R.id.bottom_nav_anggota).findViewById(com.google.android.material.R.id.smallLabel);
        largeTextView.setVisibility(View.GONE);
        smallTextView.setVisibility(View.VISIBLE);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_anggota);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_event, R.id.navigation_paket, R.id.navigation_peserta,
                R.id.navigation_pembayaran, R.id.navigation_akun
        ).build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
//        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
//        bottomNavigationView.getMenu().findItem(R.id.navigation_event).setChecked(true);
//
//        fm.beginTransaction().add(R.id.nav_host_fragment_anggota, fragmentEvent, "1").commit();
//        fm.beginTransaction().add(R.id.nav_host_fragment_anggota, fragmentPeserta, "2").hide(fragmentPeserta).commit();
//        fm.beginTransaction().add(R.id.nav_host_fragment_anggota, fragmentAkun, "3").hide(fragmentAkun).commit();


    }

//    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//            switch (menuItem.getItemId()) {
//                case R.id.navigation_event:
//                    fm.beginTransaction().hide(active).show(fragmentEvent).commit();
//                    active = fragmentEvent;
//                    getSupportActionBar().setTitle("Event");
//                    return true;
//                case R.id.navigation_peserta:
//                    fm.beginTransaction().hide(active).show(fragmentPeserta).commit();
//                    active = fragmentPeserta;
//                    getSupportActionBar().setTitle("Peserta");
//                    return true;
//                case R.id.navigation_akun:
//                    fm.beginTransaction().hide(active).show(fragmentAkun).commit();
//                    active = fragmentAkun;
//                    getSupportActionBar().setTitle("Akun");
//                    return true;
//            }
//            return false;
//        }
//    };
}
