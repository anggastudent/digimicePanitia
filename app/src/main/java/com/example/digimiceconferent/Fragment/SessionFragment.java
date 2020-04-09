package com.example.digimiceconferent.Fragment;

import android.content.Intent;
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
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.digimiceconferent.Activity.AddSession;
import com.example.digimiceconferent.Adapter.RecyclerViewSessionAdapter;
import com.example.digimiceconferent.MainViewModel;
import com.example.digimiceconferent.Model.EventSession;
import com.example.digimiceconferent.R;
import com.example.digimiceconferent.SharedPrefManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class SessionFragment extends Fragment {
    RecyclerView rvSesi;
    RequestQueue queue;
    RecyclerViewSessionAdapter adapter;
    SharedPrefManager sharedPrefManager;
    FloatingActionButton addSession;

    public SessionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_session, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvSesi = view.findViewById(R.id.rv_session_event);
        sharedPrefManager = new SharedPrefManager(getContext());
        adapter = new RecyclerViewSessionAdapter();
        queue = Volley.newRequestQueue(getContext());
        addSession = view.findViewById(R.id.add_session);

        if (sharedPrefManager.getSpPresenceType().equals("Bebas")) {
           addSession.hide();
        }

        addSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddSession.class);
                startActivity(intent);
            }
        });
        rvSesi.setLayoutManager(new LinearLayoutManager(getContext()));
        MainViewModel mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
        mainViewModel.setListEventSessionPanitia(queue, getContext(),sharedPrefManager.getSpIdEvent());
        mainViewModel.getEventSessionPanitia().observe(this, new Observer<ArrayList<EventSession>>() {
            @Override
            public void onChanged(ArrayList<EventSession> sessions) {
                adapter.sendData(sessions);
            }
        });

        rvSesi.setAdapter(adapter);
        rvSesi.setHasFixedSize(true);
        adapter.notifyDataSetChanged();

    }



}
