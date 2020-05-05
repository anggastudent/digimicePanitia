package com.example.digimiceconferent.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.MenuItem;
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
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_session);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Tambah Sesi");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        sharedPrefManager = new SharedPrefManager(this);
        etNamaSession = findViewById(R.id.name_add_session);
        btAddSession = findViewById(R.id.bt_add_session);

        dialog = new ProgressDialog(AddSession.this);
        dialog.setMessage("Memproses");
        btAddSession.setOnClickListener(new View.OnClickListener() {
            private long lastClick = 0;
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - lastClick < 1000) {
                    return;
                }
                lastClick = SystemClock.elapsedRealtime();

                boolean isEmpty = false;
                String nameSession = etNamaSession.getText().toString().trim();
                if (TextUtils.isEmpty(nameSession)) {
                    isEmpty = true;
                    etNamaSession.setError("Nama sesi tidak boleh kosong");
                }

                if (!isEmpty) {
                    showDialog(true);
                    addSession();
                }

            }
        });
    }

    private void addSession() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.3.5/myAPI/public/add-session";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                etNamaSession.setText(null);
                showDialog(false);
                Toast.makeText(getApplicationContext(), "berhasil", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showDialog(false);
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialog(Boolean state) {

        if (state) {
            dialog.show();
        } else {
            dialog.dismiss();
        }
    }
}
