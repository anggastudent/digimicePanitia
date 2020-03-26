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
import com.example.digimiceconferent.Adapter.RecyclerViewEventPacketAdapter;
import com.example.digimiceconferent.MainViewModel;
import com.example.digimiceconferent.Model.EventPacket;
import com.example.digimiceconferent.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaketFragment extends Fragment {

    ProgressBar loadingPaket;
    RecyclerView rvPacket;
    RecyclerViewEventPacketAdapter adapter;
    RequestQueue queue;

    public PaketFragment() {
        // Required empty public constructor
    }

    public static PaketFragment newInstance() {
        PaketFragment fragment = new PaketFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_paket, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        queue = Volley.newRequestQueue(getContext());
        rvPacket = view.findViewById(R.id.rv_paket);
        rvPacket.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecyclerViewEventPacketAdapter();
        loadingPaket = view.findViewById(R.id.loading_paket);
        showLoading(true);
        MainViewModel mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
        mainViewModel.setListPacket(queue, getContext());
        mainViewModel.getEventPacket().observe(this, new Observer<ArrayList<EventPacket>>() {
            @Override
            public void onChanged(ArrayList<EventPacket> eventPackets) {
                if (eventPackets != null) {
                    adapter.sendDataPacket(eventPackets);
                    showLoading(false);
                }
            }
        });
        rvPacket.setHasFixedSize(true);
        rvPacket.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void showLoading(Boolean state) {
        if (state) {
            loadingPaket.setVisibility(View.VISIBLE);
        } else {
            loadingPaket.setVisibility(View.GONE);
        }
    }
}
