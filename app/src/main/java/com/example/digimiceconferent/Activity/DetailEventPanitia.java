package com.example.digimiceconferent.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.digimiceconferent.Model.Event;
import com.example.digimiceconferent.Model.EventSession;
import com.example.digimiceconferent.MainViewModel;
import com.example.digimiceconferent.R;
import com.example.digimiceconferent.Adapter.RecyclerViewEventSessionAdapter;

import java.util.ArrayList;

public class DetailEventPanitia extends AppCompatActivity {
    public static final String EXTRA_EVENT_PANITIA = "extra";
    TextView tvJudul,tvStart,tvEnd;
    RecyclerView rvSession;
    ProgressBar loadingSession;
    RecyclerViewEventSessionAdapter adapter;
    MainViewModel mainViewModel;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event_panitia);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Detail Event");
        }

        tvJudul = findViewById(R.id.judulDetail);
        tvStart = findViewById(R.id.startDetail);
        tvEnd = findViewById(R.id.endDetail);

        final Event event = getIntent().getParcelableExtra(EXTRA_EVENT_PANITIA);
        if (event != null) {
            tvJudul.setText(event.getJudul());
            tvStart.setText(event.getStart());
            tvEnd.setText(event.getEnd());
        }

        queue = Volley.newRequestQueue(this);

        rvSession = findViewById(R.id.rv_eventSessionPanitia);
        rvSession.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RecyclerViewEventSessionAdapter();
        loadingSession = findViewById(R.id.loadingSessionPanitia);
        showLoading(true);



        rvSession.setAdapter(adapter);
        rvSession.setHasFixedSize(true);
        adapter.notifyDataSetChanged();

    }

    private void showLoading(Boolean state) {
        if (state) {
            loadingSession.setVisibility(View.VISIBLE);
        } else {
            loadingSession.setVisibility(View.GONE);
        }
    }
}
