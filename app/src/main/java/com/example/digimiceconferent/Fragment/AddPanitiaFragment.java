package com.example.digimiceconferent.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.digimiceconferent.MyUrl;
import com.example.digimiceconferent.R;
import com.example.digimiceconferent.SharedPrefManager;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddPanitiaFragment extends Fragment {

    Button btAddPanitia;
    EditText etEmail;
    SharedPrefManager sharedPrefManager;
    ProgressDialog dialog;

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
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Memproses...");
        dialog.setCancelable(false);
        sharedPrefManager = new SharedPrefManager(getContext());

        btAddPanitia.setOnClickListener(new View.OnClickListener() {
            private long lastClick = 0;
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - lastClick < 1000) {
                    return;
                }
                lastClick = SystemClock.elapsedRealtime();

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                String email = etEmail.getText().toString().trim();

                boolean isEmptyField = false;

                if (TextUtils.isEmpty(email)) {
                    isEmptyField = true;
                    etEmail.setError("Email tidak boleh kosong");
                }

                if (!isEmptyField) {
                    if (etEmail.getText().toString().trim().matches(emailPattern)) {
                        showDialog(true);
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
        String url = MyUrl.URL+"/add-panitia";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                etEmail.setText(null);
                showDialog(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showDialog(false);
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<String, String>();
                data.put("email", etEmail.getText().toString());
                data.put("event_id", sharedPrefManager.getSpIdEvent());
                data.put("name_team", sharedPrefManager.getSpNameTeam());
                data.put("token", sharedPrefManager.getSPToken());
                return data;
            }
        };
        queue.getCache().clear();
        queue.add(request);

    }

    private void showDialog(Boolean state) {

        if (state) {
            dialog.show();
        } else {
            dialog.dismiss();
        }
    }

}
