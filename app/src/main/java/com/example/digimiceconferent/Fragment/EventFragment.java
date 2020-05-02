package com.example.digimiceconferent.Fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
    SwipeRefreshLayout swipeEvent;

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
        swipeEvent = view.findViewById(R.id.swipe_list_event);
        swipeEvent.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        rvEvent = view.findViewById(R.id.rv_event);
        rvEvent.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecyclerViewEventAdapter();
        sharedPrefManager = new SharedPrefManager(getContext());
        loadingEvent = view.findViewById(R.id.loading_event);

        showLoading(true);

        swipeEvent.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                showData();
                swipeEvent.setRefreshing(false);
            }
        });
        rvEvent.setAdapter(adapter);
        rvEvent.setHasFixedSize(true);
        adapter.notifyDataSetChanged();
    }

    private void showData() {

        MainViewModel mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
        mainViewModel.setEventPanitia(queue, getContext(), sharedPrefManager.getSPIdUser());
        mainViewModel.getEventPanitia().observe(this, new Observer<ArrayList<Event>>() {
            @Override
            public void onChanged(ArrayList<Event> events) {
                if (events != null) {
                    adapter.sendEventPanitia(events);
                    showLoading(false);
                }
            }
        });
    }
    private void showLoading(Boolean state) {
        if (state) {
            loadingEvent.setVisibility(View.VISIBLE);
        } else {
            loadingEvent.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        showData();
    }
}
