package com.example.digimiceconferent.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.digimiceconferent.FilePath;
import com.example.digimiceconferent.MyUrl;
import com.example.digimiceconferent.R;
import com.example.digimiceconferent.SharedPrefManager;
import com.kishan.askpermission.AskPermission;
import com.kishan.askpermission.ErrorCallback;
import com.kishan.askpermission.PermissionCallback;
import com.kishan.askpermission.PermissionInterface;

import java.io.File;
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
    ProgressDialog dialog;

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
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Memproses..");
        permissions();

        btUpload.setOnClickListener(new View.OnClickListener() {
            private long lastClick = 0;
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - lastClick < 1000) {
                    return;
                }
                lastClick = SystemClock.elapsedRealtime();

                showDialog(true);
                uploadPdf();

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
        String url = MyUrl.URL+"/upload-materi";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("pdf", filePdf);
                data.put("name", etNameFile.getText().toString());
                data.put("event_id", sharedPrefManager.getSpIdEvent());
                data.put("event_agenda_id", sharedPrefManager.getSpIdAgenda());
                return data;
            }

        };
        queue.getCache().clear();
        queue.add(request);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_PDF_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null){

            filePath = data.getData();
            String path = FilePath.getPath(getContext(),filePath);
            if (path != null) {
                file = new File(path);
                int size = (int) file.length();
                if (size > 2000000) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Berkas melebihi 2 MB.");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getDialog().dismiss();
                        }
                    });
                    builder.show();
                }
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Berkas tidak ada di Penyimpanan Internal.");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getDialog().dismiss();
                    }
                });
                builder.show();
            }
        }else{
            Toast.makeText(getContext(),"Materi tidak disimpan", Toast.LENGTH_SHORT).show();
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

    private void showDialog(Boolean state) {

        if (state) {
            dialog.show();
        } else {
            dialog.dismiss();
        }
    }

    private void uploadPdf() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String name = etNameFile.getText().toString();
        String path = FilePath.getPath(getContext(), filePath);
        String url = MyUrl.URL+"/upload-materi";

        SimpleMultiPartRequest request = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                showDialog(false);
                getDialog().dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showDialog(false);
                Toast.makeText(getContext(), "Upload Gagal", Toast.LENGTH_SHORT).show();
                getDialog().dismiss();
            }
        });

        request.addFile("pdf", path);
        request.addStringParam("name", name);
        request.addStringParam("event_id", sharedPrefManager.getSpIdEvent());
        request.addStringParam("event_agenda_id", sharedPrefManager.getSpIdAgenda());
        queue.add(request);

    }
}
