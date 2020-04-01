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

    private Spinner spEvent;
    private Button btAddPanitia;
    EditText etEmail;
    MainViewModel mainViewModel;
    SharedPrefManager sharedPrefManager;

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

        spEvent = view.findViewById(R.id.spinner_event);
        btAddPanitia = view.findViewById(R.id.bt_add_panitia);
        etEmail = view.findViewById(R.id.email_add_panitia);

        sharedPrefManager = new SharedPrefManager(getContext());
        String user_id = sharedPrefManager.getSPIdUser();

        RequestQueue queue = Volley.newRequestQueue(getContext());
        mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
        mainViewModel.setEventPanitia(queue, getContext(),user_id);
        mainViewModel.getEventPanitia().observe(this, new Observer<ArrayList<Event>>() {
            @Override
            public void onChanged(final ArrayList<Event> events) {
                ArrayList<String> list = new ArrayList<>();
                for (int i=0; i<events.size();i++) {
                    Event dataEvent = events.get(i);
                    list.add(dataEvent.getJudul());
                }

                if (events != null) {
                    final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,list);
                    spEvent.setAdapter(adapter);
                    spEvent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Event event = events.get(position);
                            eventId = event.getId();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }
        });

        btAddPanitia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPanitia();
            }
        });


    }

    public void addPanitia(){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "http://192.168.4.107/myAPI/public/add-panitia";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Gagal", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<String, String>();
                data.put("email", etEmail.getText().toString());
                data.put("event_id", eventId);
                data.put("name_team", sharedPrefManager.getSpNameTeam());
                return data;
            }
        };

        queue.add(request);

    }

}
