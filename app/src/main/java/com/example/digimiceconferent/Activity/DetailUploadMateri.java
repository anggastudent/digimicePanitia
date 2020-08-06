package com.example.digimiceconferent.Activity;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.digimiceconferent.Adapter.RecyclerViewMateriAdapter;
import com.example.digimiceconferent.Fragment.UploadMateriDialogFragment;
import com.example.digimiceconferent.MainViewModel;
import com.example.digimiceconferent.Model.Agenda;
import com.example.digimiceconferent.Model.Materi;
import com.example.digimiceconferent.R;
import com.example.digimiceconferent.SharedPrefManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DetailUploadMateri extends AppCompatActivity {
    public static final String EXTRA_MATERI = "materi";
    TextView tvSession, tvAgenda, tvTime;
    RecyclerView rvMateri;
    RecyclerViewMateriAdapter adapter;
    RequestQueue queue;
    FloatingActionButton addMateri;
    LinearLayout noDataPage;
    ProgressBar loading;
    SwipeRefreshLayout swipeMateri;

    private int PICK_PDF_REQUEST = 1;
    File file;
    String filePdf;
    Uri filePath;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_upload_materi);

        tvSession = findViewById(R.id.session_event_agenda_materi);
        tvAgenda = findViewById(R.id.name_event_agenda_materi);
        tvTime = findViewById(R.id.tanggal_event_agenda_materi);
        addMateri = findViewById(R.id.add_materi);
        noDataPage = findViewById(R.id.no_data_materi);
        loading = findViewById(R.id.loading_materi);
        swipeMateri = findViewById(R.id.swipe_detail_upload);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setTitle("Detail Upload Materi");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        queue = Volley.newRequestQueue(this);
        sharedPrefManager = new SharedPrefManager(this);
        rvMateri = findViewById(R.id.rv_materi);
        adapter = new RecyclerViewMateriAdapter();
        rvMateri.setLayoutManager(new LinearLayoutManager(this));

        showLoading(true);

        showData();
        swipeMateri.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeMateri.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showData();
                swipeMateri.setRefreshing(false);
            }
        });

        rvMateri.setAdapter(adapter);
        rvMateri.setHasFixedSize(true);
        adapter.notifyDataSetChanged();

        addMateri.setOnClickListener(new View.OnClickListener() {
            private long lastClick = 0;
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - lastClick < 1000) {
                    return;
                }
                lastClick = SystemClock.elapsedRealtime();

                AlertDialog.Builder builder = new AlertDialog.Builder(DetailUploadMateri.this);
                builder.setMessage("Apakah berkas sudah di Penyimpanan Internal ?");
                builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UploadMateriDialogFragment fragment = new UploadMateriDialogFragment();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragment.show(fragmentManager, UploadMateriDialogFragment.class.getSimpleName());
                    }
                });

                builder.setNegativeButton("Tidak", null);
                builder.show();

            }
        });

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

    private void showEmpty(Boolean state) {
        if (state) {
            noDataPage.setVisibility(View.VISIBLE);
        } else {
            noDataPage.setVisibility(View.GONE);
        }
    }

    private void showLoading(Boolean state) {
        if (state) {
            loading.setVisibility(View.VISIBLE);
        }else{
            loading.setVisibility(View.GONE);
        }
    }

    private void showData() {
        MainViewModel mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
        final Agenda agenda = getIntent().getParcelableExtra(EXTRA_MATERI);
        if (agenda != null) {
            sharedPrefManager.saveSPString(sharedPrefManager.SP_ID_AGENDA, agenda.getId());
            tvSession.setText(agenda.getSessionAgenda());
            tvAgenda.setText(agenda.getNamaAgenda());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date dateStart = dateFormat.parse(agenda.getStartAgenda());
                Date dateEnd = dateFormat.parse(agenda.getEndAgenda());
                SimpleDateFormat dateFormatNew = new SimpleDateFormat("dd MMMM yyyy HH:mm");
                tvTime.setText(dateFormatNew.format(dateStart)+"\n"+dateFormatNew.format(dateEnd));


            } catch (ParseException e) {
                e.printStackTrace();
            }

            mainViewModel.setListMateri(queue, this, agenda.getId(), sharedPrefManager.getSPToken());
            mainViewModel.getMateri().observe(this, new Observer<ArrayList<Materi>>() {
                @Override
                public void onChanged(ArrayList<Materi> materis) {
                    if (materis != null) {
                        adapter.sendData(materis);
                        showLoading(false);
                        showEmpty(false);
                    }
                    if(materis.size() == 0){
                        showLoading(false);
                        showEmpty(true);
                    }

                }
            });
        }
    }
}
