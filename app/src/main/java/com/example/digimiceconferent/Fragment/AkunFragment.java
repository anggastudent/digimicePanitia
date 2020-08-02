package com.example.digimiceconferent.Fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.digimiceconferent.Activity.EditProfil;
import com.example.digimiceconferent.Activity.Login;
import com.example.digimiceconferent.MyUrl;
import com.example.digimiceconferent.R;
import com.example.digimiceconferent.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class AkunFragment extends Fragment {

    TextView nameUser, emailUser, statusUser;
    Button btEditProfil, btLogout;
    ImageView avatar;
    SharedPrefManager sharedPrefManager;
    SwipeRefreshLayout swipeAkun;
    ProgressBar loading;

    public AkunFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_akun, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameUser = view.findViewById(R.id.name_user_akun);
        emailUser = view.findViewById(R.id.email_user_akun);
        statusUser = view.findViewById(R.id.status_user_akun);
        btEditProfil = view.findViewById(R.id.bt_edit_user_akun);
        btLogout = view.findViewById(R.id.bt_logout_akun);
        avatar = view.findViewById(R.id.avatar_user_akun);
        loading = view.findViewById(R.id.loading_akun);
        swipeAkun = view.findViewById(R.id.swipe_akun);

        sharedPrefManager = new SharedPrefManager(getContext());
        swipeAkun.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        showLoading(true);
        swipeAkun.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                swipeAkun.setRefreshing(false);
            }
        });
        if (!sharedPrefManager.getSPBoolean()) {
            Intent intent = new Intent(getContext(), Login.class);
            startActivity(intent);
            getActivity().finish();
        }
        btLogout.setOnClickListener(new View.OnClickListener() {
            private long lastClick = 0;
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - lastClick < 1000) {
                    return;
                }
                lastClick = SystemClock.elapsedRealtime();

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Apakah anda ingin keluar ?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sharedPrefManager.saveSPBoolean(sharedPrefManager.SP_BOOLEAN, false);
                        Intent intent = new Intent(getContext(), Login.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
                builder.setNegativeButton("Tidak", null);
                builder.show();

            }
        });

        btEditProfil.setOnClickListener(new View.OnClickListener() {
            private long lastClick = 0;
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - lastClick < 1000) {
                    return;
                }
                lastClick = SystemClock.elapsedRealtime();

                Intent intent = new Intent(getContext(), EditProfil.class);
                startActivity(intent);
            }
        });

    }

    private void getData() {
        RequestQueue queue = Volley.newRequestQueue(getContext());

        String url = MyUrl.URL+"/user/" + sharedPrefManager.getSPIdUser();

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject data = response.getJSONObject(i);
                        nameUser.setText(data.getString("name"));
                        emailUser.setText(data.getString("email"));
                        statusUser.setText(data.getString("role"));

                        Glide.with(getContext())
                                .load(MyUrl.URL+"/" + data.getString("avatar"))
                                .apply(new RequestOptions().override(150, 150))
                                .into(avatar);

                    }
                    showLoading(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.getCache().clear();
        queue.add(arrayRequest);
    }

    private void showLoading(Boolean state) {
        if (state) {
            loading.setVisibility(View.VISIBLE);
        } else {
            loading.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }
}
