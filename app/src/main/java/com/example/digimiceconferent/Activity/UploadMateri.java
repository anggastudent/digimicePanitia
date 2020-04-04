package com.example.digimiceconferent.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.digimiceconferent.Adapter.RecyclerViewAgendaMateriAdapter;
import com.example.digimiceconferent.MainViewModel;
import com.example.digimiceconferent.Model.Agenda;
import com.example.digimiceconferent.R;
import com.example.digimiceconferent.SharedPrefManager;

import java.util.ArrayList;

public class UploadMateri extends AppCompatActivity {

    TextView namaEvent, tempatEvent, alamatEvent, waktuEvent;
    SharedPrefManager sharedPrefManager;
    RecyclerView rvAgendaMateri;
    RequestQueue queue;
    RecyclerViewAgendaMateriAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_materi);

        sharedPrefManager = new SharedPrefManager(this);
        rvAgendaMateri = findViewById(R.id.rv_agenda_materi);
        queue = Volley.newRequestQueue(this);
        adapter = new RecyclerViewAgendaMateriAdapter();

        namaEvent = findViewById(R.id.name_event_materi);
        tempatEvent = findViewById(R.id.tempat_event_materi);
        alamatEvent = findViewById(R.id.alamat_event_materi);
        waktuEvent = findViewById(R.id.tanggal_event_materi);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setTitle("Upload Materi");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        namaEvent.setText(sharedPrefManager.getSpNameEvent());
        tempatEvent.setText(sharedPrefManager.getSpPlaceEvent());
        alamatEvent.setText(sharedPrefManager.getSpAddressEvent());
        waktuEvent.setText(sharedPrefManager.getSpWaktuEvent());

        rvAgendaMateri.setLayoutManager(new LinearLayoutManager(this));
        MainViewModel mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
        mainViewModel.setListAgenda(queue, this, sharedPrefManager.getSpIdEvent());
        mainViewModel.getAgenda().observe(this, new Observer<ArrayList<Agenda>>() {
            @Override
            public void onChanged(ArrayList<Agenda> agenda) {
                adapter.sendData(agenda);
            }
        });
        rvAgendaMateri.setAdapter(adapter);
        rvAgendaMateri.setHasFixedSize(true);
        adapter.notifyDataSetChanged();
    }
}
