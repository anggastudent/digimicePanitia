package com.example.digimiceconferent.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
    LinearLayout noPageData;
    MainViewModel mainViewModel;

    public EventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
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
        noPageData = view.findViewById(R.id.no_data_event);

        showLoading(true);

        swipeEvent.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                showData();
                swipeEvent.setRefreshing(false);
            }
        });
        mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
        mainViewModel.getSearchEvent().observe(this, new Observer<ArrayList<Event>>() {
            @Override
            public void onChanged(ArrayList<Event> events) {
                showLoading(false);
                showEmpty(false);
                adapter.sendEventPanitia(events);

                if (events.size() == 0) {
                    showLoading(false);
                    showEmpty(true);
                }
            }
        });

        rvEvent.setAdapter(adapter);
        rvEvent.setHasFixedSize(true);
        adapter.notifyDataSetChanged();
    }

    private void showData() {

        mainViewModel.setEventPanitia(queue, getContext(), sharedPrefManager.getSPIdUser());
        mainViewModel.getEventPanitia().observe(this, new Observer<ArrayList<Event>>() {
            @Override
            public void onChanged(ArrayList<Event> events) {
                if (events != null) {
                    adapter.sendEventPanitia(events);
                    showLoading(false);
                    showEmpty(false);
                }

                if (events.size() == 0) {
                    showLoading(false);
                    showEmpty(true);
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

    private void showEmpty(Boolean state) {
        if (state) {
            noPageData.setVisibility(View.VISIBLE);
        } else {
            noPageData.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        showData();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search_event, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = new SearchView(getContext());
        final MainViewModel mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
        searchView.setQueryHint("Cari Event");
        searchView.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                queue = Volley.newRequestQueue(getContext());
                showLoading(true);
                mainViewModel.setSearchEvent(queue, getContext(), sharedPrefManager.getSPIdUser(),query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        item.setActionView(searchView);

    }


}
