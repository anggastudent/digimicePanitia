package com.example.digimiceconferent.Fragment;


import android.content.Context;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.digimiceconferent.Adapter.RecyclerViewEventPresensiAdapter;
import com.example.digimiceconferent.MainViewModel;
import com.example.digimiceconferent.Model.EventPresensi;
import com.example.digimiceconferent.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventFragment extends Fragment {

    RecyclerView rvEvent;
    ProgressBar loadingEvent;
    RecyclerViewEventPresensiAdapter adapter;
    RequestQueue queue;
    ArrayList<EventPresensi> listData;


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
        listData = new ArrayList<>();
        queue = Volley.newRequestQueue(getContext());
        rvEvent = view.findViewById(R.id.rv_event_presensi);
        rvEvent.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecyclerViewEventPresensiAdapter();
        loadingEvent = view.findViewById(R.id.loading_event_presensi);
        MainViewModel mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
        mainViewModel.setEventPanitia(queue, getContext());
        mainViewModel.getEventPanitia().observe(this, new Observer<ArrayList<EventPresensi>>() {
            @Override
            public void onChanged(ArrayList<EventPresensi> eventPresensis) {
                if (eventPresensis != null) {
                    adapter.sendEventPanitia(eventPresensis);
                    showLoading(false);
                }
            }
        });
        showLoading(true);
        adapter.sendEventPanitia(listData);
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
