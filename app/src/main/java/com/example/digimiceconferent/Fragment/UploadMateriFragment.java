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
    MainViewModel mainViewModel;
    RecyclerViewAgendaMateriAdapter adapter;
    ProgressBar loading;
    LinearLayout noDataPage;

    public UploadMateriFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_upload_materi, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPrefManager = new SharedPrefManager(getContext());
        rvAgendaMateri = view.findViewById(R.id.rv_agenda_materi);
        loading = view.findViewById(R.id.loading_upload_materi);
        queue = Volley.newRequestQueue(getContext());
        adapter = new RecyclerViewAgendaMateriAdapter();
        rvAgendaMateri.setLayoutManager(new LinearLayoutManager(getContext()));
        noDataPage = view.findViewById(R.id.no_data_upload_materi);

        showLoading(true);

        rvAgendaMateri.setAdapter(adapter);
        rvAgendaMateri.setHasFixedSize(true);
        adapter.notifyDataSetChanged();
    }

    private void showData() {
        mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
        mainViewModel.setListAgenda(queue, getContext(), sharedPrefManager.getSpIdEvent(), sharedPrefManager.getSPToken());
        mainViewModel.getAgenda().observe(this, new Observer<ArrayList<Agenda>>() {
            @Override
            public void onChanged(ArrayList<Agenda> agenda) {
                if (agenda != null) {
                    adapter.sendData(agenda);
                    showLoading(false);
                    showEmpty(false);
                }
                if(agenda.size() == 0){
                    showLoading(false);
                    showEmpty(true);
                }
            }
        });

        mainViewModel.getSearchAgenda().observe(getViewLifecycleOwner(), new Observer<ArrayList<Agenda>>() {
            @Override
            public void onChanged(ArrayList<Agenda> agenda) {
                if (agenda != null) {
                    adapter.sendData(agenda);
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search_event, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = new SearchView(getContext());
        searchView.setQueryHint("Cari Agenda");
        searchView.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                queue = Volley.newRequestQueue(getContext());
                showLoading(true);
                mainViewModel.setSearchAgenda(queue, getContext(), sharedPrefManager.getSpIdEvent(), query, sharedPrefManager.getSPToken());
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
