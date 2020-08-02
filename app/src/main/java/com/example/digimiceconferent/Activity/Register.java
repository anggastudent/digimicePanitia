package com.example.digimiceconferent.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.digimiceconferent.MainViewModel;
import com.example.digimiceconferent.Model.Kabupaten;
import com.example.digimiceconferent.Model.Provinsi;
import com.example.digimiceconferent.MyUrl;
import com.example.digimiceconferent.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    EditText etName, etEmail, etPassword, etRePassword, etTelp;
    Spinner spProvinsi, spKabupaten;
    Button btRegister;
    ProgressDialog dialog;
    String kabupaten_id = "";
    String provinsi_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Register Panitia");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        etName = findViewById(R.id.name_register);
        etEmail = findViewById(R.id.email_register);
        etPassword = findViewById(R.id.password_register);
        etRePassword = findViewById(R.id.re_password_register);
        etTelp = findViewById(R.id.no_telp_register);

        btRegister = findViewById(R.id.bt_submit_register);
        spProvinsi = findViewById(R.id.spinner_provinsi_register);
        spKabupaten = findViewById(R.id.spinner_kabupaten_register);

        dialog = new ProgressDialog(Register.this);
        dialog.setMessage("Memproses...");
        dialog.setCancelable(false);

        showData();

        btRegister.setOnClickListener(new View.OnClickListener() {
            private long lastClick = 0;
            @Override
            public void onClick(View v) {

                if (SystemClock.elapsedRealtime() - lastClick < 1000) {
                    return;
                }

                lastClick = SystemClock.elapsedRealtime();

                boolean isEmpty = false;

                if (TextUtils.isEmpty(etName.getText().toString())) {
                    isEmpty = true;
                    etName.setError("Nama Lengkap tidak boleh kosong");
                }

                if (TextUtils.isEmpty(etEmail.getText().toString())) {
                    isEmpty = true;
                    etEmail.setError("Email tidak boleh kosong");
                }

                if (TextUtils.isEmpty(etPassword.getText().toString())) {
                    isEmpty = true;
                    etPassword.setError("Password tidak boleh kosong");
                }

                if (TextUtils.isEmpty(etRePassword.getText().toString())) {
                    isEmpty = true;
                    etRePassword.setError("Verify Password tidak boleh kosong");
                }

                if (TextUtils.isEmpty(etTelp.getText().toString())) {
                    isEmpty = true;
                    etTelp.setError("No Telp tidak boleh kosong");
                }

                if (etPassword.getText().toString().trim().length() < 8) {
                    isEmpty = true;
                    etPassword.setError("Password harus minimal 8 karakter");
                }

                if (etPassword.getText().toString().equals(etRePassword.getText().toString())) {
                    if (!isEmpty) {
                        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                        if (etEmail.getText().toString().trim().matches(emailPattern)) {
                            showDialog(true);
                            register();
                        } else {
                            etEmail.setError("Email tidak valid");
                        }
                    }
                } else {
                    etRePassword.setError("Password tidak sama");
                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
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

    private void showData() {
        RequestQueue queue = Volley.newRequestQueue(this);
        MainViewModel mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
        mainViewModel.setListProvinsi(queue, this);
        mainViewModel.getProvinsi().observe(this, new Observer<ArrayList<Provinsi>>() {
            @Override
            public void onChanged(final ArrayList<Provinsi> provinsis) {

                if (provinsis != null) {
                    ArrayList<String> listProvinsi = new ArrayList<>();
                    for (int i = 0; i < provinsis.size(); i++) {
                        Provinsi provinsi = provinsis.get(i);
                        listProvinsi.add(provinsi.getName());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(Register.this, android.R.layout.simple_list_item_1, listProvinsi);
                    spProvinsi.setAdapter(adapter);
                    spProvinsi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            final Provinsi provinsi = provinsis.get(position);
                            provinsi_id = provinsi.getId();
                            ArrayList<String> listKabupaten = new ArrayList<>();
                            for (int j = 0; j < provinsi.getListKabupaten().size(); j++) {
                                Kabupaten kabupaten = provinsi.getListKabupaten().get(j);
                                listKabupaten.add(kabupaten.getName());
                            }

                            ArrayAdapter<String> adapterKabupaten = new ArrayAdapter<>(Register.this, android.R.layout.simple_list_item_1, listKabupaten);
                            spKabupaten.setAdapter(adapterKabupaten);
                            spKabupaten.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    Kabupaten kabupaten = provinsi.getListKabupaten().get(position);
                                    kabupaten_id = kabupaten.getId();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

            }
        });
    }

    private void register() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = MyUrl.URL+"/register-panitia";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                etName.setText(null);
                etEmail.setText(null);
                etPassword.setText(null);
                etRePassword.setText(null);
                etTelp.setText(null);
                showDialog(false);
                Toast.makeText(Register.this, "Berhasil Register", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showDialog(false);
                Toast.makeText(Register.this, "Gagal Register", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("name", etName.getText().toString());
                data.put("email", etEmail.getText().toString());
                data.put("password", etPassword.getText().toString());
                data.put("phone", etTelp.getText().toString());
                data.put("regencies_id", kabupaten_id);
                return data;
            }
        };
        queue.getCache().clear();
        queue.add(request);
    }
}
