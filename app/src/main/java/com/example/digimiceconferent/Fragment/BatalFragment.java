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

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.digimiceconferent.Adapter.RecyclerViewExpiredAdapter;
import com.example.digimiceconferent.MainViewModel;
import com.example.digimiceconferent.Model.Expired;
import com.example.digimiceconferent.R;
import com.example.digimiceconferent.SharedPrefManager;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BatalFragment extends Fragment {

    RecyclerView rvExpired;
    SharedPrefManager sharedPrefManagar;
    RecyclerViewExpiredAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressBar loading;
    LinearLayout noDataPage;

    public BatalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_batal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout = view.findViewById(R.id.swipe_expired);
        loading = view.findViewById(R.id.loading_expired);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        rvExpired = view.findViewById(R.id.rv_expired);
        noDataPage = view.findViewById(R.id.no_data_expired);

        sharedPrefManagar = new SharedPrefManager(getContext());

        adapter = new RecyclerViewExpiredAdapter();
        rvExpired.setLayoutManager(new LinearLayoutManager(getContext()));

        showLoading(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        rvExpired.setAdapter(adapter);
        rvExpired.setHasFixedSize(true);
        adapter.notifyDataSetChanged();
    }

    private void getData() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        MainViewModel mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
        mainViewModel.setListExpired(queue, getContext(), sharedPrefManagar.getSPIdUser());
        mainViewModel.getExpired().observe(this, new Observer<ArrayList<Expired>>() {
            @Override
            public void onChanged(ArrayList<Expired> expireds) {
                if (expireds != null) {
                    showLoading(false);
                    showEmpty(false);
                    adapter.sendData(expireds);
                }
                if(expireds.size() == 0){
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
        getData();
    }
}
