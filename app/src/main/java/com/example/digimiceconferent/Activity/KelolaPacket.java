package com.example.digimiceconferent.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class KelolaPacket extends AppCompatActivity implements View.OnClickListener, DatePickerFragment.DialogDateListener{

    EditText etNameEvent,etDescEvent, etPlaceEvent, etAddressEvent, etStartDateEvent,
            etEndDateEvent, etPriceEvent, etSessionEvent;
    Button btStartDate, btEndDate,btPilihPaket, btaddImg;
    TextView tvNamePaket, tvMaxParticipant, tvPrice;
    ImageView imgBanner;
    RequestQueue queue;
    Spinner spPresensi;
    ProgressDialog dialog;
    final String START_DATE_PICKER = "start date picker";
    final String END_DATE_PICKER = "end date picker";

    String paket_id;
    String imageString;
    int price;
    int PICK_IMAGE_REQUEST = 111;
    Bitmap bitmap;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelola_packet);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Kelola Paket");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tvNamePaket = findViewById(R.id.name_paket_title);
        tvMaxParticipant = findViewById(R.id.max_participant_title);
        tvPrice = findViewById(R.id.price_title);

        spPresensi = findViewById(R.id.spinner_presensi);

        imgBanner = findViewById(R.id.img_banner);
        etNameEvent = findViewById(R.id.name_event_kelola);
        etDescEvent = findViewById(R.id.deskripsi_kelola);
        etPlaceEvent = findViewById(R.id.tempat_event_kelola);
        etAddressEvent = findViewById(R.id.alamat_event_kelola);
        etPriceEvent = findViewById(R.id.price_event_kelola);
        etStartDateEvent = findViewById(R.id.start_event_kelola);
        etEndDateEvent = findViewById(R.id.end_event_kelola);
        etSessionEvent = findViewById(R.id.session_event_kelola);

        btPilihPaket = findViewById(R.id.bt_pilih_paket);
        btaddImg = findViewById(R.id.add_img_banner_event);
        btStartDate = findViewById(R.id.bt_start_date);
        btEndDate = findViewById(R.id.bt_end_date);

        btStartDate.setOnClickListener(this);
        btEndDate.setOnClickListener(this);
        btaddImg.setOnClickListener(this);

        btPilihPaket.setOnClickListener(new View.OnClickListener() {
            private long lastClick = 0;
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - lastClick < 1000) {
                    return;
                }
                lastClick = SystemClock.elapsedRealtime();

                AlertDialog.Builder builder = new AlertDialog.Builder(KelolaPacket.this );
                builder.setMessage("Apakah anda sudah yakin ? ");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean isEmpty = false;
                        String name = etNameEvent.getText().toString();
                        String desc = etDescEvent.getText().toString();
                        String place = etPlaceEvent.getText().toString();
                        String address = etAddressEvent.getText().toString();
                        String startDate = etStartDateEvent.getText().toString();
                        String endDate = etEndDateEvent.getText().toString();
                        String price = etPriceEvent.getText().toString();
                        String session = etSessionEvent.getText().toString();

                        if (TextUtils.isEmpty(name)) {
                            isEmpty = true;
                            etNameEvent.setError("Nama tidak boleh kosong");
                        }

                        if (TextUtils.isEmpty(desc)) {
                            isEmpty = true;
                            etDescEvent.setError("Deskripsi tidak boleh kosong");
                        }
                        if (TextUtils.isEmpty(place)) {
                            isEmpty = true;
                            etPlaceEvent.setError("Tempat tidak boleh kosong");
                        }
                        if (TextUtils.isEmpty(address)) {
                            isEmpty = true;
                            etAddressEvent.setError("Alamat tidak boleh kosong");
                        }
                        if (TextUtils.isEmpty(startDate)) {
                            isEmpty = true;
                            etStartDateEvent.setError("Tanggal tidak boleh kosong");
                        }
                        if (TextUtils.isEmpty(endDate)) {
                            isEmpty = true;
                            etEndDateEvent.setError("Tanggal tidak boleh kosong");
                        }
                        if (TextUtils.isEmpty(price)) {
                            isEmpty = true;
                            etPriceEvent.setError("Harga tiket tidak boleh kosong");
                        }
                        if (TextUtils.isEmpty(session)) {
                            isEmpty = true;
                            etSessionEvent.setError("Nama sesi tidak boleh kosong");
                        }
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String start = etStartDateEvent.getText().toString();
                        String end = etEndDateEvent.getText().toString();
                        try {
                            Date dateStart = dateFormat.parse(start);
                            Date dateEnd = dateFormat.parse(end);
                            if (dateEnd.before(dateStart)) {
                                isEmpty = true;
                                etEndDateEvent.setError("Tanggal harus lebih dari start");
                            }

                            if (!isEmpty && dateEnd.equals(dateStart)) {
                                showProcess(true);
                                addEvent();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });

                builder.setNegativeButton("Tidak", null);
                builder.show();

            }
        });

        dialog = new ProgressDialog(KelolaPacket.this);
        dialog.setMessage("Memproses...");

        sharedPrefManager = new SharedPrefManager(this);
        tvNamePaket.setText(sharedPrefManager.getSpNamePacket());
        tvMaxParticipant.setText(sharedPrefManager.getSpMaxParticipant()+" Maksimal Peserta");
        tvPrice.setText("Rp. "+sharedPrefManager.getSpPricePacket());
        paket_id = sharedPrefManager.getSpIdPacket();
        price = Integer.parseInt(sharedPrefManager.getSpPricePacket());

        if (price == 0) {
            etPriceEvent.setEnabled(false);
            etPriceEvent.setText("0");
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
            case R.id.bt_start_date:
                DatePickerFragment datePickerFragmentStartDate = new DatePickerFragment();
                datePickerFragmentStartDate.show(getSupportFragmentManager(), START_DATE_PICKER);
                break;

            case R.id.bt_end_date:
                DatePickerFragment datePickerFragmentEndDate = new DatePickerFragment();
                datePickerFragmentEndDate.show(getSupportFragmentManager(), END_DATE_PICKER);
                break;

            case R.id.add_img_banner_event:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Image"),PICK_IMAGE_REQUEST);
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
                etStartDateEvent.setText(dateFormat.format(calendar.getTime()));
                break;
            case END_DATE_PICKER:
                etEndDateEvent.setText(dateFormat.format(calendar.getTime()));
                break;
            default:
                break;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imgBanner.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void addEvent(){
        queue = Volley.newRequestQueue(this);

        if(bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        }

        String url = MyUrl.URL+"/add-event";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                etEndDateEvent.setError(null);
                Toast.makeText(getApplicationContext(), "Berhasil",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(KelolaPacket.this, HomePanitia.class);
                startActivity(intent);
                showProcess(false);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Gagal",Toast.LENGTH_SHORT).show();

            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<String, String>();
                data.put("name", etNameEvent.getText().toString());
                data.put("start", etStartDateEvent.getText().toString());
                data.put("end", etEndDateEvent.getText().toString());
                data.put("event_type_id", "3");
                if (imageString != null) {
                    data.put("banner", imageString);
                }
                data.put("description", etDescEvent.getText().toString());
                data.put("place", etPlaceEvent.getText().toString());
                data.put("address", etAddressEvent.getText().toString());
                if(price==0) {
                    data.put("event_status", "true");
                }else {
                    data.put("event_status", "false");
                }
                data.put("presence_type", spPresensi.getSelectedItem().toString());
                data.put("event_paket_id", paket_id);
                data.put("event_ticket_price", etPriceEvent.getText().toString());
                data.put("user_id", sharedPrefManager.getSPIdUser());
                data.put("team_role", sharedPrefManager.getSPRole());
                data.put("name_team", sharedPrefManager.getSpNameTeam());
                data.put("name_session", etSessionEvent.getText().toString());
                data.put("name_packet", sharedPrefManager.getSpNamePacket());
                data.put("email", sharedPrefManager.getSpEmail());
                data.put("price_packet", String.valueOf(price));

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

    private void showProcess(Boolean state) {

        if (state) {
            dialog.show();
        }else{
            dialog.dismiss();
        }
    }
}
