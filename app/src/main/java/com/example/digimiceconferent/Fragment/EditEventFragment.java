package com.example.digimiceconferent.Fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.digimiceconferent.MyUrl;
import com.example.digimiceconferent.R;
import com.example.digimiceconferent.SharedPrefManager;
import com.oginotihiro.cropview.CropView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditEventFragment extends Fragment implements View.OnClickListener {

    EditText etNameEvent,etDescEvent, etPlaceEvent, etAddressEvent, etPriceEvent, etPanitia;
    Button btStartDate, btEndDate,btEditPaket, btaddImg, btCrop, btCancelCrop;
    DatePickerFragment.DialogDateListener listener;
    static EditText etStartDateEvent;
    static EditText etEndDateEvent;
    ProgressBar loading;
    SharedPrefManager sharedPrefManager;
    CropView cropView;
    LinearLayout cropPage;

    ProgressDialog prosesDialog;

    ImageView imgBanner;

    static String startDate;
    static String endDate;
    String imageString;
    int PICK_IMAGE_REQUEST = 11;
    Bitmap bitmap;


    public EditEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPrefManager = new SharedPrefManager(getContext());
        prosesDialog = new ProgressDialog(getContext());
        imgBanner = view.findViewById(R.id.edit_img_banner);
        etNameEvent = view.findViewById(R.id.edit_name_event_kelola);
        etDescEvent = view.findViewById(R.id.edit_deskripsi_kelola);
        etPlaceEvent = view.findViewById(R.id.edit_tempat_event_kelola);
        etAddressEvent = view.findViewById(R.id.edit_alamat_event_kelola);
        etPriceEvent = view.findViewById(R.id.edit_price_event_kelola);
        etStartDateEvent = view.findViewById(R.id.edit_start_event_kelola);
        etEndDateEvent = view.findViewById(R.id.edit_end_event_kelola);
        etPanitia = view.findViewById(R.id.edit_team_event_kelola);
        loading = view.findViewById(R.id.loading_edit_event);
        cropPage = view.findViewById(R.id.page_crop_edit_event);
        cropView = view.findViewById(R.id.crop_view_edit_event);
        btCrop = view.findViewById(R.id.bt_crop_edit_event);
        btCancelCrop = view.findViewById(R.id.bt_cancel_crop_edit_event);


        btEditPaket = view.findViewById(R.id.bt_edit_paket);
        btaddImg = view.findViewById(R.id.add_edit_img_banner_event);
        btStartDate = view.findViewById(R.id.bt_edit_start_date);
        btEndDate = view.findViewById(R.id.bt_edit_end_date);

        etStartDateEvent.setText(startDate);
        etStartDateEvent.setEnabled(false);
        etEndDateEvent.setText(endDate);
        etEndDateEvent.setEnabled(false);

        if (sharedPrefManager.getSPRole().equals("eo")) {
            etPanitia.setEnabled(false);
        }

        showLoading(true);
        showDataEdit();
        btStartDate.setOnClickListener(this);
        btEndDate.setOnClickListener(this);
        btaddImg.setOnClickListener(this);
        btEditPaket.setOnClickListener(new View.OnClickListener() {
            private long lastClick = 0;
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - lastClick < 1000) {
                    return;
                }
                lastClick = SystemClock.elapsedRealtime();
                boolean isEmpty = false;
                String name = etNameEvent.getText().toString();
                String desc = etDescEvent.getText().toString();
                String place = etPlaceEvent.getText().toString();
                String address = etAddressEvent.getText().toString();
                String price = etPriceEvent.getText().toString();
                String getStart = etStartDateEvent.getText().toString();
                String getEnd = etEndDateEvent.getText().toString();
                String panitia = etPanitia.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    isEmpty = true;
                    etNameEvent.setError("Nama tidak boleh kosong");
                }

                if (TextUtils.isEmpty(panitia)) {
                    isEmpty = true;
                    etPanitia.setError("Nama panitia tidak boleh kosong");
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
                if (TextUtils.isEmpty(getStart)) {
                    isEmpty = true;
                    etStartDateEvent.setError("Tanggal tidak boleh kosong");
                }
                if (TextUtils.isEmpty(getEnd)) {
                    isEmpty = true;
                    etEndDateEvent.setError("Tanggal tidak boleh kosong");
                }
                if (TextUtils.isEmpty(price)) {
                    isEmpty = true;
                    etPriceEvent.setError("Harga tiket tidak boleh kosong");
                }

                if (Integer.parseInt(etPriceEvent.getText().toString()) < 10000 && Integer.parseInt(etPriceEvent.getText().toString()) > 0) {
                    isEmpty = true;
                    etPriceEvent.setError("Harga tiket minimal Rp. 10.000");
                }

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String start = etStartDateEvent.getText().toString();
                String end = etEndDateEvent.getText().toString();
                try {
                    Date dateStart = dateFormat.parse(start);
                    Date dateEnd = dateFormat.parse(end);
                    if (dateEnd.before(dateStart) && !dateEnd.equals(dateStart)) {
                        isEmpty = true;
                        //etEndDateEvent.setError("Tanggal harus lebih dari start");
                        Toast.makeText(getContext(),"Tanggal end event harus lebih dari start event", Toast.LENGTH_SHORT).show();

                    }

                    if (!isEmpty) {
                        prosesDialog.setMessage("Memproses...");
                        prosesDialog.setCancelable(false);
                        prosesDialog.show();
                        sendEdit();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private long lastClick = 0;
    @Override
    public void onClick(View v) {
        if (SystemClock.elapsedRealtime() - lastClick < 1000) {
            return;
        }
        lastClick = SystemClock.elapsedRealtime();

        switch (v.getId()) {
            case R.id.bt_edit_start_date:
                DialogFragment fragmentStart = new DatePicker();
                fragmentStart.show(getChildFragmentManager(), "start");
                break;

            case R.id.bt_edit_end_date:
                DialogFragment fragmentEnd = new DatePicker();
                fragmentEnd.show(getChildFragmentManager(), "end");
                break;

            case R.id.add_edit_img_banner_event:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Image"),PICK_IMAGE_REQUEST);
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            showCrop(true);
            Uri filePath = data.getData();
            try {
                //bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);
                cropView.of(filePath)
                        .withAspect(4,3)
                        .withOutputSize(400,300)
                        .initialize(getContext());

                btCrop.setOnClickListener(new View.OnClickListener() {
                    private long lastClick = 0;
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - lastClick < 1000) {
                            return;
                        }
                        lastClick = SystemClock.elapsedRealtime();
                        bitmap = cropView.getOutput();
                        imgBanner.setImageBitmap(bitmap);
                        showCrop(false);
                    }
                });

                btCancelCrop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showCrop(false);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public static class DatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            switch (getTag()) {
                case "start":
                    etStartDateEvent.setText(dateFormat.format(calendar.getTime()));
                    break;
                case "end":
                    etEndDateEvent.setText(dateFormat.format(calendar.getTime()));
                    break;
            }
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getContext(),this, year, month, day);
        }
    }

    private void showDataEdit() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = MyUrl.URL+"/edit-event";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject data = response.getJSONObject(i);
                        etNameEvent.setText(data.getString("name"));
                        etDescEvent.setText(data.getString("description"));
                        etPlaceEvent.setText(data.getString("place"));
                        etAddressEvent.setText(data.getString("address"));
                        etStartDateEvent.setText(data.getString("start"));
                        etEndDateEvent.setText(data.getString("end"));
                        etPanitia.setText(data.getString("panitia"));

                        Glide.with(getContext())
                                .load(MyUrl.URL+"/" + data.getString("banner"))
                                .apply(new RequestOptions().override(100, 100))
                                .into(imgBanner);

                        if (Integer.parseInt(data.getString("price"))==0) {
                            etPriceEvent.setEnabled(false);
                            etPriceEvent.setText("0");
                        }else{
                            etPriceEvent.setText(data.getString("ticket"));
                        }

                    }

                    showLoading(false);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("event_id", sharedPrefManager.getSpIdEvent());
                data.put("user_id", sharedPrefManager.getSPIdUser());
                data.put("token", sharedPrefManager.getSPToken());
                return data;
            }
        };
        queue.getCache().clear();
        queue.add(jsonArrayRequest);
    }

    private void sendEdit() {
        RequestQueue queue = Volley.newRequestQueue(getContext());

        String url = MyUrl.URL+"/update-event/"+sharedPrefManager.getSpIdEvent();

        if(bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        }

        StringRequest request = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                etEndDateEvent.setError(null);
                etPriceEvent.setError(null);
                prosesDialog.dismiss();
                showDataEdit();
                Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                prosesDialog.dismiss();
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<String, String>();
                data.put("name", etNameEvent.getText().toString());
                data.put("description", etDescEvent.getText().toString());
                data.put("place", etPlaceEvent.getText().toString());
                data.put("address", etAddressEvent.getText().toString());
                data.put("start", etStartDateEvent.getText().toString());
                data.put("end", etEndDateEvent.getText().toString());
                data.put("panitia", etPanitia.getText().toString());
                data.put("user_id", sharedPrefManager.getSPIdUser());
                if(imageString != null) {
                    data.put("banner", imageString);
                }
                data.put("event_ticket_price", etPriceEvent.getText().toString());
                data.put("token", sharedPrefManager.getSPToken());
                return data;
            }
        };
        queue.getCache().clear();
        queue.add(request);
    }

    private void showLoading(Boolean state) {
        if (state) {
            loading.setVisibility(View.VISIBLE);
        } else {
            loading.setVisibility(View.GONE);
        }
    }

    private void showCrop(Boolean state) {
        if (state) {
            cropPage.setVisibility(View.VISIBLE);
        } else {
            cropPage.setVisibility(View.GONE);
        }
    }
}
