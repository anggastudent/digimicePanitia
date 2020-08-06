package com.example.digimiceconferent.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.SystemClock;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.example.digimiceconferent.SharedPrefManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddPemateriFragment extends Fragment {

    EditText namePermateri, emailPemateri, password, repassword, noTelp;
    Spinner spKabupaten, spProvinsi;
    Button btAddPemateri;
    ProgressBar loading;

    String provinsi_id;
    String kabupaten_id;
    ProgressDialog dialog;

    public AddPemateriFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_pemateri, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        namePermateri = view.findViewById(R.id.name_add_pemateri);
        emailPemateri = view.findViewById(R.id.email_add_pemateri);
        password = view.findViewById(R.id.password_add_pemateri);
        repassword = view.findViewById(R.id.re_password_add_pemateri);
        noTelp = view.findViewById(R.id.no_telp_add_pemateri);
        spProvinsi = view.findViewById(R.id.spinner_provinsi);
        spKabupaten = view.findViewById(R.id.spinner_kabupaten);
        btAddPemateri = view.findViewById(R.id.bt_add_pemateri);
        loading = view.findViewById(R.id.loading_add_pemateri);

        showLoading(true);
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Memproses...");
        dialog.setCancelable(false);

        btAddPemateri.setOnClickListener(new View.OnClickListener() {
            private long lastClick = 0;
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - lastClick < 1000) {
                    return;
                }
                lastClick = SystemClock.elapsedRealtime();

                boolean isEmpty = false;
                String name = namePermateri.getText().toString().trim();
                String email = emailPemateri.getText().toString().trim();
                String pass = password.getText().toString().trim();
                String repass = repassword.getText().toString().trim();
                String telp = noTelp.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    isEmpty = true;
                    namePermateri.setError("Nama tidak boleh kosong");
                }
                if (TextUtils.isEmpty(email)) {
                    isEmpty = true;
                    emailPemateri.setError("Email tidak boleh kosong");
                }
                if (TextUtils.isEmpty(pass)) {
                    isEmpty = true;
                    password.setError("Password tidak boleh kosong");
                }
                if (TextUtils.isEmpty(repass)) {
                    isEmpty = true;
                    repassword.setError("Verifikasi password tidak boleh kosong");
                }
                if (TextUtils.isEmpty(telp)) {
                    isEmpty = true;
                    noTelp.setError("No telp tidak boleh kosong");
                }
                if (pass.length() < 8) {
                    isEmpty = true;
                    password.setError("Password minimal 8 karakter");
                }
                if (password.getText().toString().equals(repassword.getText().toString())) {
                    if (!isEmpty) {
                        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                        if (emailPemateri.getText().toString().trim().matches(emailPattern)) {
                            showDialog(true);
                            addPemateri();
                        }else{
                            emailPemateri.setError("Email tidak valid");
                        }

                    }
                }else{
                    repassword.setError("Password tidak sama");
                }

            }
        });
    }

    private void addPemateri() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        final SharedPrefManager sharedPrefManager = new SharedPrefManager(getContext());
        String url = MyUrl.URL+"/add-pemateri";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                namePermateri.setText(null);
                emailPemateri.setText(null);
                password.setText(null);
                repassword.setText(null);
                noTelp.setText(null);
                showDialog(false);
                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
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
                Map<String, String> data = new HashMap<>();
                data.put("name", namePermateri.getText().toString());
                data.put("email", emailPemateri.getText().toString());
                data.put("password", password.getText().toString());
                data.put("no_telp", noTelp.getText().toString());
                data.put("regencies_id", kabupaten_id);
                data.put("event_id", sharedPrefManager.getSpIdEvent());
                data.put("token", sharedPrefManager.getSPToken());
                return data;
            }
        };
        queue.getCache().clear();
        queue.add(request);
    }

    private void showData() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        MainViewModel mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
        mainViewModel.setListProvinsi(queue, getContext());
        mainViewModel.getProvinsi().observe(this, new Observer<ArrayList<Provinsi>>() {
            @Override
            public void onChanged(final ArrayList<Provinsi> provinsis) {

                if (provinsis != null) {
                    ArrayList<String> listProvinsi = new ArrayList<>();
                    for (int i = 0; i < provinsis.size(); i++) {
                        Provinsi provinsi = provinsis.get(i);
                        listProvinsi.add(provinsi.getName());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, listProvinsi);
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

                            ArrayAdapter<String> adapterKabupaten = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, listKabupaten);
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
                    showLoading(false);
                }

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        showData();
    }

    private void showLoading(Boolean state) {
        if (state) {
            loading.setVisibility(View.VISIBLE);
        } else {
            loading.setVisibility(View.GONE);
        }
    }

    private void showDialog(Boolean state) {

        if (state) {
            dialog.show();
        } else {
            dialog.dismiss();
        }
    }
}
