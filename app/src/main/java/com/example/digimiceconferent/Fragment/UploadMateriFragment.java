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
import com.example.digimiceconferent.Adapter.RecyclerViewAgendaMateriAdapter;
import com.example.digimiceconferent.MainViewModel;
import com.example.digimiceconferent.Model.Agenda;
import com.example.digimiceconferent.R;
import com.example.digimiceconferent.SharedPrefManager;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadMateriFragment extends Fragment {
    SharedPrefManager sharedPrefManager;
    RecyclerView rvAgendaMateri;
    RequestQueue queue;
    RecyclerViewAgendaMateriAdapter adapter;

    public UploadMateriFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upload_materi, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPrefManager = new SharedPrefManager(getContext());
        rvAgendaMateri = view.findViewById(R.id.rv_agenda_materi);
        queue = Volley.newRequestQueue(getContext());
        adapter = new RecyclerViewAgendaMateriAdapter();

        rvAgendaMateri.setLayoutManager(new LinearLayoutManager(getContext()));
        MainViewModel mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
        mainViewModel.setListAgenda(queue, getContext(), sharedPrefManager.getSpIdEvent());
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
