package com.example.digimiceconferent.Fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.digimiceconferent.Activity.EditProfil;
import com.example.digimiceconferent.Activity.Login;
import com.example.digimiceconferent.R;
import com.example.digimiceconferent.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class AkunFragment extends Fragment {

    TextView nameUser, emailUser, statusUser, teamUser;
    Button btEditProfil, btLogout;
    ImageView avatar;
    SharedPrefManager sharedPrefManager;

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
        teamUser = view.findViewById(R.id.team_user_akun);
        btLogout = view.findViewById(R.id.bt_logout_akun);
        avatar = view.findViewById(R.id.avatar_user_akun);

        sharedPrefManager = new SharedPrefManager(getContext());

        getData();

        if (!sharedPrefManager.getSPBoolean()) {
            Intent intent = new Intent(getContext(), Login.class);
            startActivity(intent);
            getActivity().finish();
        }
        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPrefManager.saveSPBoolean(sharedPrefManager.SP_BOOLEAN, false);
                Intent intent = new Intent(getContext(), Login.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        btEditProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditProfil.class);
                startActivity(intent);
            }
        });

    }

    private void getData() {
        RequestQueue queue = Volley.newRequestQueue(getContext());

        String url = "http://192.168.4.109/myAPI/public/user/" + sharedPrefManager.getSPIdUser();

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject data = response.getJSONObject(i);
                        nameUser.setText(data.getString("name"));
                        emailUser.setText(data.getString("email"));
                        statusUser.setText(data.getString("role"));
                        teamUser.setText(data.getString("team"));

                        Glide.with(getContext())
                                .load("http://192.168.4.109/myAPI/public/" + data.getString("avatar"))
                                .apply(new RequestOptions().override(150, 150))
                                .into(avatar);

                    }
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

        queue.add(arrayRequest);
    }
}
