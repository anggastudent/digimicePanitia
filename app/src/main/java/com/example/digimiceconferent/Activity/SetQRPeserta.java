package com.example.digimiceconferent.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.digimiceconferent.R;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class SetQRPeserta extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView scannerView;
    private boolean frontCamera = true;
    private boolean flashCamera = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_q_r_peserta);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Set QR Code Peserta");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        scannerView = new ZXingScannerView(this);
        ViewGroup viewGroup = findViewById(R.id.camera_set_qr);
        viewGroup.addView(scannerView);

        if (!flashCamera) {
            flashCamera = true;
        } else {
            flashCamera = false;
        }

    }

    @Override
    public void handleResult(Result rawResult) {
        Toast.makeText(getApplicationContext(), rawResult.toString(), Toast.LENGTH_SHORT).show();
        scannerView.resumeCameraPreview(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu_scan_smartphone, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.change_camera_smartphone:
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
}
