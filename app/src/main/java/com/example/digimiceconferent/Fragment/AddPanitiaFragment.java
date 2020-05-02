package com.example.digimiceconferent.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.digimiceconferent.MainViewModel;
import com.example.digimiceconferent.Model.Event;
import com.example.digimiceconferent.R;
import com.example.digimiceconferent.SharedPrefManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddPanitiaFragment extends Fragment {

    Button btAddPanitia;
    EditText etEmail;
    SharedPrefManager sharedPrefManager;
    ProgressBar loading;

    String eventId;

    public AddPanitiaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_panitia, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btAddPanitia = view.findViewById(R.id.bt_add_panitia);
        etEmail = view.findViewById(R.id.email_add_panitia);

        sharedPrefManager = new SharedPrefManager(getContext());

        btAddPanitia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                String email = etEmail.getText().toString().trim();

                boolean isEmptyField = false;

                if (TextUtils.isEmpty(email)) {
                    isEmptyField = true;
                    etEmail.setError("Email tidak boleh kosong");
                }

                if (!isEmptyField) {
                    if (etEmail.getText().toString().trim().matches(emailPattern)) {
                        addPanitia();
                    }else{
                        etEmail.setError("Email tidak valid");
                    }

                }
            }
        });


    }


    private void addPanitia(){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "http://192.168.3.5/myAPI/public/add-panitia";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                etEmail.setText(null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<String, String>();
                data.put("email", etEmail.getText().toString());
                data.put("event_id", sharedPrefManager.getSpIdEvent());
                data.put("name_team", sharedPrefManager.getSpNameTeam());
                return data;
            }
        };

        queue.add(request);

    }

}
