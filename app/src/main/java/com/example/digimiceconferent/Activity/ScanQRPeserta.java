package com.example.digimiceconferent.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.digimiceconferent.R;
import com.example.digimiceconferent.SectionPagerAdapter;
import com.example.digimiceconferent.SectionPagerScanAdapter;
import com.google.android.material.tabs.TabLayout;
import com.kishan.askpermission.AskPermission;
import com.kishan.askpermission.ErrorCallback;
import com.kishan.askpermission.PermissionCallback;
import com.kishan.askpermission.PermissionInterface;

import java.security.acl.Permission;

public class ScanQRPeserta extends AppCompatActivity implements PermissionCallback, ErrorCallback {
    private static final int REQUEST_PERMISSIONS = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_q_r_peserta);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Scan QRCode");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        SectionPagerScanAdapter sectionPagerAdapter = new SectionPagerScanAdapter(getSupportFragmentManager(),this);
        ViewPager viewPager = findViewById(R.id.view_pager_scan);
        viewPager.setAdapter(sectionPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tab_scan);
        tabLayout.setupWithViewPager(viewPager);
        getSupportActionBar().setElevation(0);

        permission();
    }

    private void permission() {
        new AskPermission.Builder(this).setPermissions(
                Manifest.permission.CAMERA)
                .setCallback(this)
                .setErrorCallback(this)
                .request(REQUEST_PERMISSIONS);

    }

    @Override
    public void onShowRationalDialog(final PermissionInterface permissionInterface, int requestCode) {
//
        permissionInterface.onDialogShown();
    }

    @Override
    public void onShowSettings(final PermissionInterface permissionInterface, int requestCode) {

    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    @Override
    public void onPermissionsDenied(int requestCode) {
        Intent intent = new Intent(getApplicationContext(), KelolaPeserta.class);
        startActivity(intent);
    }

}
