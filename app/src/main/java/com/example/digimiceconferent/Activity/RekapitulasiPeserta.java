package com.example.digimiceconferent.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.digimiceconferent.Adapter.RecyclerViewRekapitulasiAdapter;
import com.example.digimiceconferent.MainViewModel;
import com.example.digimiceconferent.Model.EventSession;
import com.example.digimiceconferent.Model.Rekapitulasi;
import com.example.digimiceconferent.R;
import com.example.digimiceconferent.SharedPrefManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RekapitulasiPeserta extends AppCompatActivity {
    public static final String EXTRA_REKAPUTILASI = "extra rekapitulasi";
    TextView nameEvent, waktuEvent, nameSession, pukul, hadir, belumHadir, lunas, belumLunas;
    RecyclerView rvRekap;
    SharedPrefManager sharedPrefManager;
    RecyclerViewRekapitulasiAdapter adapter;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekapitulasi_peserta);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Rekapitulasi Peserta");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setElevation(0);
        }

        nameEvent = findViewById(R.id.name_event_rekap);
        nameSession = findViewById(R.id.name_session_rekap);
        waktuEvent = findViewById(R.id.waktu_event_rekap);
        pukul = findViewById(R.id.pukul_rekap);
        hadir = findViewById(R.id.hadir_rekap);
        belumHadir = findViewById(R.id.belum_hadir_rekap);
        lunas = findViewById(R.id.lunas_rekap);
        belumLunas = findViewById(R.id.belum_lunas_rekap);
        rvRekap = findViewById(R.id.rv_rekapitulasi);

        hadir.setText("0");
        belumHadir.setText("0");
        lunas.setText("0");
        belumLunas.setText("0");

        rvRekap.setLayoutManager(new LinearLayoutManager(this));
        sharedPrefManager = new SharedPrefManager(this);
        adapter = new RecyclerViewRekapitulasiAdapter();
        queue = Volley.newRequestQueue(this);

        final EventSession eventSession = getIntent().getParcelableExtra(EXTRA_REKAPUTILASI);
        if (eventSession != null) {
            MainViewModel mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
            mainViewModel.setListRekapitulasi(queue, this, sharedPrefManager.getSpIdEvent(), eventSession.getId());
            mainViewModel.getRekapitulasi().observe(this, new Observer<ArrayList<Rekapitulasi>>() {
                @Override
                public void onChanged(ArrayList<Rekapitulasi> rekapitulasis) {
                    int totalHadir = 0;
                    int totalBelumHadir = 0;
                    int totalLunas = 0;
                    int totalBelumLunas = 0;

                    if (rekapitulasis != null) {
                        adapter.sendData(rekapitulasis);
                        for (int i = 0; i < rekapitulasis.size(); i++) {
                            Rekapitulasi rekapitulasi = rekapitulasis.get(i);
                            if (rekapitulasi.getRekap().equals("Hadir")) {
                                totalHadir++;
                            } else if (rekapitulasi.getRekap().equals("Belum Hadir")) {
                                totalBelumHadir++;
                            }

                            if (rekapitulasi.getPaymentStatus().equals("Lunas")) {
                                totalLunas++;
                            } else if (rekapitulasi.getPaymentStatus().equals("Belum Lunas")) {
                                totalBelumLunas++;
                            }
                        }

                        hadir.setText(String.valueOf(totalHadir));
                        belumHadir.setText(String.valueOf(totalBelumHadir));
                        lunas.setText(String.valueOf(totalLunas));
                        belumLunas.setText(String.valueOf(totalBelumLunas));

                        waktuEvent.setText(sharedPrefManager.getSpWaktuEvent());
                        nameEvent.setText(sharedPrefManager.getSpNameEvent());
                        nameSession.setText(eventSession.getJudul());

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            Date dateStart = dateFormat.parse(eventSession.getStart());
                            Date dateEnd = dateFormat.parse(eventSession.getEnd());
                            SimpleDateFormat dateFormatTime = new SimpleDateFormat("HH:mm");
                            pukul.setText(dateFormatTime.format(dateStart)+" - "+dateFormatTime.format(dateEnd) + " WIB");

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }

                }
            });

            rvRekap.setAdapter(adapter);
            rvRekap.setHasFixedSize(true);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
