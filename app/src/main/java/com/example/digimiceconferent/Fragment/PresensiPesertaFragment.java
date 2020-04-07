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
import com.example.digimiceconferent.Adapter.RecyclerViewEventSessionAdapter;
import com.example.digimiceconferent.MainViewModel;
import com.example.digimiceconferent.Model.EventSession;
import com.example.digimiceconferent.R;
import com.example.digimiceconferent.SharedPrefManager;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PresensiPesertaFragment extends Fragment {

    RecyclerView rvSession;
    RecyclerViewEventSessionAdapter adapter;
    RequestQueue queue;
    SharedPrefManager sharedPrefManager;

    public PresensiPesertaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_presensi_peserta, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvSession = view.findViewById(R.id.rv_session);
        rvSession.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecyclerViewEventSessionAdapter();
        queue = Volley.newRequestQueue(getContext());
        sharedPrefManager = new SharedPrefManager(getContext());

        MainViewModel mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
        mainViewModel.setListEventSessionPanitia(queue, getContext(), sharedPrefManager.getSpIdEvent());
        mainViewModel.getEventSessionPanitia().observe(this, new Observer<ArrayList<EventSession>>() {
            @Override
            public void onChanged(ArrayList<EventSession> eventSessions) {
                adapter.sendEventSessionPanitia(eventSessions);
            }
        });

        rvSession.setAdapter(adapter);
        rvSession.setHasFixedSize(true);
        adapter.notifyDataSetChanged();
    }
}
