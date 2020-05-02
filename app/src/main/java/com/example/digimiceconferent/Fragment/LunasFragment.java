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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.digimiceconferent.Adapter.RecyclerViewPaidAdapter;
import com.example.digimiceconferent.MainViewModel;
import com.example.digimiceconferent.Model.Paid;
import com.example.digimiceconferent.R;
import com.example.digimiceconferent.SharedPrefManager;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class LunasFragment extends Fragment {
    RecyclerView rvPaid;
    RecyclerViewPaidAdapter adapter;
    SharedPrefManager sharedPrefManager;
    ProgressBar loading;
    SwipeRefreshLayout swipePaid;
    LinearLayout noDataPage;

    public LunasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lunas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPaid = view.findViewById(R.id.rv_paid);
        loading = view.findViewById(R.id.loading_paid);
        swipePaid = view.findViewById(R.id.swipe_paid);
        noDataPage = view.findViewById(R.id.no_data_paid);

        swipePaid.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        rvPaid.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecyclerViewPaidAdapter();
        sharedPrefManager = new SharedPrefManager(getContext());

        showLoading(true);
        swipePaid.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showData();
                swipePaid.setRefreshing(false);
            }
        });

        rvPaid.setAdapter(adapter);
        rvPaid.setHasFixedSize(true);
        adapter.notifyDataSetChanged();
    }

    private void showData() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        MainViewModel mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
        mainViewModel.setListPaid(queue, getContext(), sharedPrefManager.getSPIdUser());
        mainViewModel.getPaid().observe(this, new Observer<ArrayList<Paid>>() {
            @Override
            public void onChanged(ArrayList<Paid> paids) {
                if (paids != null) {
                    showLoading(false);
                    showEmpty(false);
                    adapter.sendData(paids);
                }
                if(paids.size()==0){
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
