package com.example.digimiceconferent.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.digimiceconferent.MyUrl;
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
    ProgressDialog dialog;

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

        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Memproses...");
        dialog.setCancelable(false);

        if (!flashCamera) {
            flashCamera = true;
        } else {
            flashCamera = false;
        }

        btSetQr.setOnClickListener(new View.OnClickListener() {
            private long lastClick = 0;
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - lastClick < 1000) {
                    return;
                }
                lastClick = SystemClock.elapsedRealtime();

                boolean isEmpty = false;
                String email = etEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    etEmail.setError("Email tidak boleh kosong");
                    isEmpty = true;
                }

                if (!isEmpty) {
                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                    if (etEmail.getText().toString().trim().matches(emailPattern)) {
                        if (getQrCode != null) {
                            showDialog(true);
                            setQr(getQrCode);
                        }else {
                            Toast.makeText(getContext(), "Scan Qr Code Dahulu", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        etEmail.setError("Email tidak valid");
                    }

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
        Toast.makeText(getContext(), "QR Code berhasil disimpan", Toast.LENGTH_SHORT).show();

    }

    private void setQr(final String qrCode) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = MyUrl.URL+"/set-qrcode";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showDialog(false);
                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                getQrCode = null;
                etEmail.setText(null);
                resumeScan();
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
                data.put("email", etEmail.getText().toString());
                data.put("kode_qr", qrCode);
                data.put("event_id", sharedPrefManager.getSpIdEvent());
                data.put("session_id", sharedPrefManager.getSpIdSession());
                data.put("token", sharedPrefManager.getSPToken());
                return data;
            }
        };
        queue.getCache().clear();
        queue.add(request);
    }

    private void showDialog(Boolean state) {

        if (state) {
            dialog.show();
        } else {
            dialog.dismiss();
        }
    }

    private void resumeScan(){
        scannerView.resumeCameraPreview(this);
    }

}
