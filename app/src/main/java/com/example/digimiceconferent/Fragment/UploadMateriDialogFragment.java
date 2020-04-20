package com.example.digimiceconferent.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Base64;
import android.view.LayoutInflater;
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
import com.kishan.askpermission.AskPermission;
import com.kishan.askpermission.ErrorCallback;
import com.kishan.askpermission.PermissionCallback;
import com.kishan.askpermission.PermissionInterface;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadMateriDialogFragment extends DialogFragment implements PermissionCallback, ErrorCallback {

    Button btUpload, btCancel;
    EditText etNameFile;
    RequestQueue queue;
    SharedPrefManager sharedPrefManager;
    private int PICK_PDF_REQUEST = 1;
    private int REQUEST_PERMISSIONS = 2;
    File file;
    String filePdf;
    Uri filePath;

    public UploadMateriDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upload_materi_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btUpload = view.findViewById(R.id.bt_upload_materi);
        btCancel = view.findViewById(R.id.bt_cancel_materi);

        etNameFile = view.findViewById(R.id.nama_upload_materi);
        queue = Volley.newRequestQueue(getContext());
        sharedPrefManager = new SharedPrefManager(getContext());
        permissions();

        btUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadFile();
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
    }
    private void addMateri() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST);
    }

    public void uploadFile() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "http://192.168.4.109/myAPI/public/upload-materi";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                getDialog().dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                getDialog().dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("pdf", filePdf);
                data.put("name", etNameFile.getText().toString());
                data.put("event_id", sharedPrefManager.getSpIdEvent());
                data.put("event_agenda_id", sharedPrefManager.getSpIdAgenda());
                data.put("event_event_type_id", "3");
                return data;
            }
        };

        queue.add(request);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_PDF_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null){
            filePath = data.getData();
            file = new File(Environment.getExternalStorageDirectory(),filePath.getPath().substring(20));

            int size = (int) file.length();
            byte[] bytes = new byte[size];
            try {
                BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
                bufferedInputStream.read(bytes,0, bytes.length);
                bufferedInputStream.close();
                filePdf = Base64.encodeToString(bytes, Base64.DEFAULT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            getDialog().dismiss();
        }
    }

    @Override
    public void onShowRationalDialog(PermissionInterface permissionInterface, int requestCode) {
        permissionInterface.onDialogShown();
    }

    @Override
    public void onShowSettings(PermissionInterface permissionInterface, int requestCode) {

    }

    @Override
    public void onPermissionsGranted(int requestCode) {
        addMateri();
    }

    @Override
    public void onPermissionsDenied(int requestCode) {
        getDialog().dismiss();
    }

    private void permissions() {
        new AskPermission.Builder(this).setPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE)
                .setCallback(this)
                .setErrorCallback(this)
                .request(REQUEST_PERMISSIONS);
    }
}
