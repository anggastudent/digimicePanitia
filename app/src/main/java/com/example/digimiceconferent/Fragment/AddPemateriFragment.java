package com.example.digimiceconferent.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.digimiceconferent.Model.Kabupaten;
import com.example.digimiceconferent.Model.Provinsi;
import com.example.digimiceconferent.R;
import com.example.digimiceconferent.SharedPrefManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyAgreement;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddPemateriFragment extends Fragment {

    EditText namePermateri, emailPemateri, password, repassword, noTelp;
    Spinner spKabupaten, spProvinsi;
    Button btAddPemateri;


    String provinsi_id;
    String kabupaten_id;

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
                }

            }
        });

        btAddPemateri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (password.getText().toString().equals(repassword.getText().toString())) {
                    addPemateri();
                }else{
                    Toast.makeText(getContext(), "Password Tidak Sama", Toast.LENGTH_SHORT).show();
                }



            }
        });

    }

    private void addPemateri() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        final SharedPrefManager sharedPrefManager = new SharedPrefManager(getContext());
        String url = "http://192.168.4.109/myAPI/public/add-pemateri";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
                return data;

            }

        };
        queue.add(request);
    }
}
