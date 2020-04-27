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
    RequestQueue queue;
    RecyclerViewExpiredAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout linearLayout;

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
        linearLayout = view.findViewById(R.id.layout_expired);

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        rvExpired = view.findViewById(R.id.rv_expired);
        sharedPrefManagar = new SharedPrefManager(getContext());
        queue = Volley.newRequestQueue(getContext());
        adapter = new RecyclerViewExpiredAdapter();
        rvExpired.setLayoutManager(new LinearLayoutManager(getContext()));

//        MainViewModel mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
//        mainViewModel.setListExpired(queue, getContext(), sharedPrefManagar.getSPIdUser());
//        mainViewModel.getExpired().observe(this, new Observer<ArrayList<Expired>>() {
//            @Override
//            public void onChanged(ArrayList<Expired> expireds) {
//                adapter.sendData(expireds);
//            }
//        });

        refresh();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        refresh();
                    }
                }, 5000);
            }
        });
        rvExpired.setAdapter(adapter);
        rvExpired.setHasFixedSize(true);
        adapter.notifyDataSetChanged();
    }

    private void refresh() {
        MainViewModel mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
        mainViewModel.setListExpired(queue, getContext(), sharedPrefManagar.getSPIdUser());
        mainViewModel.getExpired().observe(this, new Observer<ArrayList<Expired>>() {
            @Override
            public void onChanged(ArrayList<Expired> expireds) {
                adapter.sendData(expireds);
            }
        });
    }
}
