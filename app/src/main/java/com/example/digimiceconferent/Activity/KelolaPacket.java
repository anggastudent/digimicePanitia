package com.example.digimiceconferent.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.digimiceconferent.Fragment.DatePickerFragment;
import com.example.digimiceconferent.Fragment.TimePickerFragment;
import com.example.digimiceconferent.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class KelolaPacket extends AppCompatActivity implements View.OnClickListener, DatePickerFragment.DialogDateListener, TimePickerFragment.DialogTimeListener {

    EditText etNameEvent,etDescEvent, etPlaceEvent, etAddressEvent, etStartDateEvent,
            etStartTimeEvent, etEndDateEvent, etEndTimeEvent, etPriceEvent;
    Button btStartDate, btStartTime, btEndDate, btEndTime,btPilihPaket, btaddImg;
    ImageView imgBanner;

    final String START_DATE_PICKER = "start date picker";
    final String START_TIME_PICKER = "start time picker";
    final String END_DATE_PICKER = "end date picker";
    final String END_TIME_PICKER = "end time picker";

    int PICK_IMAGE_REQUEST = 111;
    Bitmap bitmap;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelola_packet);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Kelola Paket");
        }
        imgBanner = findViewById(R.id.img_banner);

        etStartDateEvent = findViewById(R.id.start_event_kelola);
        etStartTimeEvent = findViewById(R.id.start_time_event_kelola);
        etEndDateEvent = findViewById(R.id.end_event_kelola);
        etEndTimeEvent = findViewById(R.id.end_time_event_kelola);

        btaddImg = findViewById(R.id.add_img_banner_event);
        btStartDate = findViewById(R.id.bt_start_date);
        btStartTime = findViewById(R.id.bt_start_time);
        btEndDate = findViewById(R.id.bt_end_date);
        btEndTime = findViewById(R.id.bt_end_time);

        btStartDate.setOnClickListener(this);
        btStartTime.setOnClickListener(this);
        btEndDate.setOnClickListener(this);
        btEndTime.setOnClickListener(this);
        btaddImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_start_date:
                DatePickerFragment datePickerFragmentStartDate = new DatePickerFragment();
                datePickerFragmentStartDate.show(getSupportFragmentManager(), START_DATE_PICKER);
                break;
            case R.id.bt_start_time:
                TimePickerFragment timePickerFragmentStartTime = new TimePickerFragment();
                timePickerFragmentStartTime.show(getSupportFragmentManager(), START_TIME_PICKER);
                break;
            case R.id.bt_end_date:
                DatePickerFragment datePickerFragmentEndDate = new DatePickerFragment();
                datePickerFragmentEndDate.show(getSupportFragmentManager(), END_DATE_PICKER);
                break;
            case R.id.bt_end_time:
                TimePickerFragment timePickerFragmentEndTime = new TimePickerFragment();
                timePickerFragmentEndTime.show(getSupportFragmentManager(), END_TIME_PICKER);
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
    public void onDialogTimeSet(String tag, int hourDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourDay);
        calendar.set(Calendar.MINUTE, minute);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        switch (tag) {
            case START_TIME_PICKER:
                etStartTimeEvent.setText(dateFormat.format(calendar.getTime()));
                break;
            case END_TIME_PICKER:
                etEndTimeEvent.setText(dateFormat.format(calendar.getTime()));
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
}
