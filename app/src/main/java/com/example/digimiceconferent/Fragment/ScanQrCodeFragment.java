package com.example.digimiceconferent.Fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.digimiceconferent.Activity.KelolaPeserta;
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
public class ScanQrCodeFragment extends Fragment implements ZXingScannerView.ResultHandler {

    private ZXingScannerView scannerView;
    private boolean frontCamera = true;
    private boolean flashCamera = false;
    SharedPrefManager sharedPrefManager;
    ProgressDialog dialog;

    public ScanQrCodeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_scan_qr_code, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewGroup viewGroup = view.findViewById(R.id.camera_smartphone);
        scannerView = new ZXingScannerView(getContext());
        viewGroup.addView(scannerView);

        sharedPrefManager = new SharedPrefManager(getContext());
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Memproses...");
        dialog.setCancelable(false);
        if (!flashCamera) {
            flashCamera = true;
        } else {
            flashCamera = false;
        }

    }

    @Override
    public void handleResult(Result rawResult) {

        //Toast.makeText(getContext(), rawResult.getText(), Toast.LENGTH_SHORT).show();
        showDialog(true);
        scanQr(rawResult.getText());
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Apakah ingin Absen Lagi?");
        builder.setPositiveButton("Oke", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                resumeScan();
            }
        });

        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getContext(), KelolaPeserta.class);
                startActivity(intent);
            }
        });

        builder.show();

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

        inflater.inflate(R.menu.option_menu_scan_smartphone, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.change_camera_smartphone:

                if (frontCamera) {
                    scannerView.stopCamera();
                    scannerView.startCamera(1);
                    frontCamera = false;
                } else {
                    scannerView.stopCamera();
                    scannerView.startCamera(-1);
                    frontCamera = true;
                }

                break;
            case R.id.flash_camera_smartphone:

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

    private void scanQr(final String qrCode) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = MyUrl.URL+"/scan-qrcode";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showDialog(false);
                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showDialog(false);
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();

                data.put("qr_code", qrCode);
                data.put("session_id", sharedPrefManager.getSpIdSession());
                data.put("event_id", sharedPrefManager.getSpIdEvent());
                return data;
            }
        };
        queue.getCache().clear();
        queue.add(request);
    }

    private void resumeScan(){
        scannerView.resumeCameraPreview(this);
    }

    private void showDialog(Boolean state) {

        if (state) {
            dialog.show();
        } else {
            dialog.dismiss();
        }
    }
}
