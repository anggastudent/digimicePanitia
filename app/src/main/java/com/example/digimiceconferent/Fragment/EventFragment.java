package com.example.digimiceconferent.Fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.digimiceconferent.Adapter.RecyclerViewEventAdapter;
import com.example.digimiceconferent.MainViewModel;
import com.example.digimiceconferent.Model.Event;
import com.example.digimiceconferent.R;
import com.example.digimiceconferent.SharedPrefManager;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventFragment extends Fragment {

    RecyclerView rvEvent;
    ProgressBar loadingEvent;
    RecyclerViewEventAdapter adapter;
    RequestQueue queue;
    SharedPrefManager sharedPrefManager;

    public EventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        queue = Volley.newRequestQueue(getContext());
        rvEvent = view.findViewById(R.id.rv_event_presensi);
        rvEvent.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecyclerViewEventAdapter();
        sharedPrefManager = new SharedPrefManager(getContext());
        loadingEvent = view.findViewById(R.id.loading_event_presensi);
        String user_id = sharedPrefManager.getSPIdUser();
        showLoading(true);
        MainViewModel mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
        mainViewModel.setEventPanitia(queue, getContext(),user_id);
        mainViewModel.getEventPanitia().observe(this, new Observer<ArrayList<Event>>() {
            @Override
            public void onChanged(ArrayList<Event> events) {
                if (events != null) {
                    adapter.sendEventPanitia(events);
                    showLoading(false);
                }
            }
        });

        rvEvent.setAdapter(adapter);
        rvEvent.setHasFixedSize(true);
        adapter.notifyDataSetChanged();
    }

    private void showLoading(Boolean state) {
        if (state) {
            loadingEvent.setVisibility(View.VISIBLE);
        } else {
            loadingEvent.setVisibility(View.GONE);
        }
    }
}
