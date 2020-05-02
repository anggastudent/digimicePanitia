package com.example.digimiceconferent.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.digimiceconferent.Fragment.DatePickerFragment;
import com.example.digimiceconferent.Fragment.TimePickerFragment;
import com.example.digimiceconferent.MainViewModel;
import com.example.digimiceconferent.Model.Agenda;
import com.example.digimiceconferent.Model.EventSession;
import com.example.digimiceconferent.R;
import com.example.digimiceconferent.SharedPrefManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditAgenda extends AppCompatActivity implements View.OnClickListener, DatePickerFragment.DialogDateListener, TimePickerFragment.DialogTimeListener {
    public final static String EXTRA_EDIT_AGENDA = "extra_edit";
    TextView namaAgenda, namaSession, waktuAgenda;
    EditText etNameAgenda, etDescAgenda, etStartDateAgenda, etStartTimeAgenda, etEndDateAgenda,
            etEndTimeAgenda;
    Spinner spSesi;
    Button btStartDateAgenda, btStartTimeAgenda, btEndDateAgenda, btEndTimeAgenda, btEditAgenda;
    SharedPrefManager sharedPrefManager;
    final String START_DATE_PICKER = "start date picker";
    final String START_TIME_PICKER = "start time picker";
    final String END_DATE_PICKER = "end date picker";
    final String END_TIME_PICKER = "end time picker";

    String start;
    String end;
    int index = 0;
    String id_session;
    String id_agenda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_agenda);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Edit Agenda");
        }

        sharedPrefManager = new SharedPrefManager(this);
        namaSession = findViewById(R.id.session_event_edit_agenda);
        namaAgenda = findViewById(R.id.name_event_edit_agenda);
        waktuAgenda = findViewById(R.id.tanggal_event_edit_agenda);

        etNameAgenda = findViewById(R.id.name_edit_agenda);
        etDescAgenda = findViewById(R.id.deskripsi_edit_agenda);
        etStartDateAgenda = findViewById(R.id.start_date_edit_agenda);
        etStartTimeAgenda = findViewById(R.id.start_time_edit_agenda);
        etEndDateAgenda = findViewById(R.id.end_date_edit_agenda);
        etEndTimeAgenda = findViewById(R.id.end_time_edit_agenda);

        spSesi = findViewById(R.id.spinner_session_edit_agenda);

        btStartDateAgenda = findViewById(R.id.bt_start_date_edit_agenda);
        btStartTimeAgenda = findViewById(R.id.bt_start_time_edit_agenda);
        btEndDateAgenda = findViewById(R.id.bt_end_date_edit_agenda);
        btEndTimeAgenda = findViewById(R.id.bt_end_time_edit_agenda);
        btEditAgenda = findViewById(R.id.bt_edit_agenda);


        btStartDateAgenda.setOnClickListener(this);
        btStartTimeAgenda.setOnClickListener(this);
        btEndDateAgenda.setOnClickListener(this);
        btEndTimeAgenda.setOnClickListener(this);
        btEditAgenda.setOnClickListener(this);

        final Agenda agenda = getIntent().getParcelableExtra(EXTRA_EDIT_AGENDA);
        if (agenda != null) {

            namaSession.setText(agenda.getSessionAgenda());
            namaAgenda.setText(agenda.getNamaAgenda());
            etNameAgenda.setText(agenda.getNamaAgenda());
            etDescAgenda.setText(agenda.getDescAgenda());
            id_session = agenda.getIdSession();
            id_agenda = agenda.getId();


            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date dateStart = dateFormat.parse(agenda.getStartAgenda());
                Date dateEnd = dateFormat.parse(agenda.getEndAgenda());
                SimpleDateFormat dateFormatNew = new SimpleDateFormat("dd MMMM yyyy HH:mm");
                SimpleDateFormat dateFormatDate = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat dateFormatTime = new SimpleDateFormat("HH:mm");
                waktuAgenda.setText(dateFormatNew.format(dateStart)+"\n"+dateFormatNew.format(dateEnd));
                etStartDateAgenda.setText(dateFormatDate.format(dateStart));
                etEndDateAgenda.setText(dateFormatDate.format(dateEnd));
                etStartTimeAgenda.setText(dateFormatTime.format(dateStart));
                etEndTimeAgenda.setText(dateFormatTime.format(dateEnd));
                id_session = agenda.getIdSession();

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        MainViewModel mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
        mainViewModel.setListEventSessionPanitia(queue, this, sharedPrefManager.getSpIdEvent());
        mainViewModel.getEventSessionPanitia().observe(this, new Observer<ArrayList<EventSession>>() {
            @Override
            public void onChanged(final ArrayList<EventSession> eventSessions) {

                if (eventSessions != null) {
                    ArrayList<String> list = new ArrayList<>();
                    for (int i = 0; i < eventSessions.size(); i++) {
                        EventSession eventSession = eventSessions.get(i);
                        list.add(eventSession.getJudul());
                        if (eventSession.getJudul().equals(agenda.getSessionAgenda())) {
                            index = i;
                        }
                    }


                    ArrayAdapter<String> adapter = new ArrayAdapter<>(EditAgenda.this, android.R.layout.simple_list_item_1, list);
                    spSesi.setAdapter(adapter);
                    spSesi.setSelection(index);
                    spSesi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            EventSession eventSession = eventSessions.get(position);
                            id_session = eventSession.getId();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_start_date_edit_agenda:
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getSupportFragmentManager(), START_DATE_PICKER);
                break;
            case R.id.bt_start_time_edit_agenda:
                TimePickerFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.show(getSupportFragmentManager(), START_TIME_PICKER);
                break;
            case R.id.bt_end_date_edit_agenda:
                DatePickerFragment datePickerFragment2 = new DatePickerFragment();
                datePickerFragment2.show(getSupportFragmentManager(), END_DATE_PICKER);
                break;
            case R.id.bt_end_time_edit_agenda:
                TimePickerFragment timePickerFragment1 = new TimePickerFragment();
                timePickerFragment1.show(getSupportFragmentManager(), END_TIME_PICKER);
                break;
            case R.id.bt_edit_agenda:
                editAgenda();
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
                etStartDateAgenda.setText(dateFormat.format(calendar.getTime()));
                break;
            case END_DATE_PICKER:
                etEndDateAgenda.setText(dateFormat.format(calendar.getTime()));
                break;
            default:
                break;
        }
    }

    @Override
    public void onDialogTimeSet(String tag, int hourDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourDay);
        calendar.set(Calendar.MINUTE, minute);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        switch (tag) {
            case START_TIME_PICKER:
                etStartTimeAgenda.setText(dateFormat.format(calendar.getTime()));
                break;
            case END_TIME_PICKER:
                etEndTimeAgenda.setText(dateFormat.format(calendar.getTime()));
            default:
                break;
        }
    }

    public void editAgenda() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.3.5/myAPI/public/update-agenda/"+id_agenda;

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
                Map<String, String> data = new HashMap<String, String>();
                data.put("name", etNameAgenda.getText().toString());
                data.put("description", etDescAgenda.getText().toString());
                data.put("event_session_id", id_session);
                start = etStartDateAgenda.getText().toString() + " " + etStartTimeAgenda.getText().toString();
                end = etEndDateAgenda.getText().toString() + " " + etEndTimeAgenda.getText().toString();
                data.put("start", start);
                data.put("end", end);
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
}
