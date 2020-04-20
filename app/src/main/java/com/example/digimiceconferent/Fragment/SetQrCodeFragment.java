package com.example.digimiceconferent.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.digimiceconferent.R;
import com.example.digimiceconferent.SharedPrefManager;
import com.google.zxing.Result;

import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SetQrCodeFragment extends Fragment implements ZXingScannerView.ResultHandler {

    private ZXingScannerView scannerView;
    private boolean frontCamera = true;
    private boolean flashCamera = false;
    EditText etEmail;
    Button btSetQr;
    SharedPrefManager sharedPrefManager;
    String getQrCode;

    public SetQrCodeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_set_qr_code, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewGroup viewGroup = view.findViewById(R.id.camera_set_qr);
        etEmail = view.findViewById(R.id.email_set_qr);
        btSetQr = view.findViewById(R.id.bt_set_qr);
        scannerView = new ZXingScannerView(getContext());
        sharedPrefManager = new SharedPrefManager(getContext());
        viewGroup.addView(scannerView);



        if (!flashCamera) {
            flashCamera = true;
        } else {
            flashCamera = false;
        }

        btSetQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getQrCode != null) {
                    setQr(getQrCode);
                }else {
                    Toast.makeText(getContext(), "Scan Qr Code Dahulu", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();

    }

    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.option_menu_scan_card, menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.change_camera_card:
                if (frontCamera) {
                    scannerView.stopCamera();
                    scannerView.startCamera(1);
                    frontCamera = false;
                }else{
                    scannerView.stopCamera();
                    scannerView.startCamera(-1);
                    frontCamera = true;
                }
                break;
            case R.id.flash_camera_card:
                if (flashCamera) {
                    scannerView.setFlash(true);
                    flashCamera = false;
                } else {
                    scannerView.setFlash(false);
                    flashCamera = true;
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void handleResult(Result rawResult) {

        getQrCode = rawResult.getText();
        scannerView.resumeCameraPreview(this);
        Toast.makeText(getContext(), "QR Code berhasil disimpan", Toast.LENGTH_SHORT).show();

    }

    private void setQr(final String qrCode) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "http://192.168.4.109/myAPI/public/set-qrcode";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                getQrCode = null;
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
                data.put("email", etEmail.getText().toString());
                data.put("kode_qr", qrCode);
                data.put("event_id", sharedPrefManager.getSpIdEvent());
                data.put("session_id", sharedPrefManager.getSpIdSession());
                return data;
            }
        };

        queue.add(request);
    }


}
