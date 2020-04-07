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

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.digimiceconferent.Adapter.RecyclerViewEventPresensiAdapter;
import com.example.digimiceconferent.MainViewModel;
import com.example.digimiceconferent.Model.Event;
import com.example.digimiceconferent.R;
import com.example.digimiceconferent.SharedPrefManager;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PesertaFragment extends Fragment {
    RequestQueue queue;
    RecyclerView rvPresensi;
    RecyclerViewEventPresensiAdapter adapter;
    SharedPrefManager sharedPrefManager;

    public PesertaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_peserta, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPrefManager = new SharedPrefManager(getContext());
        rvPresensi = view.findViewById(R.id.rv_event_presensi);
        queue = Volley.newRequestQueue(getContext());
        adapter = new RecyclerViewEventPresensiAdapter();
        rvPresensi.setLayoutManager(new LinearLayoutManager(getContext()));

        MainViewModel mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
        mainViewModel.setEventPanitia(queue, getContext(), sharedPrefManager.getSPIdUser());
        mainViewModel.getEventPanitia().observe(this, new Observer<ArrayList<Event>>() {
            @Override
            public void onChanged(ArrayList<Event> events) {
                adapter.sendData(events);
            }
        });

        rvPresensi.setAdapter(adapter);
        rvPresensi.setHasFixedSize(true);
        adapter.notifyDataSetChanged();

    }
}
