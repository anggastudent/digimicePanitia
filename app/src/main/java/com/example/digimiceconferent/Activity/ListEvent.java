package com.example.digimiceconferent.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.digimiceconferent.Model.EventPanitia;
import com.example.digimiceconferent.MainViewModel;
import com.example.digimiceconferent.R;
import com.example.digimiceconferent.Adapter.RecyclerViewListEventPanitiaAdapter;

import java.util.ArrayList;

public class ListEvent extends AppCompatActivity {
    private RecyclerView rvMovies;
    private ProgressBar progressBarMovie;
    private RecyclerViewListEventPanitiaAdapter adapterMovie;
    private MainViewModel mainViewModel;
    private RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_event);

        queue = Volley.newRequestQueue(this);

        rvMovies = findViewById(R.id.rv_eventPanitia);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        adapterMovie = new RecyclerViewListEventPanitiaAdapter();

        progressBarMovie = findViewById(R.id.loadingEventPanitia);

        showLoading(true);

        mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
        mainViewModel.setEventPanitia(queue, this);
        mainViewModel.getEventPanitia().observe(this, new Observer<ArrayList<EventPanitia>>() {
            @Override
            public void onChanged(ArrayList<EventPanitia> eventPanitias) {
                if (eventPanitias!= null) {
                    adapterMovie.sendEventPanitia(eventPanitias);
                    showLoading(false);
                }
            }
        });

        rvMovies.setAdapter(adapterMovie);
        rvMovies.setHasFixedSize(true);
        adapterMovie.notifyDataSetChanged();


    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBarMovie.setVisibility(View.VISIBLE);
        } else {
            progressBarMovie.setVisibility(View.GONE);
        }

    }
}
