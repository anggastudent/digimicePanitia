package com.example.digimiceconferent.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import java.util.HashMap;
import java.util.Map;

public class EditProfil extends AppCompatActivity {
    EditText etNamaLengkap, etPasswordLama, etPasswordBaru, etRePasswordBaru;
    Button btUploadGambar, btEditProfil, btCrop, btCancelCrop;
    ImageView avatar;
    SharedPrefManager sharedPrefManager;
    ProgressBar loading;
    ProgressDialog dialog;
    CropView cropView;
    LinearLayout cropPage;

    int PICK_IMAGE_REQUEST = 22;
    String imageString;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Edit Profil");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

        etNamaLengkap = findViewById(R.id.name_user_edit);
        etPasswordLama = findViewById(R.id.old_password_user_edit);
        etPasswordBaru = findViewById(R.id.new_password_user_edit);
        etRePasswordBaru = findViewById(R.id.re_password_user_edit);
        loading = findViewById(R.id.loading_edit_profil);
        btUploadGambar = findViewById(R.id.bt_upload_avatar_user);
        btEditProfil = findViewById(R.id.bt_edit_user);
        cropView = findViewById(R.id.cropViewProfil);
        avatar = findViewById(R.id.avatar_user_edit);
        cropPage = findViewById(R.id.page_crop_profil);
        btCrop = findViewById(R.id.bt_crop_profil);
        btCancelCrop = findViewById(R.id.bt_cancel_crop_profil);

        dialog = new ProgressDialog(EditProfil.this);
        dialog.setMessage("Memproses...");
        dialog.setCancelable(false);
        showCrop(false);
        sharedPrefManager = new SharedPrefManager(this);
        showLoading(true);
        getData();
        btUploadGambar.setOnClickListener(new View.OnClickListener() {
            private long lastClick = 0;
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - lastClick < 1000) {
                    return;
                }
                lastClick = SystemClock.elapsedRealtime();

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
            }
        });

        btEditProfil.setOnClickListener(new View.OnClickListener() {
            private long lastClick = 0;
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - lastClick < 1000) {
                    return;
                }
                lastClick = SystemClock.elapsedRealtime();

                if (etPasswordBaru.getText().toString().length() < 8 && !etPasswordBaru.getText().toString().equals("")) {
                    etPasswordBaru.setError("Password minimal 8 karakter");
                }else{

                    if (!etPasswordBaru.getText().toString().equals(etRePasswordBaru.getText().toString())) {
                        etRePasswordBaru.setError("Password tidak sama");

                    }else {
                        showDialog(true);
                        editProfil();
                    }
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            final Uri filePath = data.getData();
            showCrop(true);
            try {
                //bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                cropView.of(filePath)
                        .withAspect(4,3)
                        .withOutputSize(400,300)
                        .initialize(this);

                btCrop.setOnClickListener(new View.OnClickListener() {
                    private long lastClick = 0;
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - lastClick < 1000) {
                            return;
                        }
                        lastClick = SystemClock.elapsedRealtime();
                        bitmap = cropView.getOutput();
                        avatar.setImageBitmap(bitmap);
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

    private void getData() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = MyUrl.URL+"/edit-user/"+sharedPrefManager.getSPIdUser();
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject data = response.getJSONObject(i);
                        etNamaLengkap.setText(data.getString("name"));
                        Glide.with(getApplicationContext())
                                .load(MyUrl.URL+"/" + data.getString("avatar"))
                                .apply(new RequestOptions().override(100, 100))
                                .into(avatar);
                    }

                    showLoading(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("token", sharedPrefManager.getSPToken());
                return data;
            }
        };
        queue.getCache().clear();
        queue.add(arrayRequest);
    }

    private void editProfil() {

        RequestQueue queue = Volley.newRequestQueue(this);

        if (bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        }
        String url = MyUrl.URL+"/update-user/"+sharedPrefManager.getSPIdUser();
        StringRequest request = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showDialog(false);
                etPasswordBaru.setText(null);
                etPasswordLama.setText(null);
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showDialog(false);
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("name", etNamaLengkap.getText().toString());
                data.put("old_password", etPasswordLama.getText().toString());
                data.put("new_password", etPasswordBaru.getText().toString());
                if(imageString != null) {
                    data.put("avatar", imageString);
                }
                data.put("token", sharedPrefManager.getSPToken());
                return data;
            }
        };
        queue.getCache().clear();
        queue.add(request);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLoading(Boolean state) {
        if (state) {
            loading.setVisibility(View.VISIBLE);
        } else {
            loading.setVisibility(View.GONE);
        }
    }

    private void showDialog(Boolean state) {

        if (state) {
            dialog.show();
        } else {
            dialog.dismiss();
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
