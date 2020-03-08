package com.example.digimiceconferent.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.digimiceconferent.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomePanitia extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_panitia);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_event, R.id.navigation_panitia,
                R.id.navigation_peserta, R.id.navigation_pembayaran, R.id.navigation_akun).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        TextView largeTextView = bottomNavigationView.findViewById(R.id.bottom_nav).findViewById(com.google.android.material.R.id.largeLabel);
        TextView smallTextView = bottomNavigationView.findViewById(R.id.bottom_nav).findViewById(com.google.android.material.R.id.smallLabel);
        largeTextView.setVisibility(View.GONE);
        smallTextView.setVisibility(View.VISIBLE);

    }


}
