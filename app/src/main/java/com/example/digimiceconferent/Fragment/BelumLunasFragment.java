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
import com.example.digimiceconferent.Adapter.RecyclerViewPendingAdapter;
import com.example.digimiceconferent.MainViewModel;
import com.example.digimiceconferent.Model.Pending;
import com.example.digimiceconferent.R;
import com.example.digimiceconferent.SharedPrefManager;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BelumLunasFragment extends Fragment {

    SharedPrefManager sharedPrefManager;
    RecyclerView rvPending;
    RecyclerViewPendingAdapter adapter;
    RequestQueue queue;

    public BelumLunasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_belum_lunas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPending = view.findViewById(R.id.rv_pending);
        adapter = new RecyclerViewPendingAdapter();
        queue = Volley.newRequestQueue(getContext());
        sharedPrefManager = new SharedPrefManager(getContext());
        rvPending.setLayoutManager(new LinearLayoutManager(getContext()));

        MainViewModel mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
        mainViewModel.setListPending(queue, getContext(), sharedPrefManager.getSPIdUser());
        mainViewModel.getPending().observe(this, new Observer<ArrayList<Pending>>() {
            @Override
            public void onChanged(ArrayList<Pending> pendings) {
                adapter.sendData(pendings);
            }
        });

        rvPending.setAdapter(adapter);
        rvPending.setHasFixedSize(true);
        adapter.notifyDataSetChanged();
    }
}
