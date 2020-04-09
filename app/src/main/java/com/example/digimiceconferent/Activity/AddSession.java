package com.example.digimiceconferent.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.digimiceconferent.R;
import com.example.digimiceconferent.SharedPrefManager;

import java.util.HashMap;
import java.util.Map;

public class AddSession extends AppCompatActivity {
    EditText etNamaSession;
    Button btAddSession;
    SharedPrefManager sharedPrefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_session);

        sharedPrefManager = new SharedPrefManager(this);
        etNamaSession = findViewById(R.id.name_add_session);
        btAddSession = findViewById(R.id.bt_add_session);

        btAddSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSession();
            }
        });
    }

    private void addSession() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.4.107/myAPI/public/add-session";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "berhasil", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("name", etNamaSession.getText().toString());
                data.put("event_id", sharedPrefManager.getSpIdEvent());
                data.put("event_event_type_id", "3");
                return data;
            }
        };

        queue.add(request);
    }
}
