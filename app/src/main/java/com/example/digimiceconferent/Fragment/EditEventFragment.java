package com.example.digimiceconferent.Fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.digimiceconferent.R;
import com.example.digimiceconferent.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditEventFragment extends Fragment implements View.OnClickListener {

    EditText etNameEvent,etDescEvent, etPlaceEvent, etAddressEvent, etPriceEvent;
    Button btStartDate, btEndDate,btEditPaket, btaddImg;
    DatePickerFragment.DialogDateListener listener;
    static EditText etStartDateEvent;
    static EditText etEndDateEvent;
    ProgressBar loading;
    SharedPrefManager sharedPrefManager;

    ProgressDialog prosesDialog;

    ImageView imgBanner;

    static String startDate;
    static String endDate;
    String imageString;
    int PICK_IMAGE_REQUEST = 151;
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
        loading = view.findViewById(R.id.loading_edit_event);


        btEditPaket = view.findViewById(R.id.bt_edit_paket);
        btaddImg = view.findViewById(R.id.add_edit_img_banner_event);
        btStartDate = view.findViewById(R.id.bt_edit_start_date);
        btEndDate = view.findViewById(R.id.bt_edit_end_date);

        etStartDateEvent.setText(startDate);
        etEndDateEvent.setText(endDate);

        showLoading(true);

        btStartDate.setOnClickListener(this);
        btEndDate.setOnClickListener(this);
        btaddImg.setOnClickListener(this);
        btEditPaket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prosesDialog.setMessage("Memproses");
                prosesDialog.show();
                sendEdit();
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_edit_start_date:
                DialogFragment fragmentStart = new DatePicker();
                fragmentStart.show(getActivity().getSupportFragmentManager(), "start");
                break;

            case R.id.bt_edit_end_date:
                DialogFragment fragmentEnd = new DatePicker();
                fragmentEnd.show(getActivity().getSupportFragmentManager(), "end");
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
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imgBanner.setImageBitmap(bitmap);
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
            return new DatePickerDialog(getActivity(),this, year, month, day);
        }
    }

    private void showDataEdit() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "http://192.168.3.5/myAPI/public/edit-event/"+sharedPrefManager.getSpIdEvent();

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

                        Glide.with(getContext())
                                .load("http://192.168.3.5/myAPI/public/" + data.getString("banner"))
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
        });

        queue.add(jsonArrayRequest);
    }

    private void sendEdit() {
        RequestQueue queue = Volley.newRequestQueue(getContext());

        String url = "http://192.168.3.5/myAPI/public/update-event/"+sharedPrefManager.getSpIdEvent();

        if(bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        }

        StringRequest request = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                prosesDialog.dismiss();
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
                if(imageString != null) {
                    data.put("banner", imageString);
                }
                data.put("event_ticket_price", etPriceEvent.getText().toString());
                return data;
            }
        };

        queue.add(request);
    }

    private void showLoading(Boolean state) {
        if (state) {
            loading.setVisibility(View.VISIBLE);
        } else {
            loading.setVisibility(View.GONE);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        showDataEdit();
    }
}
