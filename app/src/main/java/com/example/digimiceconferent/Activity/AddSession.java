package com.example.digimiceconferent.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.digimiceconferent.Fragment.DatePickerFragment;
import com.example.digimiceconferent.MyUrl;
import com.example.digimiceconferent.R;
import com.example.digimiceconferent.SharedPrefManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddSession extends AppCompatActivity implements View.OnClickListener, DatePickerFragment.DialogDateListener {
    EditText etNamaSession,etStartSession;
    TextView namaEvent, tempatEvent, alamatEvent, waktuEvent;
    Button btAddSession,btStartSession;
    SharedPrefManager sharedPrefManager;
    ProgressDialog dialog;
    final String START_DATE_PICKER = "start date picker";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_session);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Tambah Sesi");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        sharedPrefManager = new SharedPrefManager(this);

        namaEvent = findViewById(R.id.name_event_sesi);
        tempatEvent = findViewById(R.id.tempat_event_sesi);
        alamatEvent = findViewById(R.id.alamat_event_sesi);
        waktuEvent = findViewById(R.id.tanggal_event_sesi);

        etNamaSession = findViewById(R.id.name_add_session);
        etStartSession = findViewById(R.id.start_date_session);

        btAddSession = findViewById(R.id.bt_add_session);
        btStartSession = findViewById(R.id.bt_start_date_session);

        namaEvent.setText(sharedPrefManager.getSpNameEvent());
        tempatEvent.setText(sharedPrefManager.getSpPlaceEvent());
        alamatEvent.setText(sharedPrefManager.getSpAddressEvent());
        waktuEvent.setText(sharedPrefManager.getSpWaktuEvent());

        btStartSession.setOnClickListener(this);

        etStartSession.setEnabled(false);
        dialog = new ProgressDialog(AddSession.this);
        dialog.setMessage("Memproses...");
        dialog.setCancelable(false);
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
                String startSession = etStartSession.getText().toString().trim();
                if (TextUtils.isEmpty(nameSession)) {
                    isEmpty = true;
                    etNamaSession.setError("Nama sesi tidak boleh kosong");
                }
                if (TextUtils.isEmpty(startSession)) {
                    isEmpty = true;
                    etStartSession.setError("Start sesi tidak boleh kosong");
                }


                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String startDateSession = etStartSession.getText().toString();
                String startDateEvent = sharedPrefManager.getSpStartEvent();
                String endDateEvent = sharedPrefManager.getSpEndEvent();

                try {
                    Date dateStartSession = dateFormat.parse(startDateSession);
                    Date dateStartEvent = dateFormat.parse(startDateEvent);
                    Date dateEndEvent = dateFormat.parse(endDateEvent);


                    if (dateStartSession.before(dateStartEvent) && !dateStartSession.equals(dateStartEvent)) {
                        isEmpty = true;
                        //etStartSession.setError("Tanggal start sesi harus lebih dari start event");
                        Toast.makeText(AddSession.this,"Tanggal start sesi harus sesudah dari start event", Toast.LENGTH_SHORT).show();

                    }

                    if (dateStartSession.after(dateEndEvent) && !dateStartSession.equals(dateEndEvent)) {
                        isEmpty = true;
                        //etStartSession.setError("Tanggal start sesi harus lebih dari start event");
                        Toast.makeText(AddSession.this,"Tanggal start sesi harus sebelum dari end event", Toast.LENGTH_SHORT).show();

                    }


                } catch (Exception e) {
                    e.printStackTrace();
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
        String url = MyUrl.URL+"/add-session";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                etNamaSession.setText(null);
                etStartSession.setText(null);
                etStartSession.setError(null);
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
                data.put("start", etStartSession.getText().toString());
                data.put("token", sharedPrefManager.getSPToken());
                return data;
            }
        };
        queue.getCache().clear();
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
    private long lastClick = 0;
    @Override
    public void onClick(View v) {
        if (SystemClock.elapsedRealtime() - lastClick < 1000) {
            return;
        }
        lastClick = SystemClock.elapsedRealtime();

        switch (v.getId()) {
            case R.id.bt_start_date_session:
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
