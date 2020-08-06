package com.example.digimiceconferent.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

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
    ProgressBar loading;
    LinearLayout noDataPage;
    SwipeRefreshLayout swipePending;

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
        loading = view.findViewById(R.id.loading_pending);
        swipePending = view.findViewById(R.id.swipe_pending);
        rvPending = view.findViewById(R.id.rv_pending);
        noDataPage = view.findViewById(R.id.no_data_pending);

        swipePending.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        adapter = new RecyclerViewPendingAdapter();
        sharedPrefManager = new SharedPrefManager(getContext());
        rvPending.setLayoutManager(new LinearLayoutManager(getContext()));

        showLoading(true);

        swipePending.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showData();
                swipePending.setRefreshing(false);
            }
        });

        rvPending.setAdapter(adapter);
        rvPending.setHasFixedSize(true);
        adapter.notifyDataSetChanged();
    }

    private void showData() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        MainViewModel mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
        mainViewModel.setListPending(queue, getContext(), sharedPrefManager.getSPIdUser(), sharedPrefManager.getSPToken());
        mainViewModel.getPending().observe(this, new Observer<ArrayList<Pending>>() {
            @Override
            public void onChanged(ArrayList<Pending> pendings) {
                if (pendings != null) {
                    showLoading(false);
                    showEmpty(false);
                    adapter.sendData(pendings);

                }
                if(pendings.size() == 0){
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
