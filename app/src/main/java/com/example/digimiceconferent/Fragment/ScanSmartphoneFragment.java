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
import android.widget.Toast;

import com.example.digimiceconferent.R;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScanSmartphoneFragment extends Fragment implements ZXingScannerView.ResultHandler{

    private ZXingScannerView scannerView;
    private boolean frontCamera = true;
    private boolean flashCamera = false;

    public ScanSmartphoneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_scan_smartphone, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewGroup viewGroup = view.findViewById(R.id.camera_smartphone);
        scannerView = new ZXingScannerView(getContext());
        viewGroup.addView(scannerView);

        if (!flashCamera) {
            flashCamera = true;
        } else {
            flashCamera = false;
        }

    }

    @Override
    public void handleResult(Result rawResult) {
        Toast.makeText(getContext(), rawResult.getText(), Toast.LENGTH_SHORT).show();
        scannerView.resumeCameraPreview(this);
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
