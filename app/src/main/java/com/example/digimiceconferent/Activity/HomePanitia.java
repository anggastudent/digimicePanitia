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

public class HomePanitia extends AppCompatActivity {

//    final Fragment fragmentEvent = new EventFragment();
//    final Fragment fragmentPaket = new PaketFragment();
//    final Fragment fragmentPeserta = new PesertaFragment();
//    final Fragment fragmentPembayaran = new PembayaranFragment();
//    final Fragment fragmentAkun = new AkunFragment();
//
//    final FragmentManager fm = getSupportFragmentManager();
//    Fragment active = fragmentPaket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_panitia);

        getSupportActionBar().setTitle("Pilih Paket Event");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        TextView largeTextView = bottomNavigationView.findViewById(R.id.bottom_nav).findViewById(com.google.android.material.R.id.largeLabel);
        TextView smallTextView = bottomNavigationView.findViewById(R.id.bottom_nav).findViewById(com.google.android.material.R.id.smallLabel);
        largeTextView.setVisibility(View.GONE);
        smallTextView.setVisibility(View.VISIBLE);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_event, R.id.navigation_paket, R.id.navigation_peserta,
                R.id.navigation_pembayaran, R.id.navigation_akun
        ).build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        //bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        //bottomNavigationView.getMenu().findItem(R.id.navigation_panitia).setChecked(true);

//        fm.beginTransaction().add(R.id.nav_host_fragment, fragmentEvent, "1").hide(fragmentEvent).commit();
//        fm.beginTransaction().add(R.id.nav_host_fragment, fragmentPaket, "2").commit();
//        fm.beginTransaction().add(R.id.nav_host_fragment, fragmentPeserta, "3").hide(fragmentPeserta).commit();
//        fm.beginTransaction().add(R.id.nav_host_fragment, fragmentPembayaran, "4").hide(fragmentPembayaran).commit();
//        fm.beginTransaction().add(R.id.nav_host_fragment, fragmentAkun, "5").hide(fragmentAkun).commit();



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
//                case R.id.navigation_panitia:
//                    fm.beginTransaction().hide(active).show(fragmentPaket).commit();
//                    active = fragmentPaket;
//                    getSupportActionBar().setTitle("Pilih Paket Event");
//                    return true;
//                case R.id.navigation_peserta:
//                    fm.beginTransaction().hide(active).show(fragmentPeserta).commit();
//                    active = fragmentPeserta;
//                    getSupportActionBar().setTitle("Peserta");
//                    return true;
//                case R.id.navigation_pembayaran:
//                    fm.beginTransaction().hide(active).show(fragmentPembayaran).commit();
//                    active = fragmentPembayaran;
//                    getSupportActionBar().setTitle("Pembayaran");
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
//
//    private boolean connected(){
//        ConnectivityManager connection = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        return connection.getActiveNetworkInfo() != null;
//    }



}
