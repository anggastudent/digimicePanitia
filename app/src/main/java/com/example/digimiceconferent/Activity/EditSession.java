package com.example.digimiceconferent.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.digimiceconferent.Model.EventSession;
import com.example.digimiceconferent.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditSession extends AppCompatActivity {
    public final static String EXTRA_EDIT_SESSION = "edit_session";

    EditText etNameSession;
    Button btEditSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_session);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Edit Sesi");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        etNameSession = findViewById(R.id.name_edit_session);
        btEditSession = findViewById(R.id.bt_edit_session);

        final EventSession eventSession = getIntent().getParcelableExtra(EXTRA_EDIT_SESSION);
        if (eventSession != null) {

            showSession(eventSession.getId());

            btEditSession.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editSession(eventSession.getId());
                }
            });
        }

    }

    private void editSession(String sessionId) {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "http://192.168.4.109/myAPI/public/edit-session/" + sessionId;
        StringRequest request = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
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
                data.put("name", etNameSession.getText().toString());
                return data;
            }
        };

        queue.add(request);

    }

    private void showSession(String sessionId) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.4.109/myAPI/public/show-session/" + sessionId;
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject data = response.getJSONObject(i);
                        etNameSession.setText(data.getString("name"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(arrayRequest);
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
}
