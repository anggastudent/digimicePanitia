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

    RecyclerView rvPresensi;
    RecyclerViewEventPresensiAdapter adapter;
    SharedPrefManager sharedPrefManager;
    ProgressBar loading;
    SwipeRefreshLayout swipePeserta;
    LinearLayout noPageData;

    public PesertaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_peserta, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPrefManager = new SharedPrefManager(getContext());
        rvPresensi = view.findViewById(R.id.rv_event_presensi);
        loading = view.findViewById(R.id.loading_peserta);
        swipePeserta = view.findViewById(R.id.swipe_peserta);
        noPageData = view.findViewById(R.id.no_data_peserta);
        swipePeserta.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        adapter = new RecyclerViewEventPresensiAdapter();
        rvPresensi.setLayoutManager(new LinearLayoutManager(getContext()));

        showLoading(true);

        swipePeserta.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showData();
                swipePeserta.setRefreshing(false);
            }
        });
        rvPresensi.setAdapter(adapter);
        rvPresensi.setHasFixedSize(true);
        adapter.notifyDataSetChanged();

    }

    private void showData() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        MainViewModel mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
        mainViewModel.setEventPanitia(queue, getContext(), sharedPrefManager.getSPIdUser());
        mainViewModel.getEventPanitia().observe(this, new Observer<ArrayList<Event>>() {
            @Override
            public void onChanged(ArrayList<Event> events) {
                if (events != null) {
                    showLoading(false);
                    showEmpty(false);
                    adapter.sendData(events);
                }

                if (events.size() == 0) {
                    showLoading(false);
                    showEmpty(true);
                }

            }
        });

        mainViewModel.getSearchEvent().observe(this, new Observer<ArrayList<Event>>() {
            @Override
            public void onChanged(ArrayList<Event> events) {
                showLoading(false);
                adapter.sendData(events);
                showEmpty(false);

                if (events.size() == 0) {
                    showLoading(false);
                    showEmpty(true);
                }
            }

        });
    }

    private void showLoading(Boolean state) {
        if (state) {
            loading.setVisibility(View.VISIBLE);
        }else{
            loading.setVisibility(View.GONE);
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
                RequestQueue queue = Volley.newRequestQueue(getContext());
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
