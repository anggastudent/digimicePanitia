package com.example.digimiceconferent.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
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
    ProgressBar loading;
    LinearLayout noDataPage;
    MainViewModel mainViewModel;

    public SessionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
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
        loading = view.findViewById(R.id.loading_session);
        noDataPage = view.findViewById(R.id.no_data_session);

        showLoading(true);
        if (sharedPrefManager.getSpPresenceType().equals("Bebas")) {
           addSession.hide();

        }

        addSession.setOnClickListener(new View.OnClickListener() {
            private long lastClick = 0;
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - lastClick < 1000) {
                    return;
                }
                lastClick = SystemClock.elapsedRealtime();

                Intent intent = new Intent(getContext(), AddSession.class);
                startActivity(intent);
            }
        });
        rvSesi.setLayoutManager(new LinearLayoutManager(getContext()));
        mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
        mainViewModel.getSearchSession().observe(getViewLifecycleOwner(), new Observer<ArrayList<EventSession>>() {
            @Override
            public void onChanged(ArrayList<EventSession> eventSessions) {
                if (eventSessions != null) {
                    adapter.sendData(eventSessions);
                    showLoading(false);
                    showEmpty(false);
                }

                if (eventSessions.size() == 0) {
                    showLoading(false);
                    showEmpty(true);

                }
            }
        });

        rvSesi.setHasFixedSize(true);
        rvSesi.setAdapter(adapter);
        adapter.notifyDataSetChanged();

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
    private void showData() {
        mainViewModel.setListEventSessionPanitia(queue, getContext(),sharedPrefManager.getSpIdEvent(), sharedPrefManager.getSPToken());
        mainViewModel.getEventSessionPanitia().observe(this, new Observer<ArrayList<EventSession>>() {
            @Override
            public void onChanged(ArrayList<EventSession> sessions) {
                if (sessions != null) {
                    adapter.sendData(sessions);
                    showLoading(false);
                    showEmpty(false);
                }

                if (sessions.size() == 0) {
                    showEmpty(true);
                    showLoading(false);
                }
            }
        });

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
        searchView.setQueryHint("Cari Sesi");
        searchView.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                queue = Volley.newRequestQueue(getContext());
                showLoading(true);
                //Toast.makeText(getContext(), query, Toast.LENGTH_SHORT).show();
                mainViewModel.setSearchSession(queue,getContext(), sharedPrefManager.getSpIdEvent(),query, sharedPrefManager.getSPToken());
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
