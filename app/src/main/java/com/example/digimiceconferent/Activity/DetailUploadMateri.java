package com.example.digimiceconferent.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.digimiceconferent.Adapter.RecyclerViewMateriAdapter;
import com.example.digimiceconferent.Fragment.UploadMateriDialogFragment;
import com.example.digimiceconferent.MainViewModel;
import com.example.digimiceconferent.Model.Agenda;
import com.example.digimiceconferent.Model.Materi;
import com.example.digimiceconferent.R;
import com.example.digimiceconferent.SharedPrefManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DetailUploadMateri extends AppCompatActivity {
    public static final String EXTRA_MATERI = "materi";
    TextView tvSession, tvAgenda, tvTime;
    RecyclerView rvMateri;
    RecyclerViewMateriAdapter adapter;
    RequestQueue queue;
    FloatingActionButton addMateri;
    private int PICK_PDF_REQUEST = 1;
    File file;
    String filePdf;
    Uri filePath;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_upload_materi);

        tvSession = findViewById(R.id.session_event_agenda_materi);
        tvAgenda = findViewById(R.id.name_event_agenda_materi);
        tvTime = findViewById(R.id.tanggal_event_agenda_materi);
        addMateri = findViewById(R.id.add_materi);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setTitle("Detail Upload Materi");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        queue = Volley.newRequestQueue(this);
        sharedPrefManager = new SharedPrefManager(this);
        rvMateri = findViewById(R.id.rv_materi);
        adapter = new RecyclerViewMateriAdapter();
        rvMateri.setLayoutManager(new LinearLayoutManager(this));

        MainViewModel mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
        final Agenda agenda = getIntent().getParcelableExtra(EXTRA_MATERI);
        if (agenda != null) {
            sharedPrefManager.saveSPString(sharedPrefManager.SP_ID_AGENDA, agenda.getId());
            tvSession.setText(agenda.getSessionAgenda());
            tvAgenda.setText(agenda.getNamaAgenda());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date dateStart = dateFormat.parse(agenda.getStartAgenda());
                Date dateEnd = dateFormat.parse(agenda.getEndAgenda());
                SimpleDateFormat dateFormatNew = new SimpleDateFormat("dd MMMM yyyy HH:mm");
                tvTime.setText(dateFormatNew.format(dateStart)+"\n"+dateFormatNew.format(dateEnd));


            } catch (ParseException e) {
                e.printStackTrace();
            }

            mainViewModel.setListMateri(queue, this, agenda.getId());
            mainViewModel.getMateri().observe(this, new Observer<ArrayList<Materi>>() {
                @Override
                public void onChanged(ArrayList<Materi> materis) {
                    adapter.sendData(materis);
                }
            });
        }

        rvMateri.setAdapter(adapter);
        rvMateri.setHasFixedSize(true);
        adapter.notifyDataSetChanged();

        addMateri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UploadMateriDialogFragment fragment = new UploadMateriDialogFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragment.show(fragmentManager, UploadMateriDialogFragment.class.getSimpleName());


            }
        });

    }


}
