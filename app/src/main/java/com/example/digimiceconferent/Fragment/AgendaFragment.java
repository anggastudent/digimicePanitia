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
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.digimiceconferent.Activity.AddAgenda;
import com.example.digimiceconferent.Adapter.RecyclerViewAgendaAdapter;
import com.example.digimiceconferent.MainViewModel;
import com.example.digimiceconferent.Model.Agenda;
import com.example.digimiceconferent.R;
import com.example.digimiceconferent.SharedPrefManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AgendaFragment extends Fragment {

    RecyclerView rvAgenda;
    RecyclerViewAgendaAdapter adapter;
    RequestQueue queue;
    SharedPrefManager sharedPrefManager;
    FloatingActionButton btAddAgenda;
    ProgressBar loading;
    LinearLayout noDataPage;

    public AgendaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_agenda, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        queue = Volley.newRequestQueue(getContext());
        sharedPrefManager = new SharedPrefManager(getContext());
        rvAgenda = view.findViewById(R.id.rv_agenda);
        btAddAgenda = view.findViewById(R.id.bt_add_agenda);
        loading = view.findViewById(R.id.loading_agenda);
        noDataPage = view.findViewById(R.id.no_data_agenda);

        btAddAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddAgenda.class);
                startActivity(intent);

            }
        });

        rvAgenda.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecyclerViewAgendaAdapter();

        showLoading(true);

        rvAgenda.setHasFixedSize(true);
        rvAgenda.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void showData() {
        MainViewModel mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
        mainViewModel.setListAgenda(queue,getContext(),sharedPrefManager.getSpIdEvent());
        mainViewModel.getAgenda().observe(this, new Observer<ArrayList<Agenda>>() {
            @Override
            public void onChanged(ArrayList<Agenda> agenda) {
                if (agenda != null) {
                    adapter.sendDataAgenda(agenda);
                    showLoading(false);
                    showEmpty(false);
                }

                if (agenda.size() == 0) {
                    showLoading(false);
                    showEmpty(true);
                }
            }
        });
    }

    private void showLoading(Boolean state) {
        if (state) {
            loading.setVisibility(View.VISIBLE);
        } else {
            loading.setVisibility(View.GONE);
        }
    }

    private void showEmpty(Boolean state) {
        if (state) {
            noDataPage.setVisibility(View.VISIBLE);
        } else {
            noDataPage.setVisibility(View.GONE);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        showData();
    }
}
