package com.example.digimiceconferent.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.HashMap;
import java.util.Map;

public class EditProfil extends AppCompatActivity {
    EditText etNamaLengkap, etPasswordLama, etPasswordBaru, etRePasswordBaru;
    Button btUploadGambar, btEditProfil;
    ImageView avatar;
    SharedPrefManager sharedPrefManager;
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

        btUploadGambar = findViewById(R.id.bt_upload_avatar_user);
        btEditProfil = findViewById(R.id.bt_edit_user);

        avatar = findViewById(R.id.avatar_user_edit);

        sharedPrefManager = new SharedPrefManager(this);

        getData();

        btUploadGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
            }
        });

        btEditProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etPasswordBaru.getText().toString().equals(etRePasswordBaru.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Password Baru Tidak Sama", Toast.LENGTH_SHORT).show();

                }else {
                    editProfil();
                }


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                avatar.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void getData() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.4.109/myAPI/public/edit-user/"+sharedPrefManager.getSPIdUser();
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject data = response.getJSONObject(i);
                        etNamaLengkap.setText(data.getString("name"));
                        Glide.with(getApplicationContext())
                                .load("http://192.168.4.109/myAPI/public/" + data.getString("avatar"))
                                .apply(new RequestOptions().override(100, 100))
                                .into(avatar);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(arrayRequest);
    }

    private void editProfil() {

        RequestQueue queue = Volley.newRequestQueue(this);

        if (bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        }
        String url = "http://192.168.4.109/myAPI/public/update-user/"+sharedPrefManager.getSPIdUser();
        StringRequest request = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
                return data;
            }
        };

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
}
