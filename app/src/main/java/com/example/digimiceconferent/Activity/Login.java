package com.example.digimiceconferent.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.digimiceconferent.MyUrl;
import com.example.digimiceconferent.R;
import com.example.digimiceconferent.SharedPrefManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    Button btnLogin, btnRegister;
    EditText email, password;
    SharedPrefManager sharedPrefManager;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = findViewById(R.id.bt_login);
        btnRegister = findViewById(R.id.bt_register);
        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);
        loading = new ProgressDialog(Login.this);
        sharedPrefManager = new SharedPrefManager(this);

        if (sharedPrefManager.getSPBoolean() && sharedPrefManager.getSPRole().equals("lead eo")) {
            Intent intent = new Intent(Login.this, HomePanitia.class);
            startActivity(intent);
            finish();
        } else if (sharedPrefManager.getSPBoolean() && sharedPrefManager.getSPRole().equals("eo")) {
            Intent intent = new Intent(Login.this, HomeAnggota.class);
            startActivity(intent);
            finish();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            private long lastClick = 0;
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - lastClick < 1000) {
                    return;
                }
                lastClick = SystemClock.elapsedRealtime();

                boolean isEmpty = false;
                String getEmail = email.getText().toString().trim();
                String getPass = password.getText().toString().trim();
                if (TextUtils.isEmpty(getEmail)) {
                    isEmpty = true;
                    email.setError("Email tidak boleh kosong");
                }

                if (TextUtils.isEmpty(getPass)) {
                    isEmpty = true;
                    password.setError("Password tidak boleh kosong");
                }

                if (!isEmpty) {
                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                    if (email.getText().toString().trim().matches(emailPattern)) {
                        loading.setMessage("Loading..");
                        loading.setCancelable(false);
                        loading.show();
                        login();
                    }else{
                        email.setError("Email tidak valid");
                    }
                }


            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
    }

    private void login() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = MyUrl.URL+"/login";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject data = null;
                try {

                    data = new JSONObject(response);
                    JSONObject getData = data.getJSONObject("result");
                    sharedPrefManager.saveSPString(sharedPrefManager.SP_TOKEN, getData.getString("token"));
                    sharedPrefManager.saveSPBoolean(sharedPrefManager.SP_BOOLEAN, true);
                    String role_team = getData.getString("role");
                    String id_user = getData.getString("user_id");
                    String email = getData.getString("email");
                    String name = getData.getString("name");

                    if(role_team.equals("lead eo")) {
                        sharedPrefManager.saveSPString(sharedPrefManager.SP_ROLE, role_team);
                        sharedPrefManager.saveSPString(sharedPrefManager.SP_ID_USER, id_user);
                        sharedPrefManager.saveSPString(sharedPrefManager.SP_EMAIL, email);
                        sharedPrefManager.saveSPString(sharedPrefManager.SP_NAME, name);
                        Intent intent = new Intent(Login.this, HomePanitia.class);
                        startActivity(intent);
                        finish();
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(), "Login Berhasil", Toast.LENGTH_SHORT).show();
                    } else if (role_team.equals("eo")) {
                        sharedPrefManager.saveSPString(sharedPrefManager.SP_ROLE, role_team);
                        sharedPrefManager.saveSPString(sharedPrefManager.SP_ID_USER, id_user);
                        sharedPrefManager.saveSPString(sharedPrefManager.SP_EMAIL, email);
                        sharedPrefManager.saveSPString(sharedPrefManager.SP_NAME, name);
                        Intent intent = new Intent(Login.this, HomeAnggota.class);
                        startActivity(intent);
                        finish();
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(), "Login Berhasil", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(getApplicationContext(), "Login Gagal", Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String, String> getParams(){
                Map<String, String> data = new HashMap<String, String>();
                data.put("email", email.getText().toString());
                data.put("password", password.getText().toString());
                return data;
            }
        };
        queue.getCache().clear();
        queue.add(stringRequest);
    }
}
