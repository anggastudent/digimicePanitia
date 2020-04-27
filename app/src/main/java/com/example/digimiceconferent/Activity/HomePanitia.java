package com.example.digimiceconferent.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.digimiceconferent.Fragment.AkunFragment;
import com.example.digimiceconferent.Fragment.EventFragment;
import com.example.digimiceconferent.Fragment.PanitiaFragment;
import com.example.digimiceconferent.Fragment.PembayaranFragment;
import com.example.digimiceconferent.Fragment.PesertaFragment;
import com.example.digimiceconferent.R;
import com.example.digimiceconferent.SharedPrefManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomePanitia extends AppCompatActivity {

    final Fragment fragmentEvent = new EventFragment();
    final Fragment fragmentPanitia = new PanitiaFragment();
    final Fragment fragmentPeserta = new PesertaFragment();
    final Fragment fragmentPembayaran = new PembayaranFragment();
    final Fragment fragmentAkun = new AkunFragment();

    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragmentPanitia;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_panitia);

        getSupportActionBar().setTitle("Panitia");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        bottomNavigationView.getMenu().findItem(R.id.navigation_panitia).setChecked(true);

        fm.beginTransaction().add(R.id.nav_host_fragment, fragmentEvent, "1").hide(fragmentEvent).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, fragmentPanitia, "2").commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, fragmentPeserta, "3").hide(fragmentPeserta).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, fragmentPembayaran, "4").hide(fragmentPembayaran).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, fragmentAkun, "5").hide(fragmentAkun).commit();

//        TextView largeTextView = bottomNavigationView.findViewById(R.id.bottom_nav).findViewById(com.google.android.material.R.id.largeLabel);
//        TextView smallTextView = bottomNavigationView.findViewById(R.id.bottom_nav).findViewById(com.google.android.material.R.id.smallLabel);
//        largeTextView.setVisibility(View.GONE);
//        smallTextView.setVisibility(View.VISIBLE);

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
                case R.id.navigation_panitia:
                    fm.beginTransaction().hide(active).show(fragmentPanitia).commit();
                    active = fragmentPanitia;
                    getSupportActionBar().setTitle("Panitia");
                    return true;
                case R.id.navigation_peserta:
                    fm.beginTransaction().hide(active).show(fragmentPeserta).commit();
                    active = fragmentPeserta;
                    getSupportActionBar().setTitle("Peserta");
                    return true;
                case R.id.navigation_pembayaran:
                    fm.beginTransaction().hide(active).show(fragmentPembayaran).commit();
                    active = fragmentPembayaran;
                    getSupportActionBar().setTitle("Pembayaran");
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
