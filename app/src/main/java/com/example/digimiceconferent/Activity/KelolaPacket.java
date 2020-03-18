package com.example.digimiceconferent.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.digimiceconferent.R;

public class KelolaPacket extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelola_packet);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Kelola Paket");
        }
    }
}
