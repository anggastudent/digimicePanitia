package com.example.digimiceconferent.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.digimiceconferent.Fragment.DatePickerFragment;
import com.example.digimiceconferent.Model.EventSession;
import com.example.digimiceconferent.MyUrl;
import com.example.digimiceconferent.R;
import com.example.digimiceconferent.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditSession extends AppCompatActivity implements View.OnClickListener, DatePickerFragment.DialogDateListener{
    public final static String EXTRA_EDIT_SESSION = "edit_session";

    EditText etNameSession, etStartSession;
    Button btEditSession, btStartSession;
    ProgressDialog dialog;
    SharedPrefManager sharedPrefManager;
    final String START_DATE_PICKER = "start date picker";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_session);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Edit Sesi");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        sharedPrefManager = new SharedPrefManager(this);
        etNameSession = findViewById(R.id.name_edit_session);
        etStartSession = findViewById(R.id.start_date_session_edit);
        btEditSession = findViewById(R.id.bt_edit_session);
        btStartSession = findViewById(R.id.bt_start_date_session_edit);
        btStartSession.setOnClickListener(this);

        dialog = new ProgressDialog(EditSession.this);
        dialog.setMessage("Memproses...");
        dialog.setCancelable(false);

        final EventSession eventSession = getIntent().getParcelableExtra(EXTRA_EDIT_SESSION);
        if (eventSession != null) {

            showSession(eventSession.getId());

            btEditSession.setOnClickListener(new View.OnClickListener() {
                private long lastClick = 0;
                @Override
                public void onClick(View v) {
                    if (SystemClock.elapsedRealtime() - lastClick < 1000) {
                        return;
                    }
                    lastClick = SystemClock.elapsedRealtime();
                    boolean isEmpty = false;
                    String nama = etNameSession.getText().toString().trim();
                    String start = etStartSession.getText().toString().trim();
                    if (TextUtils.isEmpty(nama)) {
                        isEmpty = true;
                        etNameSession.setError("Nama tidak boleh kosong");
                    }
                    if (TextUtils.isEmpty(start)) {
                        isEmpty = true;
                        etStartSession.setError("Start tidak boleh kosong");
                    }

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String startDateSession = etStartSession.getText().toString();
                    String startDateEvent = sharedPrefManager.getSpStartEvent();

                    try {
                        Date dateStartSession = dateFormat.parse(startDateSession);
                        Date dateStartEvent = dateFormat.parse(startDateEvent);

                        if (dateStartSession.before(dateStartEvent) && !dateStartSession.equals(dateStartEvent)) {
                            isEmpty = true;
                            etStartSession.setError("Tanggal start sesi harus lebih dari start event");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (!isEmpty) {
                        showDialog(true);
                        editSession(eventSession.getId());
                    }

                }
            });
        }

    }

    private void editSession(String sessionId) {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = MyUrl.URL+"/edit-session/" + sessionId;
        StringRequest request = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showDialog(false);
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
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
                data.put("name", etNameSession.getText().toString());
                data.put("start", etStartSession.getText().toString());
                return data;
            }
        };
        queue.getCache().clear();
        queue.add(request);

    }

    private void showSession(String sessionId) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = MyUrl.URL+"/show-session/" + sessionId;
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject data = response.getJSONObject(i);
                        etNameSession.setText(data.getString("name"));
                        etStartSession.setText(data.getString("start"));
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
        queue.getCache().clear();
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

    private void showDialog(Boolean state) {

        if (state) {
            dialog.show();
        } else {
            dialog.dismiss();
        }
    }
    private long lastClick = 0;
    @Override
    public void onClick(View v) {
        if (SystemClock.elapsedRealtime() - lastClick < 1000) {
            return;
        }
        lastClick = SystemClock.elapsedRealtime();

        switch (v.getId()) {
            case R.id.bt_start_date_session_edit:
                DatePickerFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), START_DATE_PICKER);
                break;
        }
    }

    @Override
    public void onDialogDataSet(String tag, int year, int mount, int dayOfMount) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, mount, dayOfMount);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        switch (tag) {
            case START_DATE_PICKER:
                etStartSession.setText(dateFormat.format(calendar.getTime()));
                break;
        }
    }
}
