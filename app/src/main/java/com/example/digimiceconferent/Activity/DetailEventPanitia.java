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
import com.example.digimiceconferent.Model.EventPanitia;
import com.example.digimiceconferent.Model.EventSessionPanitia;
import com.example.digimiceconferent.MainViewModel;
import com.example.digimiceconferent.R;
import com.example.digimiceconferent.Adapter.RecyclerViewListSessionPanitiaAdapter;

import java.util.ArrayList;

public class DetailEventPanitia extends AppCompatActivity {
    public static final String EXTRA_EVENT_PANITIA = "extra";
    TextView tvJudul,tvStart,tvEnd;
    RecyclerView rvSession;
    ProgressBar loadingSession;
    RecyclerViewListSessionPanitiaAdapter adapter;
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

        final EventPanitia eventPanitia = getIntent().getParcelableExtra(EXTRA_EVENT_PANITIA);
        if (eventPanitia != null) {
            tvJudul.setText(eventPanitia.getJudul());
            tvStart.setText(eventPanitia.getStart());
            tvEnd.setText(eventPanitia.getEnd());
        }

        queue = Volley.newRequestQueue(this);

        rvSession = findViewById(R.id.rv_eventSessionPanitia);
        rvSession.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RecyclerViewListSessionPanitiaAdapter();
        loadingSession = findViewById(R.id.loadingSessionPanitia);
        showLoading(true);

        mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
        mainViewModel.setListEventSessionPanitia(queue,this,"2");
        mainViewModel.getEventSessionPanitia().observe(this, new Observer<ArrayList<EventSessionPanitia>>() {
            @Override
            public void onChanged(ArrayList<EventSessionPanitia> eventSessionPanitias) {
                if (eventSessionPanitias != null) {
                    adapter.sendEventSessionPanitia(eventSessionPanitias);
                    showLoading(false);
                }
            }
        });

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
